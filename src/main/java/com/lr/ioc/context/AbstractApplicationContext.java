package com.lr.ioc.context;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.processor.BeanPostProcessor;

import java.util.List;

public abstract class AbstractApplicationContext implements ApplicationContext {

    protected AbstractBeanFactory beanFactory;

    public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void refresh() {
        loadBeanDefinitions(beanFactory);

        /**
         * BeanPostProcessor先于普通bean创建，在此之前registerBeanPostProcessors大小是0
         */
        registerBeanPostProcessors(beanFactory);

        onRefresh();
    }

    protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory);

    protected void registerBeanPostProcessors(AbstractBeanFactory beanFactory) {
        List<BeanPostProcessor> beanPostProcessors = beanFactory.getBeans(BeanPostProcessor.class);
        for (Object beanPostProcessor : beanPostProcessors) {
            beanFactory.addBeanPostProcessor((BeanPostProcessor) beanPostProcessor);
        }
    }

    protected void onRefresh() {
        beanFactory.preInstantiateSingletons();
    }

    protected void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
//                AbstractApplicationContext.this.destroy();
            }
        });
    }

    @Override
    public Object getBean(String name) throws IocRuntimeException {
        return this.beanFactory.getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> clazz) throws IocRuntimeException {
        return this.beanFactory.getBean(name, clazz);
    }

    @Override
    public <T> List<T> getBeans(final Class<T> type) {
        return this.beanFactory.getBeans(type);
    }

    @Override
    public <T> T getTypeBean(Class<T> requiredType, String name) {
        return this.beanFactory.getTypeBean(requiredType, name);
    }

    @Override
    public boolean containsBean(final String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public boolean isTypeMatch(final String name, final Class type) {
        return this.beanFactory.isTypeMatch(name, type);
    }

    @Override
    public Class<?> getType(final String name) {
        return this.beanFactory.getType(name);
    }

}
