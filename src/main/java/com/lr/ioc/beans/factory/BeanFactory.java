package com.lr.ioc.beans.factory;

/**
 * bean容器接口
 */

public interface BeanFactory {

    Object getBean(String name) throws Exception;

    <T> T getBean(String name, Class<T> clazz) throws Exception;
}
