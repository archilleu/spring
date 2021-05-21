package com.lr.ioc.util;

import com.lr.ioc.exception.IocRuntimeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;

public class ClassUtils {

    /**
     * 获取类信息
     *
     * @param className
     * @return
     */
    public static Class<?> getClass(final String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException ex) {
            throw new IocRuntimeException(ex);
        }
    }

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
            return factoryMethod.invoke(null, null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static Object invokeMethod(final Object object, final Method method, Object... args) {
        try {
            if (null == args) {
                return method.invoke(object);
            } else {
                return method.invoke(object, args);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    public static void setFieldValue(String field, Object instance, Object value) {
        //是否定义setter方法
        try {
            String methodName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
            Method declareMethod = instance.getClass().getDeclaredMethod(methodName, value.getClass());
            declareMethod.setAccessible(true);
            declareMethod.invoke(instance, value);
        } catch (NoSuchMethodException ex) {
            try {
                Class<?> clazz = instance.getClass();
                Field declaredField = clazz.getDeclaredField(field);
                declaredField.setAccessible(true);
                declaredField.set(instance, value);
            } catch (Exception e) {
                throw new IocRuntimeException(e);
            }
        } catch (Exception e) {
            throw new IocRuntimeException(e);
        }
    }
}
