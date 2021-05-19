package com.lr.ioc.support.lifecycle.property.impl;

import com.lr.ioc.aop.aspectj.BeanFactoryAware;
import com.lr.ioc.beans.BeanReference;
import com.lr.ioc.beans.PropertyValues;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.support.lifecycle.property.BeanPropertyProcessor;
import com.lr.ioc.util.ClassUtils;

public class DefaultBeanPropertyProcessor implements BeanPropertyProcessor {

    private static final DefaultBeanPropertyProcessor INSTANCE = new DefaultBeanPropertyProcessor();

    public static DefaultBeanPropertyProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public void propertyProcessor(BeanFactory beanFactory, Object instance, PropertyValues propertyValues) {
        // aop需要设置BeanFactory
        if (instance instanceof BeanFactoryAware) {
            ((BeanFactoryAware) instance).setBeanFactory(beanFactory);
        }

        if (propertyValues.getPropertyValues().isEmpty()) {
            return;
        }

        propertyValues.getPropertyValues().forEach((propertyValue -> {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();

            // 是否为引用对象
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = beanFactory.getBean(beanReference.getName());
            }

            ClassUtils.setFieldValue(name, instance, value);
        }));
    }
}
