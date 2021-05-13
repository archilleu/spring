package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.annotation.FactoryMethod;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.util.ClassUtils;

import java.lang.reflect.Method;

public class FactoryMethodNewInstanceBean extends AbstractNewInstanceBean {

    private static final FactoryMethodNewInstanceBean INSTANCE = new FactoryMethodNewInstanceBean();

    static FactoryMethodNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    protected Object newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class<?> beanClass) {
        if (null != beanDefinition.getFactoryMethod()) {
            Object obj = newInstance(beanClass, beanDefinition.getFactoryMethod());
            if (null != obj) {
                return obj;
            }
        }

        return newInstance(beanClass);
    }

    /**
     * 根据指定的factoryMethod创建对象
     *
     * @param clazz
     * @param factoryMethodName
     * @return
     */
    private Object newInstance(final Class<?> clazz, final String factoryMethodName) {
        Method factoryMethod = ClassUtils.getMethod(clazz, factoryMethodName);
        return ClassUtils.invokeFactoryMethod(clazz, factoryMethod);
    }

    /**
     * 根据注解获取对应的信息
     *
     * @param clazz
     * @return
     */
    private Object newInstance(final Class<?> clazz) {
        Method factoryMethod = ClassUtils.getAnnotationMethod(clazz, FactoryMethod.class);
        if (null == factoryMethod) {
            return null;
        }
        return ClassUtils.invokeFactoryMethod(clazz, factoryMethod);
    }
}
