package com.lr.ioc.beans.scan.advisor;

import com.lr.ioc.annotation.*;
import com.lr.ioc.aop.aspectj.JoinPoint;
import com.lr.ioc.aop.aspectj.ProceedingJoinPoint;

@Aspect
public class AutoProxyAdvisor {

    @Pointcut("execution(* com.lr.ioc.beans.scan.advisor.*.*(..))")
    public void around() {
    }

    @Pointcut("execution(* com.lr.ioc.beans.scan.advisor.*.*(..))")
    public void before() {
    }

    @Pointcut("execution(* com.lr.ioc.beans.scan.advisor.*.*(..))")
    public void after() {

    }

    @Before("before()")
    public void before(JoinPoint joinPoint) throws Throwable {
        System.out.println("before");
    }

    @After("after()")
    public void after(JoinPoint joinPoint) throws Throwable {
        System.out.println("after");
    }

    @Around("around()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long time = System.nanoTime();
        System.out.println("Invocation ot method " + proceedingJoinPoint.getMethod() + " start!");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("Invocation ot method " + proceedingJoinPoint.getMethod() + " end! takes "
                + (System.nanoTime() - time) + " nanoseconds");
        return proceed;
    }
}
