package com.lr.ioc.aop.pointcut;

import java.lang.reflect.Method;

public interface MethodMatcher {

    boolean matches(Method method, Class targetClass);

}
