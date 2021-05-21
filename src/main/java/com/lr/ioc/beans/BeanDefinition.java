package com.lr.ioc.beans;

import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

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
     * 来源
     */
    private BeanSourceType sourceType;

    /**
     * 成员类，限定BeanSourceType#CONFIGURATION_BEAN使用
     */
    private String configurationName;
    private String configurationBeanMethod;
    private boolean primary;

    /**
     * 对象工厂方法(必须静态、无参、返回对象)
     */
    private String factoryMethod;

    /**
     * 构造器列表
     *
     * @since 0.0.6
     */
    private List<ConstructorArgDefinition> constructorArgList;

    /**
     * 对象属性
     */
    private PropertyValues propertyValues = new PropertyValues();

    public void setBeanClassName(String beanClassName) {
        try {
            this.beanClass = Class.forName(beanClassName);
            this.beanClassName = beanClassName;
        } catch (ClassNotFoundException e) {
            throw new IocRuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, id);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, id);
    }
}
