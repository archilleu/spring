package com.lr.ioc.beans.factory;

import com.lr.ioc.annotation.Order;
import com.lr.ioc.annotation.Primary;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.constant.ScopeConst;
import com.lr.ioc.constant.enums.BeanSourceType;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.lifecycle.create.DefaultNewInstanceBean;
import com.lr.ioc.support.lifecycle.destroy.DefaultPreDestroyBean;
import com.lr.ioc.support.lifecycle.destroy.DisposableBean;
import com.lr.ioc.support.lifecycle.init.DefaultPostConstructBean;
import com.lr.ioc.support.lifecycle.init.InitializingBean;
import com.lr.ioc.support.processor.BeanPostProcessor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AbstractBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private final List<String> beanDefinitionNames = new ArrayList<>();

    private final Map<Class, Set<String>> typeBeanNameMap = new ConcurrentHashMap<>();

    @Getter
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public Object getBean(final String name) throws IocRuntimeException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) {
            throw new IocRuntimeException(("No bean named " + name + " is defined"));
        }

        Object bean;
        final String scope = beanDefinition.getScope();
        if (ScopeConst.PROTOTYPE.equals(scope)) {
            bean = doCreateBean(beanDefinition);
        } else {
            bean = beanDefinition.getBean();
            if (bean == null) {
                bean = doCreateBean(beanDefinition);
                beanDefinition.setBean(bean);
            }
        }

        return bean;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(final String name, final Class<T> clazz) throws IocRuntimeException {
        return (T) getBean(name);
    }

    @Override
    public <T> List<T> getBeans(final Class<T> type) {
        List<T> list = new LinkedList<>();
        Set<String> names = getBeanNames(type);
        if (null == names || names.isEmpty()) {
            return list;
        }

        names.forEach((name) -> list.add(getBean(name, type)));
        //??????
        list.sort((Object left, Object right) -> {
            int orderLeft = 0;
            int orderRight = 0;
            Class<?> classLeft = left.getClass();
            Class<?> classRight = right.getClass();
            if (classLeft.isAnnotationPresent(Order.class)) {
                orderLeft = classLeft.getAnnotation(Order.class).value();
            }
            if (classRight.isAnnotationPresent(Order.class)) {
                orderRight = classRight.getAnnotation(Order.class).value();
            }

            // ??????
            return (orderRight - orderLeft);
        });

        return list;
    }

    @Override
    public <T> T getTypeBean(Class<T> requiredType, String name) {
        Set<String> beanNames = getBeanNames(requiredType);
        if (null == beanNames || beanNames.isEmpty()) {
            throw new IocRuntimeException("required type of " + requiredType.getName() + " beans not found");
        }

        if (beanNames.size() == 1) {
            name = beanNames.iterator().next();
            return getBean(name, requiredType);
        }

        if (StringUtils.isNotEmpty(name)) {
            return getBean(name, requiredType);
        }

        T primary = getPrimaryBean(requiredType, beanNames);
        if (null != primary) {
            return primary;
        }

        throw new IocRuntimeException("required type of " + requiredType.getName() + " not unique!");
    }

    @Override
    public boolean containsBean(final String name) {
        return beanDefinitionMap.keySet().contains(name);
    }

    @Override
    public boolean isTypeMatch(final String name, final Class<?> type) {
        Class<?> beanType = getType(name);
        return beanType.equals(type);
    }

    @Override
    public Class<?> getType(final String name) {
        Object bean = getBean(name);
        return bean.getClass();
    }


    protected Object doCreateBean(BeanDefinition beanDefinition) {
        Object bean = createBeanInstance(beanDefinition);

        // ????????????????????????
        InitializingBean initializingBean = new DefaultPostConstructBean(bean, beanDefinition);
        initializingBean.initialize();
        return bean;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        return DefaultNewInstanceBean.getInstance().newInstance(this, beanDefinition);
    }

    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        // ??????bean's id??????????????????
        if (null != beanDefinitionMap.put(beanDefinition.getId(), beanDefinition)) {
            throw new IocRuntimeException(beanDefinition.getId() + " not unique");
        }

        // ??????beans?????????
        beanDefinitionNames.add(beanDefinition.getId());

        // ??????bean???????????????bean????????????
        registerTypeBeanNames(beanDefinition.getId(), beanDefinition);

        return;
    }

    // ?????????bean
    public void preInstantiateSingletons() {
        beanDefinitionMap.forEach((name, beanDefinition) -> {
            if (beanDefinition.isLazyInit()) {
                return;
            }
            getBean(name);
        });
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public void destroy() {
        beanDefinitionMap.forEach((name, beanDefinition) -> {
            DisposableBean disposableBean = new DefaultPreDestroyBean(beanDefinition);
            disposableBean.destroy();
        });
    }

    private void registerTypeBeanNames(final String beanName, final BeanDefinition beanDefinition) {
        final Set<Class<?>> typeSet = getTypeSet(beanDefinition);
        typeSet.forEach((clazz) -> {
            Set<String> beanNameSet = typeBeanNameMap.get(clazz);
            if (null == beanNameSet) {
                beanNameSet = new HashSet<>();
            }
            beanNameSet.add(beanName);
            typeBeanNameMap.put(clazz, beanNameSet);
        });
    }

    private Set<Class<?>> getTypeSet(final BeanDefinition beanDefinition) {
        Set<Class<?>> classSet = new HashSet<>();
        Class<?> clazz = beanDefinition.getBeanClass();
        classSet.add(clazz);
        Class<?>[] classes = clazz.getInterfaces();
        if (classes.length != 0) {
            classSet.addAll(Arrays.asList(classes));
        }

        return classSet;
    }

    private <T> T getPrimaryBean(Class<T> clazz, Set<String> beanNames) {
        List<T> list = getBeans(clazz);
        if (list.isEmpty()) {
            return null;
        }

        // 1.???????????????????????????@Primary
        for (T bean : list) {
            if (bean.getClass().isAnnotationPresent(Primary.class)) {
                return bean;
            }
        }

        // 2.??????@Bean??????????????????@Primary
        for (String name : beanNames) {
            // 2.1??????bean??????
            BeanDefinition beanDefinition = beanDefinitionMap.get(name);
            if (!BeanSourceType.isConfigurationBean(beanDefinition.getSourceType())) {
                continue;
            }

            // 2.2??????bean??????@Primary
            if (beanDefinition.isPrimary()) {
                return (T) getBean(name);
            }
        }

        return null;
    }

    private Set<String> getBeanNames(final Class<?> type) {
        return typeBeanNameMap.get(type);
    }
}
