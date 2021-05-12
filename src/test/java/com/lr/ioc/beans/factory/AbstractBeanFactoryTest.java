package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.constant.enums.ScopeEnum;
import com.lr.ioc.exception.IocRuntimeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AbstractBeanFactoryTest {

    @Before
    public void init() throws Exception {
        beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName("com.lr.ioc.beans.factory.Apple");

        beanFactory = new AbstractBeanFactory() {
            @Override
            protected void applyPropertyValues(Object bean, BeanDefinition mbd) {
                super.applyPropertyValues(bean, mbd);
            }
        };
        beanFactory.registerBeanDefinition(BEAN_NAME, beanDefinition);

        beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName("com.lr.ioc.beans.factory.Apple");
        beanDefinition.setLazyInit(true);
        beanDefinition.setScope(ScopeEnum.PROTOTYPE.getCode());
        beanFactory.registerBeanDefinition(BEAN_NAME_PROTOTYPE, beanDefinition);
    }

    @Test
    public void testGetBean() throws Exception {
        Apple apple = (Apple) beanFactory.getBean(BEAN_NAME);
        Assert.assertEquals(apple.toString(), apple.toString());

        apple = beanFactory.getBean(BEAN_NAME, Apple.class);
        Assert.assertEquals(apple.toString(), apple.toString());

        try {
            Object o = beanFactory.getBean(BEAN_NAME_INVALID);
            Assert.assertNotNull(o);
        } catch (IocRuntimeException e) {
        }
    }

    @Test
    public void testGetBeans() throws Exception {
        List<Apple> list = beanFactory.getBeans(Apple.class);
        Assert.assertTrue(list.size() > 0);

        List<AbstractBeanFactoryTest> list1 = beanFactory.getBeans(AbstractBeanFactoryTest.class);
        Assert.assertTrue(list1.isEmpty());
    }

    @Test
    public void testContainsBean() throws Exception {
        Assert.assertTrue(beanFactory.containsBean(BEAN_NAME));
        Assert.assertFalse(beanFactory.containsBean(BEAN_NAME_INVALID));
    }

    @Test
    public void testIsTypeMatch() throws Exception {
        Assert.assertTrue(beanFactory.isTypeMatch(BEAN_NAME, Apple.class));
        Assert.assertFalse(beanFactory.isTypeMatch(BEAN_NAME, AbstractBeanFactoryTest.class));
    }

    @Test
    public void testGetType() throws Exception {
        Class<?> clazz = beanFactory.getType(BEAN_NAME);
        try {
            Class<?> invalid_clazz = beanFactory.getType(BEAN_NAME_INVALID);
            Assert.assertTrue(true);
        } catch (IocRuntimeException e) {
        }
    }

    @Test
    public void testSingleton() throws Exception {
        Apple singleton1 = beanFactory.getBean(BEAN_NAME, Apple.class);
        Apple singleton2 = beanFactory.getBean(BEAN_NAME, Apple.class);
        Assert.assertEquals(singleton1, singleton2);
    }

    @Test
    public void testPrototype() throws Exception {
        Apple apple1 = beanFactory.getBean(BEAN_NAME_PROTOTYPE, Apple.class);
        Apple apple2 = beanFactory.getBean(BEAN_NAME_PROTOTYPE, Apple.class);
        Assert.assertNotEquals(apple1, apple2);
    }

    private BeanDefinition beanDefinition;
    private AbstractBeanFactory beanFactory;

    final private static String BEAN_NAME = "apple";
    final private static String BEAN_NAME_PROTOTYPE = "apple1";
    final private static String BEAN_NAME_INVALID = "invalid";
}
