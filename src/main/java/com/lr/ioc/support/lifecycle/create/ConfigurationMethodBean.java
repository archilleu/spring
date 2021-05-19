package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.util.ClassUtils;

import java.lang.reflect.Method;

public class ConfigurationMethodBean extends AbstractNewInstanceBean {

    private static final ConfigurationMethodBean INSTANCE = new ConfigurationMethodBean();

    public static ConfigurationMethodBean getInstance() {
        return INSTANCE;
    }

    @Override
    protected Object newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class<?> beanClass) {
        if (!BeanSourceType.isConfigurationBean(beanDefinition.getSourceType())) {
            throw new IocRuntimeException("BeanDefinition not come from @Bean");
        }

        // 1.无参数
        // 1.1获取成员所属类对象
        Object configurationInstance = beanFactory.getBean(beanDefinition.getConfigurationName());
        // 1.2获取生成@Bean注解方法
        // 1.3 TODO:带参数的处理
        Class<?> clazz = configurationInstance.getClass();
        String methodName = beanDefinition.getConfigurationBeanMethod();
        Method method = ClassUtils.getMethod(clazz, methodName);
        Object instance = ClassUtils.invokeMethod(configurationInstance, method, null);
        return instance;
    }
}
