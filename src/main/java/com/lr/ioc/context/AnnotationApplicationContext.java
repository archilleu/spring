package com.lr.ioc.context;

import com.lr.ioc.annotation.*;
import com.lr.ioc.aop.aspectj.AspectJAwareAdvisorAutoProxyCreator;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.constant.ScopeConst;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.lr.ioc.support.processor.impl.AutowiredAnnotationBeanPostProcessor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationApplicationContext extends AbstractApplicationContext {

    final private Class<?>[] configClasses;

    @Setter
    private BeanNameStrategy beanNameStrategy = new DefaultBeanNameStrategy();

    public AnnotationApplicationContext(Class<?>... configClasses) throws IocRuntimeException {
        this(new AbstractBeanFactory(), configClasses);
    }

    public AnnotationApplicationContext(AbstractBeanFactory beanFactory, Class<?>... configClasses) {
        super(beanFactory);

        this.configClasses = configClasses;

        super.refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) {
        for (Class<?> clazz : getConfigurationClassList()) {
            BeanDefinition beanDefinition = buildConfigurationBeanDefinition(clazz);
            if (null == beanDefinition) {
                continue;
            }

            beanFactory.registerBeanDefinition(beanDefinition);

            List<BeanDefinition> beanDefinitions = buildBeanAnnotationList(beanDefinition, clazz);
            beanDefinitions.forEach(definition -> beanFactory.registerBeanDefinition(definition));
        }
    }

    /**
     * 获取配置类列表，包含{@link Import}注解的
     */
    private List<Class<?>> getConfigurationClassList() {
        Set<Class<?>> configSet = new HashSet<>();
        for (Class<?> clazz : this.configClasses) {
            addImportClass(configSet, clazz);
        }

        //增加BeanPostProcessor
        configSet.add(AutowiredAnnotationBeanPostProcessor.class);
        configSet.add(AspectJAwareAdvisorAutoProxyCreator.class);

        return new ArrayList<>(configSet);
    }

    private void addImportClass(final Set<Class<?>> configSet, final Class<?> configClass) {
        configSet.add(configClass);
        if (!configClass.isAnnotationPresent(Import.class)) {
            return;
        }

        Class<?>[] classes = configClass.getAnnotation(Import.class).value();
        for (Class<?> clazz : classes) {
            configSet.add(clazz);

            // 递归，可能导入的class还导入其他class
            addImportClass(configSet, clazz);
        }
    }

    /**
     * 根据注解{@link Configuration}构建bean定义
     */
    private BeanDefinition buildConfigurationBeanDefinition(final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Configuration.class)) {
            return null;
        }

        Configuration configuration = clazz.getAnnotation(Configuration.class);
        String name = configuration.value();

        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(clazz.getName());
        beanDefinition.setLazyInit(getLazy(clazz));
        beanDefinition.setScope(getScope(clazz));
        if (StringUtils.isEmpty(name)) {
            name = beanNameStrategy.generateBeanName(beanDefinition);
        }
        beanDefinition.setId(name);
        beanDefinition.setSourceType(BeanSourceType.CONFIGURATION);
        if (clazz.isAnnotationPresent(Primary.class)) {
            beanDefinition.setPrimary(true);
        }

        return beanDefinition;
    }

    /**
     * 根据{@link Bean}注解构建bean定义
     */
    private List<BeanDefinition> buildBeanAnnotationList(final BeanDefinition configuration, final Class<?> clazz) {
        List<BeanDefinition> list = new LinkedList<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Bean.class)) {
                continue;
            }

            Bean bean = method.getAnnotation(Bean.class);
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            String beanName = bean.value();
            if (StringUtils.isEmpty(beanName)) {
                beanName = methodName;
            }

            BeanDefinition beanDefinition = new BeanDefinition();
            beanDefinition.setId(beanName);
            beanDefinition.setBeanClassName(returnType.getName());
            beanDefinition.setInitialize(bean.initMethod());
            beanDefinition.setDestroy(bean.destroyMethod());
            beanDefinition.setLazyInit(getLazy(method));
            beanDefinition.setScope(getScope(method));

            beanDefinition.setSourceType(BeanSourceType.CONFIGURATION_BEAN);
            beanDefinition.setConfigurationName(configuration.getId());
            beanDefinition.setConfigurationBeanMethod(methodName);
            if (method.isAnnotationPresent(Primary.class)) {
                beanDefinition.setPrimary(true);
            }

            list.add(beanDefinition);
        }

        return list;
    }

    private String getScope(final Method method) {
        if (method.isAnnotationPresent(Scope.class)) {
            Scope scope = method.getAnnotation(Scope.class);
            return scope.value();
        }

        return ScopeConst.SINGLETON;
    }

    private String getScope(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope scope = clazz.getAnnotation(Scope.class);
            return scope.value();
        }

        return ScopeConst.SINGLETON;
    }

    private boolean getLazy(final Method method) {
        if (method.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = method.getAnnotation(Lazy.class);
            return lazy.value();
        }

        return false;
    }

    private boolean getLazy(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = clazz.getAnnotation(Lazy.class);
            return lazy.value();
        }

        return false;
    }

}
