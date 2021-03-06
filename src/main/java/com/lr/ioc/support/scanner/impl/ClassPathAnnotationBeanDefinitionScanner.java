package com.lr.ioc.support.scanner.impl;

import com.lr.ioc.annotation.*;
import com.lr.ioc.aop.aspectj.AbstractAspectJAdvice;
import com.lr.ioc.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.PropertyValue;
import com.lr.ioc.beans.PropertyValues;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.annotation.Lazes;
import com.lr.ioc.support.annotation.Scopes;
import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.lr.ioc.support.scanner.AnnotationBeanDefinitionScanner;
import com.lr.ioc.support.scanner.BeanDefinitionScannerContext;
import com.lr.ioc.util.ClassAnnotationTypeMetadata;
import com.lr.ioc.util.ClassUtils;
import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Data
public class ClassPathAnnotationBeanDefinitionScanner implements AnnotationBeanDefinitionScanner {

    private BeanDefinitionScannerContext context;

    private BeanFactory beanFactory;

    @Override
    public List<BeanDefinition> scan(BeanDefinitionScannerContext context) {
        this.context = context;

        Set<String> scanClassNameSet = new HashSet<>();
        //1.获取所有类全称
        context.getScanPackages().forEach((packageName) -> {
            Set<String> classNameSet = scanPackageClassNameSet(packageName);
            scanClassNameSet.addAll(classNameSet);
        });

        //2.构建类定义
        List<Class<?>> classList = new LinkedList<>();
        scanClassNameSet.forEach((className) -> {
            classList.add(ClassUtils.getClass(className));
        });

        //3.添加过滤
        List<BeanDefinition> beanDefinitionList = new LinkedList<>();
        BeanNameStrategy beanNameStrategy = new DefaultBeanNameStrategy();
        classList.forEach((clazz) -> {
            ClassAnnotationTypeMetadata typeMetadata = new ClassAnnotationTypeMetadata(clazz);

            // @Aspect优先级高于@Component
            if (isTypeAnnotationMatchAspect(typeMetadata, clazz)) {
                //3.1构建bean定义
                buildAspectBeanDefinition(clazz, beanNameStrategy, beanDefinitionList);
                return;
            }

            if (isTypeAnnotationMatchComponent(typeMetadata, clazz)) {
                //3.2构建bean定义
                BeanDefinition beanDefinition = buildComponentBeanDefinition(clazz, typeMetadata, beanNameStrategy);
                beanDefinitionList.add(beanDefinition);
                return;
            }

        });

        return beanDefinitionList;
    }

