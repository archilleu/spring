package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Indexed
public @interface Component {

    /**
     * 组件名称
     *
     * @return 组件名称
     */
    String value() default "";

}
