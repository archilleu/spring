package com.lr.ioc.util;

import com.lr.ioc.exception.IocRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassUtils {

    /**
     * 根据无参构造函数创建类实例
     */
    public static Object newInstance(final Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
    }

    /**
     * 根据有参构造函数创建实例
     */
    public static Object newInstance(final Constructor<?> constructor, final Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... paramTypes) {
        try {
            return clazz.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static Method getAnnotationMethod(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        final Method[] methods = clazz.getMethods();
        if (methods.length == 0) {
            return null;
        }

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationClass)) {
                return method;
            }
        }

        return null;
    }

    public static Method getMethod(final Class<?> clazz, String methodName) {
        try {
            return clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static Object invokeFactoryMethod(final Class<?> clazz, final Method factoryMethod) {
        // 1. 信息校验
        // 1.1 无参
        Class<?>[] paramTypes = factoryMethod.getParameterTypes();
        if (paramTypes.length != 0) {
            throw new IocRuntimeException(factoryMethod.getName() + " must be has no params.");
        }

        // 1.2 静态
        if (!Modifier.isStatic(factoryMethod.getModifiers())) {
            throw new IocRuntimeException(factoryMethod.getName() + " must be static.");
        }

        // 1.3 返回对象
        Class<?> returnType = factoryMethod.getReturnType();
        if (!returnType.isAssignableFrom(clazz)) {
            throw new IocRuntimeException(factoryMethod.getName() + " must be return " + clazz.getName());
        }

        // 2. 反射调用
        try {
            return factoryMethod.invoke(null, factoryMethod);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }
}
