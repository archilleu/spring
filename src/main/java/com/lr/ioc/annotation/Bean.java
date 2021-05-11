package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Bean {

    String value() default "";

    String initMethod() default "";

    String destroyMethod() default "";
   
}
