package com.lr.ioc.support.annotation;

import com.lr.ioc.annotation.Scope;
import com.lr.ioc.constant.ScopeConst;

import java.lang.reflect.Method;

public final class Scopes {

    public static String getScope(final Method method) {
        if (method.isAnnotationPresent(Scope.class)) {
            Scope scope = method.getAnnotation(Scope.class);
            return scope.value();
        }

        return ScopeConst.SINGLETON;
    }

    public static String getScope(final Class<?> clazz) {
        if (clazz.isAnnotationPresent(Scope.class)) {
            Scope scope = clazz.getAnnotation(Scope.class);
            return scope.value();
        }

        return ScopeConst.SINGLETON;
    }

}
