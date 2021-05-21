package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Repository {

    /**
     * 存储类注解
     *
     * @return
     */
    String value() default "";

}
