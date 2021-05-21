package com.lr.ioc.support.scanner.impl;

import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.scanner.BeanDefinitionScannerContext;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.util.List;

@Setter
public class DefaultBeanDefinitionScannerContext implements BeanDefinitionScannerContext {

    private List<String> scanPackages;

    private List<Class<? extends Annotation>> excludes;

    private List<Class<? extends Annotation>> includes;

    private Class<? extends BeanNameStrategy> beanNameStrategy;
   
    @Override
    public List<String> getScanPackages() {
        return this.scanPackages;
    }

    @Override
    public List<Class<? extends Annotation>> getExcludes() {
        return this.excludes;
    }

    @Override
    public List<Class<? extends Annotation>> getIncludes() {
        return this.includes;
    }

    @Override
    public Class<? extends BeanNameStrategy> getBeanNameStrategy() {
        return this.beanNameStrategy;
    }
}
