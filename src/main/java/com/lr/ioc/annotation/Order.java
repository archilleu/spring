package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface Order {

    /**
     * 组件名称
     *
     * @return 组件名称
     */
    int value() default 0;

}
