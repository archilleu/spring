package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.support.NewInstanceBean;

abstract class AbstractNewInstanceBean implements NewInstanceBean {

    protected abstract Object newInstanceOpt(final BeanFactory beanFactory
            , final BeanDefinition beanDefinition, final Class<?> beanClass);

    @Override
    public Object newInstance(final BeanFactory beanFactory, BeanDefinition beanDefinition) {
        Class beanClass = beanDefinition.getBeanClass();
        return newInstanceOpt(beanFactory, beanDefinition, beanClass);
    }
}
