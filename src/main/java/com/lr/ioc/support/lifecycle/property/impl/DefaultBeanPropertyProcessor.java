package com.lr.ioc.support.lifecycle.property.impl;

import com.lr.ioc.aop.aspectj.BeanFactoryAware;
import com.lr.ioc.beans.BeanReference;
import com.lr.ioc.beans.PropertyValues;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.lifecycle.property.BeanPropertyProcessor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DefaultBeanPropertyProcessor implements BeanPropertyProcessor {

    private static final DefaultBeanPropertyProcessor INSTANCE = new DefaultBeanPropertyProcessor();

    public static DefaultBeanPropertyProcessor getInstance() {
        return INSTANCE;
    }

    @Override
    public void propertyProcessor(BeanFactory beanFactory, Object instance, PropertyValues propertyValues) {
        if (propertyValues.getPropertyValues().isEmpty()) {
            return;
        }

        propertyValues.getPropertyValues().forEach((propertyValue -> {
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();

            // aop需要设置BeanFactory
            if (value instanceof BeanFactoryAware) {
                ((BeanFactoryAware) value).setBeanFactory(beanFactory);
            }

            // 是否为引用对象
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = beanFactory.getBean(beanReference.getName());
            }

            //是否定义setter方法
            try {
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method declareMethod = instance.getClass().getDeclaredMethod(methodName, value.getClass());
                declareMethod.setAccessible(true);
                declareMethod.invoke(instance, value);
            } catch (NoSuchMethodException ex) {
                try {
                    Class<?> clazz = instance.getClass();
                    Field declaredField = clazz.getDeclaredField(name);
                    declaredField.setAccessible(true);
                    declaredField.set(instance, value);
                } catch (Exception e) {
                    throw new IocRuntimeException(e);
                }
            } catch (Exception e) {
                throw new IocRuntimeException(e);
            }
        }));
    }
}
