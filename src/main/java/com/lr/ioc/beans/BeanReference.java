package com.lr.ioc.beans;

import lombok.Data;

/**
 * 注入bean的引用占位对象
 */

@Data
public class BeanReference {

    private String name;

    public BeanReference(String name) {
        this.name = name;
    }
}
