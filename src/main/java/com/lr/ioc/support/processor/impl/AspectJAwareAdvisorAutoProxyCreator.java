package com.lr.ioc.support.processor.impl;

/**
 * 自动创建代理类
 */

import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.lr.ioc.aop.proxy.ProxyFactory;
import com.lr.ioc.aop.proxy.TargetSource;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.aware.BeanFactoryAware;
import com.lr.ioc.support.processor.BeanPostProcessor;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

@Configuration
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private AbstractBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws IocRuntimeException {
        this.beanFactory = (AbstractBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws IocRuntimeException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws IocRuntimeException {
        // 切点通知器不需要代理
        if (bean instanceof AspectJExpressionPointcutAdvisor) {
            return bean;
        }

        // 方法拦截器不需要代理
        if (bean instanceof MethodInterceptor) {
            return bean;
        }

        // 以上的类型都放过，只对普通的类进行代理

        /**
         *
         * {@linke BeanFactory#getBeans} 保证了 PointcutAdvisor 的实例化顺序优于普通 Bean。
         * AspectJ方式实现织入,这里它会扫描所有Pointcut，并对bean做织入
         */
        // 获取所有切面定义
        List<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeans(AspectJExpressionPointcutAdvisor.class);
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 匹配当前的bean是否是要拦截的类
            if (advisor.getPointcut().getClassFilter().matches(bean.getClass())) {
                ProxyFactory advisedSupport = new ProxyFactory();
                // 切点方法拦截器
                advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
                // 方法AspectJ表达式
                advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

                TargetSource targetSource = new TargetSource(bean, bean.getClass(), bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);

                return advisedSupport.getProxy();
            }
        }

        return bean;
    }
}
