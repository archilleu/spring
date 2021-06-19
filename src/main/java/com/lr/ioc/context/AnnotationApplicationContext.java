package com.lr.ioc.context;

import com.lr.ioc.annotation.*;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.support.annotation.Lazes;
import com.lr.ioc.support.annotation.Scopes;
import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;
import com.lr.ioc.support.processor.impl.AspectJAwareAdvisorAutoProxyCreator;
import com.lr.ioc.support.processor.impl.AutowiredAnnotationBeanPostProcessor;
import com.lr.ioc.support.scanner.impl.ClassPathAnnotationBeanDefinitionScanner;
import com.lr.ioc.support.scanner.impl.DefaultBeanDefinitionScannerContext;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

public class AnnotationApplicationContext extends AbstractApplicationContext {

    final private Class<?>[] configClasses;

    @Setter
    private BeanNameStrategy beanNameStrategy = new DefaultBeanNameStrategy();

    public AnnotationApplicationContext(Class<?>... configClasses) {
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

            // {@link @Bean}定义
            List<BeanDefinition> beanDefinitions = buildBeanAnnotationList(beanDefinition, clazz);
            beanDefinitions.forEach(definition -> beanFactory.registerBeanDefinition(definition));

            // {@link ComponentScan}定义
            buildScanBeanDefinitionList(clazz).forEach(definition -> {
                beanFactory.registerBeanDefinition(definition);
            });
        }
    }

    /**
     * 自动扫描注解类
     *
     * @param clazz 可能包含{@link ComponentScan}类
     * @return bean定义集合
     */
    private List<BeanDefinition> buildScanBeanDefinitionList(final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(ComponentScan.class)) {
            return new LinkedList<>();
        }

        ComponentScan componentScan =  clazz.getAnnotation(ComponentScan.class);

        DefaultBeanDefinitionScannerContext context = new DefaultBeanDefinitionScannerContext();
        if (componentScan.value().length == 0) {
            //默认扫描包下的所有类
            String defaultPackage = clazz.getPackage().getName();
            context.setScanPackages(new LinkedList<String>() {{
                add(defaultPackage);
            }});
        } else {
            context.setScanPackages(Arrays.asList(componentScan.value()));
        }
        context.setBeanNameStrategy(componentScan.beanNameStrategy());
        context.setIncludes(Arrays.asList(componentScan.includes()));
        context.setExcludes(Arrays.asList(componentScan.excludes()));

        ClassPathAnnotationBeanDefinitionScanner scanner = new ClassPathAnnotationBeanDefinitionScanner();
        scanner.setBeanFactory(beanFactory);
        return scanner.scan(context);
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
        beanDefinition.setLazyInit(Lazes.getLazy(clazz));
        beanDefinition.setScope(Scopes.getScope(clazz));
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
            beanDefinition.setLazyInit(Lazes.getLazy(method));
            beanDefinition.setScope(Scopes.getScope(method));

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

}
