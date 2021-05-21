package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Controller {

    /**
     * 控制层类注解
     *
     * @return
     */
    String value() default "";

}
