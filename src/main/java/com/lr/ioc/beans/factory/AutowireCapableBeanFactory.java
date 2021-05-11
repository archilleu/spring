package com.lr.ioc.beans.factory;

import com.lr.ioc.aop.aspectj.BeanFactoryAware;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanReference;
import com.lr.ioc.beans.PropertyValue;
import com.lr.ioc.exception.IocRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AutowireCapableBeanFactory extends AbstractBeanFactory {

    @Override
    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        for (PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
            Object value = propertyValue.getValue();
            // 判断是否为ref
            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = getBean(beanReference.getName());
            }

            //是否定义setter方法
            try {
                String methodName = "set" + propertyValue.getName().substring(0, 1).toUpperCase() + propertyValue.getName().substring(1);
                Method declareMethod = bean.getClass().getDeclaredMethod(methodName, value.getClass());
                declareMethod.setAccessible(true);
                declareMethod.invoke(bean, value);
            } catch (NoSuchMethodException ex) {
                try {
                    Class<?> clazz = bean.getClass();
                    Field declaredField = clazz.getDeclaredField(propertyValue.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(bean, value);
                } catch (Exception e) {
                    throw new IocRuntimeException(e);
                }
            } catch (Exception e) {
                throw new IocRuntimeException(e);
            }
        }
    }
}
