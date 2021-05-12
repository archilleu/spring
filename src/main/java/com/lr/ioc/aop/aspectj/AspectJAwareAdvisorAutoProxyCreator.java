package com.lr.ioc.aop.aspectj;

import com.lr.ioc.aop.proxy.ProxyFactory;
import com.lr.ioc.aop.proxy.TargetSource;
import com.lr.ioc.beans.BeanPostProcessor;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

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
        if (bean instanceof AspectJExpressionPointcutAdvisor) {
            return bean;
        }

        if (bean instanceof MethodInterceptor) {
            return bean;
        }

        // 以上的类型都放过，只对普通的类进行代理

        // 获取所有切面定义
        List<AspectJExpressionPointcutAdvisor> advisors =
                beanFactory.getBeans(AspectJExpressionPointcutAdvisor.class);
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 找到第一个符合切面的对象
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
