package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解@Around代理
 */

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Data
public class AspectJAroundAdvice implements Advice, MethodInterceptor {

    private BeanFactory beanFactory;

    private Method aspectJAdviceMethod;

    private String aspectInstanceName;

    private Object params;

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable, Exception {
        ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(methodInvocation);
        Object instance = beanFactory.getBean(aspectInstanceName);
        Object obj = aspectJAdviceMethod.invoke(instance, proceedingJoinPoint);
        return obj;
    }
}
