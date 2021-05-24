package com.lr.ioc.util;

import com.lr.ioc.annotation.Component;
import com.lr.ioc.exception.IocRuntimeException;

import java.lang.annotation.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.*;

public class ClassAnnotationTypeMetadata {

    private Class<?> clazz;

    private Annotation[] annotations;

    public ClassAnnotationTypeMetadata(Class<?> clazz) {
        this.clazz = clazz;
        this.annotations = clazz.getAnnotations();
    }

    public boolean isAnnotated(Class<? extends Annotation> annotationClass) {
        return this.clazz.isAnnotationPresent(annotationClass);
    }


    public Class<? extends Annotation> isAnnotatedOrMeta(List<Class<? extends Annotation>> classList) {
        for (Class<? extends Annotation> clazz : classList) {
            if (true == isAnnotatedOrMeta(clazz)) {
                return clazz;
            }
        }

        return null;
    }

    /**
     * 是否被注解或者是注解的元注解
     *
     * @param annotationClass
     * @return
     */
    public boolean isAnnotatedOrMeta(Class<? extends Annotation> annotationClass) {
        //1.直接注解
        if (isAnnotated(annotationClass)) {
            return true;
        }

        //2.当前注解类型被当前注解注解(@Service被@Compoment注解)
        if (!getAnnotationMeta(annotationClass).isEmpty()) {
            return true;
        }

        return false;
    }

    /**
     * 获取类包含目标注解的元注解
     *
     * @param annotationClass 目标注解类
     * @return
     */
    public Set<Annotation> getAnnotationMeta(final Class<? extends Annotation> annotationClass) {
        Set<Annotation> annotationSet = new HashSet<>();

        for (Annotation annotation : annotations) {
            if (true == checkHasMetaAnnotation(annotation, annotationClass)) {
                annotationSet.add(annotation);
            }
        }

        return annotationSet;
    }

    /**
     * 获取类被标注的注解
     *
     * @param annotationClass
     * @return
     */
    public Object getDirectComponentAnnotationName(Class<? extends Annotation> annotationClass, String filed) {
        Annotation annotation = null;
        for (Annotation tmp : annotations) {
            //1.直接注解包好目标注解
            if (tmp.annotationType().equals(annotationClass)) {
                annotation = tmp;
                break;
            }
        }
        if (null == annotation) {
            Set<Annotation> annotationSet = getAnnotationMeta(Component.class);
            if (annotationSet.isEmpty()) {
                return null;
            }
            annotation = annotationSet.iterator().next();
        }

        InvocationHandler h = Proxy.getInvocationHandler(annotation);
        try {
            Field field = h.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);
            Map<String, Object> values = (Map<String, Object>) field.get(h);
            return values.get(filed);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IocRuntimeException(e);
        }
    }

    private boolean checkHasMetaAnnotation(final Annotation annotation, final Class<? extends Annotation> annotationClass) {
        if (annotation.annotationType().equals(annotationClass)) {
            return true;
        }

        Annotation[] annotations = buildInMetaAnnotation(annotation.annotationType().getAnnotations());
        for (Annotation item : annotations) {
            if (item.annotationType().equals(annotationClass)) {
                return true;
            } else {
                if (true == checkHasMetaAnnotation(item, annotationClass)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Annotation[] buildInMetaAnnotation(Annotation[] annotations) {
        return Arrays.stream(annotations).filter((annotation) -> {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.equals(Documented.class)
                    || type.equals(Retention.class)
                    || type.equals(Target.class)
                    || type.equals(Inherited.class)
            ) {
                return false;
            }
            return true;
        }).toArray(Annotation[]::new);
    }
}