package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解@Before代理
 */

import org.aopalliance.intercept.MethodInvocation;

public class AspectJBeforeAdvice extends AbstractAspectJAdvice {

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        JoinPoint joinPoint = new JoinPoint();
        Object instance = beanFactory.getBean(aspectInstanceName);
        aspectJAdviceMethod.invoke(instance, joinPoint);
        return methodInvocation.proceed();
    }
}
