package com.lr.ioc.aop.aspectj;

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

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable, Exception {
        return aspectJAdviceMethod.invoke(beanFactory.getBean(aspectInstanceName), methodInvocation);
    }
}
