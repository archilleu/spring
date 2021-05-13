package com.lr.ioc.support.lifecycle.create;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.ConstructorArgDefinition;
import com.lr.ioc.beans.factory.BeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.util.ClassUtils;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.util.List;

public class ConstructorNewInstanceBean extends AbstractNewInstanceBean {

    private static final ConstructorNewInstanceBean INSTANCE = new ConstructorNewInstanceBean();

    public static ConstructorNewInstanceBean getInstance() {
        return INSTANCE;
    }

    @Override
    protected Object newInstanceOpt(BeanFactory beanFactory, BeanDefinition beanDefinition, Class<?> beanClass) {
        List<ConstructorArgDefinition> argDefinitions = beanDefinition.getConstructorArgList();
        // 1.无参构造
        if (argDefinitions == null || argDefinitions.isEmpty()) {
            return newInstanceOpt(beanClass);
        }

        // 2.有参构造
        return newInstanceOpt(beanFactory, beanClass, argDefinitions);
    }

    /**
     * 无参构造函数
     *
     * @param clazz
     * @return
     */
    private Object newInstanceOpt(final Class<?> clazz) {
        return ClassUtils.newInstance(clazz);
    }

    private Object newInstanceOpt(final BeanFactory beanFactory,
                                  final Class<?> clazz,
                                  final List<ConstructorArgDefinition> argDefinitions) {
        Pair<Class[], Object[]> pair = getParamsPair(beanFactory, argDefinitions);
        Constructor<?> constructor = ClassUtils.getConstructor(clazz, pair.getKey());
        Object instance = ClassUtils.newInstance(constructor, pair.getValue());
        return instance;
    }

    private Pair<Class[], Object[]> getParamsPair(final BeanFactory beanFactory,
                                                  final List<ConstructorArgDefinition> argDefinitions) {
        int size = argDefinitions.size();
        Class[] types = new Class[size];
        Object[] values = new Object[size];

        for (int i = 0; i < size; i++) {
            ConstructorArgDefinition argDefinition = argDefinitions.get(i);
            final String ref = argDefinition.getRef();
            // 引用类型
            if (StringUtils.isNotEmpty(ref)) {
                Class<?> refType = beanFactory.getType(ref);
                Object refValue = beanFactory.getBean(ref);

                types[i] = refType;
                values[i] = refValue;
            } else {
                // 指定类型值
                try {
                    Class<?> valueType = this.getClass().getClassLoader().loadClass(argDefinition.getType());
                    String valueStr = argDefinition.getValue();

                    types[i] = valueType;
                    values[i] = valueStr;
                } catch (ClassNotFoundException e) {
                    throw new IocRuntimeException(e);
                }
            }
        }

        return new Pair<>(types, values);
    }
}
