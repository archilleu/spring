package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanPostProcessor;
import com.lr.ioc.constant.enums.ScopeEnum;
import com.lr.ioc.exception.IocRuntimeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<String> beanDefinitionNames = new ArrayList<>();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws IocRuntimeException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IllegalArgumentException(("No bean named " + name + " is defined"));
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
    public <T> T getBean(String name, Class<T> clazz) throws IocRuntimeException {
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

    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanDefinition);
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        Object bean;
        try {
            bean = beanDefinition.getBeanClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
        return bean;
    }

    protected void applyPropertyValues(Object bean, BeanDefinition mbd) {
    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
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

    // 获取class类型的bean
    @SuppressWarnings("all")
    public List getBeansForType(Class type) {
        List beans = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }

        return beans;
    }
}
