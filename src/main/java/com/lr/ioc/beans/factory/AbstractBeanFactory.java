package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanPostProcessor;
import com.lr.ioc.constant.enums.ScopeEnum;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.lifecycle.create.DefaultNewInstanceBean;
import com.lr.ioc.support.lifecycle.destroy.DefaultPreDestroyBean;
import com.lr.ioc.support.lifecycle.destroy.DisposableBean;
import com.lr.ioc.support.lifecycle.init.DefaultPostConstructBean;
import com.lr.ioc.support.lifecycle.init.InitializingBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<Class, Set<String>> typeBeanNameMap = new ConcurrentHashMap<>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(final String name) throws IocRuntimeException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IocRuntimeException(("No bean named " + name + " is defined"));
        }

        Object bean;
        final String scope = beanDefinition.getScope();
        if (ScopeEnum.PROTOTYPE.getCode().equals(scope)) {
            bean = doCreateBean(beanDefinition);
            bean = initializeBean(bean, name);
        } else {
            bean = beanDefinition.getBean();
            if (bean == null) {
                bean = doCreateBean(beanDefinition);
                bean = initializeBean(bean, name);
                beanDefinition.setBean(bean);
            }
        }

        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final String name, final Class<T> clazz) throws IocRuntimeException {
        return (T) getBean(name);
    }

    protected Object initializeBean(Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }

        return bean;
    }

    @Override
    public <T> List<T> getBeans(final Class<T> type) {
        List<T> list = new LinkedList<>();
        Set<String> names = getBeanNames(type);
        if (null == names || names.isEmpty()) {
            return list;
        }

        names.forEach((name) -> list.add(getBean(name, type)));
        return list;
    }

    @Override
    public boolean containsBean(final String name) {
        return beanDefinitionMap.keySet().contains(name);
    }

    @Override
    public boolean isTypeMatch(final String name, final Class type) {
        Class<?> beanType = getType(name);
        return beanType.equals(type);
    }

    @Override
    public Class<?> getType(final String name) {
        Object bean = getBean(name);
        return bean.getClass();
    }

    public Set<String> getBeanNames(final Class type) {
        return typeBeanNameMap.get(type);
    }

    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanDefinition);

        // 初始化完成后调用
        InitializingBean initializingBean = new DefaultPostConstructBean(bean, beanDefinition);
        initializingBean.initialize();
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return DefaultNewInstanceBean.getInstance().newInstance(this, beanDefinition);
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);

        beanDefinitionNames.add(name);

        Set<String> beanNameSet = typeBeanNameMap.get(beanDefinition.getBeanClass());
        if (null == beanNameSet) {
            beanNameSet = new HashSet<>();
        }
        beanNameSet.add(name);
        typeBeanNameMap.put(beanDefinition.getBeanClass(), beanNameSet);

        return;
    }

    // 预加载bean
    public void preInstantiateSingletons() {
        beanDefinitionMap.forEach((name, beanDefinition) -> {
            if (beanDefinition.isLazyInit()) {
                return;
            }
            getBean(name);
        });
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public void destroy() {
        beanDefinitionMap.forEach((name, beanDefinition) -> {
            DisposableBean disposableBean = new DefaultPreDestroyBean(beanDefinition);
            disposableBean.destroy();
        });
    }
}
