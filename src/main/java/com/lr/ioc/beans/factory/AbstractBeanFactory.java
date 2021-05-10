package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanPostProcessor;
import com.lr.ioc.constant.enums.ScopeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBeanFactory implements BeanFactory {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<String> beanDefinitionNames = new ArrayList<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(String name) throws Exception {
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
    public <T> T getBean(String name, Class<T> clazz) throws Exception {
        return (T) getBean(name);
    }

    protected Object initializeBean(Object bean, String beanName) throws Exception {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }

        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            bean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
        }

        return bean;
    }

    protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
        Object bean = createBeanInstance(beanDefinition);
        applyPropertyValues(bean, beanDefinition);
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
        Object bean = beanDefinition.getBeanClass().newInstance();
        return bean;
    }

    protected void applyPropertyValues(Object bean, BeanDefinition mbd) throws Exception {

    }

    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
        beanDefinitionMap.put(name, beanDefinition);
        beanDefinitionNames.add(name);
    }

    // 预加载bean
    public void preInstantiateSingletons() throws Exception {
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            if (entry.getValue().isLazyInit()) {
                continue;
            } else {
                getBean(entry.getKey());
            }
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    // 获取class类型的bean
    public List getBeansForType(Class type) throws Exception {
        List beans = new ArrayList<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            if (type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())) {
                beans.add(getBean(beanDefinitionName));
            }
        }

        return beans;
    }
}
