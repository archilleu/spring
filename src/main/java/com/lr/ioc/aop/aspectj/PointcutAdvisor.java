package com.lr.ioc.aop.aspectj;

import com.lr.ioc.aop.pointcut.Pointcut;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
