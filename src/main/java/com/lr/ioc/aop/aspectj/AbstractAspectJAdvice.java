package com.lr.ioc.aop.aspectj;

/**
 * @Aspect注解@Around代理基类
 */

import com.lr.ioc.beans.factory.BeanFactory;
import lombok.Data;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

@Data
public abstract class AbstractAspectJAdvice implements Advice, MethodInterceptor {

    protected BeanFactory beanFactory;

    protected Method aspectJAdviceMethod;

    protected String aspectInstanceName;

    @Override
    public abstract Object invoke(MethodInvocation methodInvocation) throws Throwable;
}
