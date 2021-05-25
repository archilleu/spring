package com.lr.ioc.aop.aspectj;

import lombok.Getter;
import org.aopalliance.intercept.MethodInvocation;

@Getter
public class ProceedingJoinPoint {

    private MethodInvocation method;

    public ProceedingJoinPoint(MethodInvocation methodInvocation) {
        this.method = methodInvocation;
    }

    public Object proceed() throws Throwable {
        return method.proceed();
    }
}
