package com.lr.ioc.support.scanner;

import com.lr.ioc.support.name.BeanNameStrategy;

import java.lang.annotation.Annotation;
import java.util.List;

public interface BeanDefinitionScannerContext {

    List<String> getScanPackages();

    List<Class<? extends Annotation>> getExcludes();

    List<Class<? extends Annotation>> getIncludes();

    Class<? extends BeanNameStrategy> getBeanNameStrategy();

}
