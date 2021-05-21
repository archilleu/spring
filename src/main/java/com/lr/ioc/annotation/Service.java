package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Component
public @interface Service {

    /**
     * 服务类名称
     *
     * @return
     */
    String value() default "";

}
