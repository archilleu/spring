package com.lr.ioc.beans;

import lombok.Data;

/**
 * 注入bean的bean引用
 */

@Data
public class BeanReference {

    private String name;

    //private Object bean;

    public BeanReference(String name) {
        this.name = name;
    }
}
