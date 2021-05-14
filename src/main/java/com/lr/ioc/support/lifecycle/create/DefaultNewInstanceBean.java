package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.NewInstanceBean;
import com.lr.ioc.support.lifecycle.property.impl.DefaultBeanPropertyProcessor;

public class DefaultNewInstanceBean implements NewInstanceBean {

    private static final DefaultNewInstanceBean INSTANCE = new DefaultNewInstanceBean();

    public static DefaultNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    public Object newInstance(BeanFactory beanFactory, BeanDefinition beanDefinition) {
        // 1.工厂方法创建
        Object object = FactoryMethodNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
        if (null == object) {
            // 2.构造器创建
            object = ConstructorNewInstanceBean.getInstance().newInstance(beanFactory, beanDefinition);
        }
        if (null == object) {
            throw new IocRuntimeException("object create failed: " + beanDefinition.getBeanClassName());
        }

        // 3.设置属性
        DefaultBeanPropertyProcessor.getInstance()
                .propertyProcessor(beanFactory, object, beanDefinition.getPropertyValues());

        // 4.返回
        return object;
    }
}
