package com.lr.ioc.support.processor.impl;

import com.lr.ioc.annotation.Autowired;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.aware.BeanFactoryAware;
import com.lr.ioc.support.processor.BeanPostProcessor;
import com.lr.ioc.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.Collection;

@Configuration
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws IocRuntimeException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws IocRuntimeException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws IocRuntimeException {
        // 1.获取bean所有成员
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 2.排除不包含{@link Autowired}注解的成员
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            Autowired autowired = field.getAnnotation(Autowired.class);
            Class<?> fieldTypeClass = field.getType();
            String value = autowired.value();

            // 3.1判断是否集合
            Object fieldValue;
            if (Collection.class.isAssignableFrom(fieldTypeClass)) {
                fieldValue = null;
                throw new IocRuntimeException("collection autowired not implement");
            } else {
                fieldValue = beanFactory.getTypeBean(fieldTypeClass, value);
            }

            ClassUtils.setFieldValue(field.getName(), bean, fieldValue);
        }
        return bean;
    }
}
