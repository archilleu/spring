package com.lr.ioc.aop.proxy;

import com.lr.ioc.beans.BeanService;
import com.lr.ioc.beans.BeanServiceImpl;
import com.lr.ioc.context.ApplicationContext;
import com.lr.ioc.context.ClassPathJsonApplicationContext;
import org.junit.Test;

public class Cglib2AopProxyTest {

    @Test
    public void testInterceptor() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext("bean-post-processor.json");
        BeanService beanService = (BeanService) applicationContext.getBean("beanService");
        beanService.helloWorld();


        // 设置被代理对象
        AdvisedSupport advisedSupport = new AdvisedSupport();
        TargetSource targetSource = new TargetSource(beanService, BeanServiceImpl.class, BeanService.class);
        advisedSupport.setTargetSource((targetSource));

        // 设置拦截器
        TimerInterceptor timerInterceptor = new TimerInterceptor();
        advisedSupport.setMethodInterceptor(timerInterceptor);

        // TODO:没有设置MethodMatcher，所以拦截该类的所有方法
        // 创建代理对象
        Cglib2AopProxy cglib2AopProxy = new Cglib2AopProxy(advisedSupport);
        BeanService beanServiceProxy = (BeanService) cglib2AopProxy.getProxy();

        // 调用
        beanServiceProxy.helloWorld();
    }
}
