package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.NewInstanceBean;
import com.lr.ioc.support.lifecycle.property.impl.DefaultBeanPropertyProcessor;
import com.lr.ioc.support.processor.BeanPostProcessor;

import java.util.List;

public class DefaultNewInstanceBean implements NewInstanceBean {

    private static final DefaultNewInstanceBean INSTANCE = new DefaultNewInstanceBean();

    public static DefaultNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        // 1 创建
        // 1.1工厂方法创建
        Object object = FactoryMethodNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
        if (null == object) {
            // 1.2 @Bean注解创建
            if (BeanSourceType.isConfigurationBean(beanDefinition.getSourceType())) {
                object = ConfigurationMethodBean.getInstance().newInstance(beanFactory, beanDefinition);
            } else {
                // 1.3构造器创建
                object = ConstructorNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
            }
        }
        if (null == object) {
            throw new IocRuntimeException("object create failed: " + beanDefinition.getBeanClassName());
        }

        // 2.对创建的bean进行加工
        List<BeanPostProcessor> beanPostProcessors = ((AbstractBeanFactory) beanFactory).getBeanPostProcessors();
        // 2.1加工前
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            object = beanPostProcessor.postProcessBeforeInitialization(object, beanDefinition.getId());
        }

        // 2.2设置属性
        DefaultBeanPropertyProcessor.getInstance()
                .propertyProcessor(beanFactory, object, beanDefinition.getPropertyValues());

        // 2.3加工后
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            object = beanPostProcessor.postProcessAfterInitialization(object, beanDefinition.getId());
        }

        // 4.返回
        return object;
    }

}
