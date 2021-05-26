package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解@Around代理
 */

import org.aopalliance.intercept.MethodInvocation;

public class AspectJAroundAdvice extends AbstractAspectJAdvice {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(methodInvocation);
        Object instance = beanFactory.getBean(aspectInstanceName);
        return aspectJAdviceMethod.invoke(instance, proceedingJoinPoint);
    }
}
