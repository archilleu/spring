package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解@After代理
 */

import org.aopalliance.intercept.MethodInvocation;

public class AspectJAfterAdvice extends AbstractAspectJAdvice {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object res = methodInvocation.proceed();
        JoinPoint joinPoint = new JoinPoint();
        Object instance = beanFactory.getBean(aspectInstanceName);
        aspectJAdviceMethod.invoke(instance, joinPoint);
        return res;
    }
}
