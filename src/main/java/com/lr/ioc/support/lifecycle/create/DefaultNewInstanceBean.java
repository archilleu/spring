package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.support.NewInstanceBean;

public class DefaultNewInstanceBean implements NewInstanceBean {

    private static final DefaultNewInstanceBean INSTANCE = new DefaultNewInstanceBean();

    public static DefaultNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        // 1.工厂方法创建
        Object object = FactoryMethodNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
        if (null != object) {
            return object;
        }

        // 2.构造器创建
        return ConstructorNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
    }
}
