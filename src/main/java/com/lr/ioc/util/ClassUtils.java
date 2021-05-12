package com.lr.ioc.util;

import com.lr.ioc.exception.IocRuntimeException;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ClassUtils {

    /**
     * 根据无参构造函数创建类实例
     *
     * @param clazz
     * @return
     */
    public static Object newInstance(final Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static Method getAnnotationMethod(final Class<?> clazz, final Class<? extends Annotation> annotationClass) {
        final Method[] methods = clazz.getMethods();
        if (methods.length == 0) {
            return null;
        }

        for (Method method : methods) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                return method;
            }
        }

        return null;
    }
}
