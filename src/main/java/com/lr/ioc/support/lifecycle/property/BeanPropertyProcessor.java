package com.lr.ioc.support.lifecycle.property;

import com.lr.ioc.beans.PropertyValues;
import com.lr.ioc.beans.factory.BeanFactory;

/**
 * 对象属性设置接口
 * 1.非引用属性
 * 2.引用属性
 */
public interface BeanPropertyProcessor {

    void propertyProcessor(final BeanFactory beanFactory,
                           final Object instance,
                           final PropertyValues propertyValues);

}
