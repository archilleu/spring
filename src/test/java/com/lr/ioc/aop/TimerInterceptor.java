package com.lr.ioc.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TimerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long time = System.nanoTime();
        System.out.println("Invocation ot method " + methodInvocation.getMethod() + " start!");
        Object proceed = methodInvocation.proceed();
        System.out.println("Invocation ot method " + methodInvocation.getMethod() + " end! takes "
        + (System.nanoTime() - time) + " nanoseconds");

        return proceed;
    }

}
