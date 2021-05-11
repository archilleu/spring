package com.lr.ioc.beans;

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;

/**
 * bean定义，保存在{@link BeanFactory}中
 */


@Data
public class BeanDefinition {

    private Object bean;

    private String id;

    private Class beanClass;

    private String beanClassName;

    private String scope;

    private boolean lazyInit;

    private PropertyValues propertyValues = new PropertyValues();

    public void setBeanClassName(String beanClassName) throws ClassNotFoundException {
        this.beanClass = Class.forName(beanClassName);
        this.beanClassName = beanClassName;
    }

}
