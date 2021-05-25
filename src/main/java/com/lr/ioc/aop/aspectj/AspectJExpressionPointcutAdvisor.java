package com.lr.ioc.aop.aspectj;

/**
 * 切面类
 * Advice为切面织入方法（MethodInterceptor扩展{@link Adivce}类）
 */

import com.lr.ioc.aop.pointcut.Pointcut;
import org.aopalliance.aop.Advice;

public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

    /**
     * AOP表达式
     */
    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

    /**
     * AOP方法
     */
    private Advice advice;

    public void setExpression(String expression) {
        pointcut.setExpression(expression);
    }

    public void setAdvice(Advice advice) {
        this.advice = advice;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }
}
