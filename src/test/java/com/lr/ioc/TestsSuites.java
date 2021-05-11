package com.lr.ioc;

import com.lr.ioc.aop.aspectj.AspectJExpressionPointcutTest;
import com.lr.ioc.aop.proxy.Cglib2AopProxyTest;
import com.lr.ioc.aop.proxy.JdkDynamicAopProxyTest;
import com.lr.ioc.beans.factory.BeanFactoryTest;
import com.lr.ioc.beans.json.JsonBeanDefinitionReaderTest;
import com.lr.ioc.context.ApplicationContextTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ApplicationContextTest.class, AspectJExpressionPointcutTest.class
        , Cglib2AopProxyTest.class, JdkDynamicAopProxyTest.class, BeanFactoryTest.class
        , JsonBeanDefinitionReaderTest.class})
public class TestsSuites {
}
