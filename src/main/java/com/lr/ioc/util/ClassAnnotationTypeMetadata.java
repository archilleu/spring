package com.lr.ioc.util;

import com.lr.ioc.annotation.Component;
import com.lr.ioc.annotation.Controller;
import com.lr.ioc.annotation.Repository;
import com.lr.ioc.annotation.Service;
import com.lr.ioc.exception.IocRuntimeException;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public Annotation getComponentAnnotation(Class<? extends Annotation> annotationClass) {
        for (Annotation annotation : annotations) {
            //1.直接注解包好目标注解
            if (annotation.annotationType().equals(annotationClass)) {
                return annotation;
            }

            //2.直接注解类型被目标注解类型注释
//            for (Annotation annotationsRefs: annotation.annotationType().getAnnotations()) {
//                for (Annotation item : annotationsRefs) {
//                    // 当前注解类型是目标注解
//                    if (item.annotationType().equals(annotationClass)) {
//                    }
//
//                    // 当前注解类型被当前注解注解(@Service->@Component->@Indexed)
//                    if (item.annotationType().isAnnotationPresent(annotationClass)) {
//                    }
//                }
//            }

            if (annotation.annotationType().equals(Component.class)) {
                return annotation;
            } else if (annotation.annotationType().equals(Controller.class)) {
                return annotation;
            } else if (annotation.annotationType().equals(Service.class)) {
                return annotation;
            } else if (annotation.annotationType().equals(Repository.class)) {
                return annotation;
            } else {
                throw new IocRuntimeException("get component annotation failed");
            }
        }

        return null;
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