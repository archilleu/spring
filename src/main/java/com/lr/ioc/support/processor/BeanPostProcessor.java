package com.lr.ioc.support.processor;

import com.lr.ioc.exception.IocRuntimeException;

public interface BeanPostProcessor extends PostProcessor {

    /**
     * bean 完成初始化之前
     *
     * @param bean
     * @param beanName
     * @return
     * @throws IocRuntimeException
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws IocRuntimeException;

    /**
     * bean完成初始化之后
     *
     * @param bean
     * @param beanName
     * @return
     * @throws IocRuntimeException
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws IocRuntimeException;

}
