package com.lr.ioc.aop.aspectj;

import com.lr.ioc.beans.BeanService;
import com.lr.ioc.beans.BeanServiceImpl;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class AspectJExpressionPointcutTest {

    @Test
    public void testClassFilter() throws Exception {
        String expression = "execution(* com.lr.ioc.*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.getClassFilter().matches(BeanService.class);
        Assert.assertTrue(matches);
    }

    @Test
    public void testMethodInterceptor() throws Exception {
        String expression = "execution(* com.lr.ioc.beans.*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        Method method = BeanServiceImpl.class.getDeclaredMethod("helloWorld");
        boolean matches = aspectJExpressionPointcut.getMethodMatcher().matches(method, BeanServiceImpl.class);
        Assert.assertTrue(matches);
    }
}
