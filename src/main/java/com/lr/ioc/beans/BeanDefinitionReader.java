package com.lr.ioc.beans;

import com.lr.ioc.exception.IocRuntimeException;

public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws IocRuntimeException;

}
