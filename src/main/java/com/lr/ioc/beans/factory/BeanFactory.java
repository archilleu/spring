package com.lr.ioc.beans.factory;

import com.lr.ioc.exception.IocRuntimeException;

/**
 * bean容器接口
 */

public interface BeanFactory {

    Object getBean(String name) throws IocRuntimeException;

    <T> T getBean(String name, Class<T> clazz) throws IocRuntimeException;
}
