package com.lr.ioc.beans;

import com.lr.ioc.exception.IocRuntimeException;

public class BeanInitializeLogger implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws IocRuntimeException {
        System.out.println("Initialize bean " + beanName + " start!");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws IocRuntimeException {
        System.out.println("Initialize bean " + beanName + " end!");
        return bean;
    }
}
