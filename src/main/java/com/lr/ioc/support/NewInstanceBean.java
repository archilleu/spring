package com.lr.ioc.support;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;

/**
 * （1）直接使用 {@link BeanDefinition#getFactoryMethod()} 创建对象
 * （2）根据构造器创建对象
 * 2.1 默认无参构造器
 * 2.2 通过指定的构造器信息创建
 */
public interface NewInstanceBean {

    Object newInstance(final BeanFactory beanFactory, final BeanDefinition beanDefinition);
}
