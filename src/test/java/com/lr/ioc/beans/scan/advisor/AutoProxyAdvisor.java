package com.lr.ioc.beans.scan.advisor;

import com.lr.ioc.annotation.Around;
import com.lr.ioc.annotation.Aspect;
import com.lr.ioc.annotation.Pointcut;
import com.lr.ioc.aop.aspectj.ProceedingJoinPoint;

@Aspect
public class AutoProxyAdvisor {

    @Pointcut("execution(* com.lr.ioc.beans.scan.advisor.*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long time = System.nanoTime();
        System.out.println("Invocation ot method " + proceedingJoinPoint.getMethod() + " start!");
        Object proceed = proceedingJoinPoint.proceed();
        System.out.println("Invocation ot method " + proceedingJoinPoint.getMethod() + " end! takes "
                + (System.nanoTime() - time) + " nanoseconds");
        return proceed;
    }
}
