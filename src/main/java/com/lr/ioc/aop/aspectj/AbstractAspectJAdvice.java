package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解代理基类
 */

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Data
public class AbstractAspectJAdvice implements Advice, MethodInterceptor {

    protected BeanFactory beanFactory;

    protected Method aspectJAdviceMethod;

    protected String aspectInstanceName;

    protected Method aspectJAdviceMethodAround;
    protected Method aspectJAdviceMethodBefore;
    protected Method aspectJAdviceMethodAfter;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object object = null;
        boolean aopMethodIsInvoked = false;

        // 1.优先执行around
        if (null != aspectJAdviceMethodAround) {
            ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(methodInvocation);
            Object instance = beanFactory.getBean(aspectInstanceName);
            object = aspectJAdviceMethodAround.invoke(instance, proceedingJoinPoint);
            aopMethodIsInvoked = true;
        }

        // 2.执行before
        if (null != aspectJAdviceMethodBefore) {
            JoinPoint joinPoint = new JoinPoint();
            Object instance = beanFactory.getBean(aspectInstanceName);
            aspectJAdviceMethodBefore.invoke(instance, joinPoint);
            if (!aopMethodIsInvoked) {
                object = methodInvocation.proceed();
                aopMethodIsInvoked = true;
            }
        }

        // 3.执行after
        if (null != aspectJAdviceMethodAfter) {
            if (!aopMethodIsInvoked) {
                object = methodInvocation.proceed();
            }
            JoinPoint joinPoint = new JoinPoint();
            Object instance = beanFactory.getBean(aspectInstanceName);
            aspectJAdviceMethodAfter.invoke(instance, joinPoint);
        }

        return object;
    }

}
