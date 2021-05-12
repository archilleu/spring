package com.lr.ioc.beans;

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;

/**
 * bean定义，保存在{@link BeanFactory}中
 */


@Data
public class BeanDefinition {

    /**
     * 对象实例
     */
    private Object bean;

    /**
     * 对象id（唯一）
     */
    private String id;

    /**
     * 对象类型
     */
    private Class beanClass;

    /**
     * 对象类型名称
     */
    private String beanClassName;

    /**
     * 对象实例化方式
     */
    private String scope;

    /**
     * 是否懒加载
     */
    private boolean lazyInit;

    /**
     * 对象初始化方法
     */
    private String initialize;

    /**
     * 对象销毁方法
     */
    private String destroy;

    /**
     * 对象属性
     */
    private PropertyValues propertyValues = new PropertyValues();

    public void setBeanClassName(String beanClassName) throws ClassNotFoundException {
        this.beanClass = Class.forName(beanClassName);
        this.beanClassName = beanClassName;
    }

}
