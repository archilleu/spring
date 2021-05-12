package com.lr.ioc.support.lifecycle.destroy;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.util.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PreDestroy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DefaultPreDestroyBean implements DisposableBean {

    private final BeanDefinition beanDefinition;

    private final Object instance;

    public DefaultPreDestroyBean(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
        this.instance = beanDefinition.getBean();
    }

    @Override
    public void destroy() {
        preDestroy();

        disposableBean();

        customDestroy();
    }

    /**
     * 处理{@PreDestroy}注解
     */
    private void preDestroy() {
        Method method = ClassUtils.getAnnotationMethod(instance.getClass(), PreDestroy.class);
        if (null == method) {
            return;
        }

        // 1.信息校验
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length != 0) {
            throw new IocRuntimeException("@PreDestroy must be has no params.");
        }

        // 2.调用
        try {
            method.invoke(instance);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }

    private void disposableBean() {
        if (instance instanceof DisposableBean) {
            ((DisposableBean) instance).destroy();
        }
    }

    /**
     * 自定义销毁函数
     */
    private void customDestroy() {
        String destroy = beanDefinition.getDestroy();
        if (StringUtils.isEmpty(destroy)) {
            return;
        }

        try {
            Method method = instance.getClass().getMethod(destroy);
            method.invoke(instance);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new IocRuntimeException(e);
        }
    }
}
