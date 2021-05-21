package com.lr.ioc.support.annotation;

import com.lr.ioc.annotation.Lazy;

import java.lang.reflect.Method;

public final class Lazes {

    public static boolean getLazy(final Method method) {
        if (method.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = method.getAnnotation(Lazy.class);
            return lazy.value();
        }

        return false;
    }

    public static boolean getLazy(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Lazy.class)) {
            Lazy lazy = clazz.getAnnotation(Lazy.class);
            return lazy.value();
        }

        return false;
    }
}
