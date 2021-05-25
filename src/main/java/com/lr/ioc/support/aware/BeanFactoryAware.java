package com.lr.ioc.support.aware;

import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;

public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory) throws IocRuntimeException;

}
