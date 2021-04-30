package com.lr.ioc.beans;

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;

/**
 * bean定义，保存在{@link BeanFactory}中
 */


@Data
public class BeanDefinition {

    private Object bean;

    private Class beanClass;

    private String beanClassName;

    private PropertyValues propertyValues = new PropertyValues();

    public void setBeanClassName(String beanClassName) {
        try {
            this.beanClass = Class.forName(beanClassName);
            this.beanClassName = beanClassName;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

}
