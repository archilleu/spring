package com.lr.ioc.support.lifecycle.init;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultPostConstructBean implements InitializingBean {

    private final BeanDefinition beanDefinition;

    private final Object instance;

    public DefaultPostConstructBean(Object instance, BeanDefinition beanDefinition) {
        this.instance = instance;
        this.beanDefinition = beanDefinition;
    }

    @Override
    public void initialize() {
        postConstruct();

        initializingBean();

        customInit();
    }

    /**
     * 处理{@PostConstruct}注解
     */
    private void postConstruct() {
        Method method = ClassUtils.getAnnotationMethod(instance.getClass(), PostConstruct.class);
        if (null == method) {
            return;
        }

        // 1.信息校验
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 0) {
            throw new IocRuntimeException("@PostConstruct must be has no params.");
        }

        // 2.调用
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    /**
     * 处理接口类型的bean
     */
    private void initializingBean() {
        if (instance instanceof InitializingBean) {
            ((InitializingBean) instance).initialize();
        }
    }

    /**
     * 自定义初始化函数
     */
    private void customInit() {
        String init = beanDefinition.getInitialize();
        if (StringUtils.isEmpty(init)) {
            return;
        }

        try {
            Method method = instance.getClass().getMethod(init);
            method.invoke(instance);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

}
