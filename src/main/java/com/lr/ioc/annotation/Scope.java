package com.lr.ioc.annotation;

import com.lr.ioc.constant.ScopeConst;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Scope {

    /**
     * 生命周期
     *
     * @return
     */
    String value() default ScopeConst.SINGLETON;

}
