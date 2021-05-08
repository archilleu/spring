package com.lr.ioc.aop.aspectj;

import com.lr.ioc.beans.factory.BeanFactory;

public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory) throws Exception;

}
