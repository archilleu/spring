package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Autowired {

    /**
     * 指定依赖名称
     *
     * @return
     */
    String value() default "";

}
