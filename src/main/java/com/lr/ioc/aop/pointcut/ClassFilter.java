package com.lr.ioc.aop.pointcut;

public interface ClassFilter {

    boolean matches(Class targetClass);

}
