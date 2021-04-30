package com.lr.ioc.beans;

public interface BeanDefinitionReader {

    void loadBeanDefinitions(String location) throws Exception;

}
