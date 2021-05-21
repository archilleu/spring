package com.lr.ioc.annotation;

import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ComponentScan {

    String[] value();

    Class<? extends Annotation>[] excludes() default {};

    Class<? extends Annotation>[] includes() default {Component.class};

    Class<? extends BeanNameStrategy> beanNameStrategy() default DefaultBeanNameStrategy.class;

}