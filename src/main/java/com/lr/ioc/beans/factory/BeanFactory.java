package com.lr.ioc.beans.factory;

import com.lr.ioc.exception.IocRuntimeException;

import java.util.List;

/**
 * bean容器接口
 */

public interface BeanFactory {

    /**
     * 获取bean
     *
     * @param name
     * @return
     * @throws IocRuntimeException
     */
    Object getBean(final String name) throws IocRuntimeException;

    /**
     * 获取bean
     *
     * @param name
     * @param clazz
     * @param <T>
     * @return
     * @throws IocRuntimeException
     */
    <T> T getBean(final String name, Class<T> clazz) throws IocRuntimeException;

    /**
     * 获取指定类型的bean
     *
     * @param type
     * @param <T>
     * @return
     */
    <T> List<T> getBeans(final Class<T> type);

    <T> T getTypeBean(Class<T> requiredType, String name);

    /**
     * 判断是否包含bean
     *
     * @param name
     * @return
     */
    boolean containsBean(final String name);

    /**
     * G
     * 判断bean类型是否匹配
     *
     * @param name
     * @param type
     * @return
     */
    boolean isTypeMatch(final String name, final Class type);

    /**
     * 获取bean类型
     *
     * @param name
     * @return
     */
    Class<?> getType(final String name);
}
