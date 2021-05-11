package com.lr.ioc.beans;

import com.lr.ioc.exception.IocRuntimeException;

public interface BeanPostProcessor {

    Object postProcessBeforeInitialization(Object bean, String beanName) throws IocRuntimeException;

    Object postProcessAfterInitialization(Object bean, String beanName) throws IocRuntimeException;

}
