package com.lr.ioc.util;

import com.lr.ioc.annotation.Controller;
import com.lr.ioc.annotation.Service;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Controller
@Service
public @interface TestAnnotation {

    String value() default "";

}