    private Set<String> scanPackageClassNameSet(final String packageName) {
        Set<String> classNameSet = new HashSet<>();

        try {
            String packageDirName = packageName.replace('.', '/');
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();

                // 文件处理
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File file = new File(filePath);

                    // 递归处理下面的文件明细
                    if (file.isDirectory()) {
                        File[] files = file.listFiles();
                        if (files.length != 0) {
                            for (File entry : files) {
                                recursiveFile(packageName, entry, classNameSet);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new IocRuntimeException(e);
        }

        return classNameSet;
    }

    private static void recursiveFile(String packageNamePrefix, File file, Set<String> classNameSet) {
        // 如果是文件
        if (file.isFile()) {
            String fileName = file.getName().split("\\.")[0];
            String className = packageNamePrefix + "." + fileName;

            classNameSet.add(className);
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                String dirName = file.getName();
                packageNamePrefix += "." + dirName;
                if (files.length != 0) {
                    for (File fileEntry : files) {
                        // 递归处理
                        recursiveFile(packageNamePrefix, fileEntry, classNameSet);
                    }
                }
            }
        }
    }

    private boolean isTypeAnnotationMatchComponent(ClassAnnotationTypeMetadata typeMetadata, Class<?> clazz) {

        List<Class<? extends Annotation>> includes = context.getIncludes();
        List<Class<? extends Annotation>> excludes = context.getExcludes();

        if (null != typeMetadata.isAnnotatedOrMeta(includes)
                && null == typeMetadata.isAnnotatedOrMeta(excludes)) {
            return true;
        }

        return false;
    }

    private boolean isTypeAnnotationMatchAspect(ClassAnnotationTypeMetadata typeMetadata, Class<?> clazz) {
        // @Aspect
        if (typeMetadata.isAnnotated(Aspect.class)) {
            return true;
        }

        return false;
    }

    private BeanDefinition buildComponentBeanDefinition(Class<?> clazz,
                                                        ClassAnnotationTypeMetadata typeMetadata,
                                                        BeanNameStrategy beanNameStrategy) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        beanDefinition.setLazyInit(Lazes.getLazy(clazz));
        beanDefinition.setScope(Scopes.getScope(clazz));
        beanDefinition.setSourceType(BeanSourceType.COMPONENT);
        Object id = typeMetadata.getDirectComponentAnnotationName(Component.class, "value");
        if (null == id) {
            beanDefinition.setId(beanNameStrategy.generateBeanName(beanDefinition));
        } else {
            if (StringUtils.isEmpty((String) id)) {
                beanDefinition.setId(beanNameStrategy.generateBeanName(beanDefinition));
            } else {
                beanDefinition.setId((String) id);
            }
        }
        if (clazz.isAnnotationPresent(Primary.class)) {
            beanDefinition.setPrimary(true);
        }

        return beanDefinition;
    }

    private void buildAspectBeanDefinition(Class<?> clazz,
                                           BeanNameStrategy beanNameStrategy,
                                           List<BeanDefinition> list) {
        /*
         * 需要生成两个BeanDefinition对象
         * 1.AOP对象(@Aspect注解的类)
         * 2.AOP表达式对象(PointcutAdvisor对象)
         */

        Method[] methods = clazz.getMethods();
        int aopType;
        Annotation annotation;
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                annotation = method.getAnnotation(Before.class);
                aopType = AOP_TYPE_BEFORE;
            } else if (method.isAnnotationPresent(Around.class)) {
                annotation = method.getAnnotation(Around.class);
                aopType = AOP_TYPE_AROUND;
            } else if (method.isAnnotationPresent(After.class)) {
                annotation = method.getAnnotation(After.class);
                aopType = AOP_TYPE_AFTER;
            } else {
                continue;
            }
            String methodName = getAnnotation(annotation);
            buildAspectJBeanDefinition(clazz, method, methodName, aopType, list, beanNameStrategy);
        }
    }

    // 获取@Before，@Around，@After的value值
    private String getAnnotation(Annotation annotation) {
        InvocationHandler h = Proxy.getInvocationHandler(annotation);
        try {
            Field field = h.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map<String, Object> values = (Map<String, Object>) field.get(h);
            String methodName = (String) values.get("value");
            if (StringUtils.isEmpty(methodName)) {
                throw new IocRuntimeException(annotation.annotationType().getName() + "  must has value!");
            }

            methodName = methodName.substring(0, methodName.length() - 2);
            return methodName;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
    }

    private void buildAspectJBeanDefinition(Class<?> aopClazz,
                                            Method aopMethod,
                                            String pointcutName,
                                            int aopType,
                                            List<BeanDefinition> list,
                                            BeanNameStrategy beanNameStrategy) {
        // 1.检查pointcutName方法，是否标注了@Pointcut
        String expression = null;
        Method pointcutMethod = ClassUtils.getMethod(aopClazz, pointcutName);
        if (pointcutMethod.isAnnotationPresent(Pointcut.class)) {
            Pointcut pointcut = pointcutMethod.getAnnotation(Pointcut.class);
            expression = pointcut.value();
            if (StringUtils.isEmpty(expression)) {
                throw new IocRuntimeException(aopClazz.getName() + "#" + pointcutName + " @Pointcut must has value!");
            }
        }

        // 2.AOP对象

        String aopId = beanNameStrategy.generateBeanNameByClass(aopClazz);
        BeanDefinition beanDefinition = findAopDefinition(list, aopId);
        if (null == beanDefinition) {
            beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(aopClazz.getName());
            beanDefinition.setId(aopId);
            beanDefinition.setSourceType(BeanSourceType.AOP);
            list.add(beanDefinition);
        } else {
            // 只创建一次就好了
        }

        // 3.构建AspectJExpressionPointcutAdvisor对象定义
        String pointcutAdvisorId = beanNameStrategy.generateBeanNameByClass(AspectJExpressionPointcutAdvisor.class);
        beanDefinition = findAopDefinition(list, pointcutAdvisorId);
        if (null == beanDefinition) {
            beanDefinition = new BeanDefinition();
            beanDefinition.setBeanClassName(AspectJExpressionPointcutAdvisor.class.getName());
            beanDefinition.setSourceType(BeanSourceType.AOP);
            beanDefinition.setId(pointcutAdvisorId);

            PropertyValues propertyValues = new PropertyValues();
            AbstractAspectJAdvice abstractAspectJAdvice = new AbstractAspectJAdvice();
            abstractAspectJAdvice.setBeanFactory(beanFactory);
            abstractAspectJAdvice.setAspectInstanceName(aopId);
            Method invoke = null;
            try {
                invoke = AbstractAspectJAdvice.class.getMethod("invoke", MethodInvocation.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            abstractAspectJAdvice.setAspectJAdviceMethod(invoke);
            switch (aopType) {
                case AOP_TYPE_BEFORE:
                    abstractAspectJAdvice.setAspectJAdviceMethodBefore(aopMethod);
                    break;
                case AOP_TYPE_AROUND:
                    abstractAspectJAdvice.setAspectJAdviceMethodAround(aopMethod);
                    break;
                case AOP_TYPE_AFTER:
                    abstractAspectJAdvice.setAspectJAdviceMethodAfter(aopMethod);
                    break;

                default:
                    throw new IocRuntimeException("Aop type error!");
            }
            propertyValues.addPropertyValue(new PropertyValue("advice", abstractAspectJAdvice));
            propertyValues.addPropertyValue(new PropertyValue("expression", expression));
            beanDefinition.setPropertyValues(propertyValues);
            list.add(beanDefinition);
        } else {
            // 修改切面
            AbstractAspectJAdvice abstractAspectJAdvice = null;
            for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
                if (propertyValue.getName().equals("advice")) {
                    abstractAspectJAdvice = (AbstractAspectJAdvice) propertyValue.getValue();
                    break;
                }
            }
            switch (aopType) {
                case AOP_TYPE_BEFORE:
                    abstractAspectJAdvice.setAspectJAdviceMethodBefore(aopMethod);
                    break;
                case AOP_TYPE_AROUND:
                    abstractAspectJAdvice.setAspectJAdviceMethodAround(aopMethod);
                    break;
                case AOP_TYPE_AFTER:
                    abstractAspectJAdvice.setAspectJAdviceMethodAfter(aopMethod);
                    break;

                default:
                    throw new IocRuntimeException("Aop type error!");
            }
        }

        return;
    }

    /**
     * 查找@AspectJ是否已经生成了Bean Definition，一个@AspectJ只生成一份定义
     *
     * @param list
     * @param id
     * @return
     */
    private BeanDefinition findAopDefinition(List<BeanDefinition> list, String id) {
        for (BeanDefinition beanDefinition : list) {
            if (beanDefinition.getId().equals(id)) {
                return beanDefinition;
            }
        }

        return null;
    }

    private final static int AOP_TYPE_BEFORE = 1;
    private final static int AOP_TYPE_AROUND = 2;
    private final static int AOP_TYPE_AFTER = 3;
}
