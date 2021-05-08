package com.lr.ioc.aop.proxy;

import lombok.Getter;

/**
 * 被代理对象
 */

@Getter
public class TargetSource {

    private Object target;

    private Class<?> targetClass;

    private Class<?>[] interfaces;

    public TargetSource(Object target, Class<?> targetClass, Class<?>... interfaces) {
        this.target = target;
        this.targetClass = targetClass;
        this.interfaces = interfaces;
    }
}
