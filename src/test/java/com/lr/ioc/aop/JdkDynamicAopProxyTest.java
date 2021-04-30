package com.lr.ioc.aop;

import com.lr.ioc.beans.BeanController;
import com.lr.ioc.context.ApplicationContext;
import com.lr.ioc.context.ClassPathJsonApplicationContext;
import org.junit.Test;

public class JdkDynamicAopProxyTest {

    @Test
    public void test() throws Exception {
        // without AOP
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        BeanController beanController = (BeanController) applicationContext.getBean("beanController");
        beanController.helloWorld();

        // with AOP
        // 1.设置被代理对象(Joinpoint)
        AdvisedSupport advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(beanController, BeanController.class);
        advisedSupport.setTargetSource(targetSource);

        // 2.设置拦截器(Advice)
        TimerInterceptor timerInterceptor = new TimerInterceptor();
        advisedSupport.setMethodInterceptor(timerInterceptor);

        // 3.创建代理
        JdkDynamicAopProxy jdkDynamicAopProxy = new JdkDynamicAopProxy(advisedSupport);
        BeanController beanControllerProxy = (BeanController) jdkDynamicAopProxy.getProxy();

        // 4.基于AOP调用
        beanControllerProxy.helloWorld();
    }
}
