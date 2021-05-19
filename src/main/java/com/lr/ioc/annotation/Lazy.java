package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Lazy {

    /**
     * 是否延迟加载
     *
     * @return
     */
    boolean value() default false;

}
