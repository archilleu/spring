package com.lr.ioc.beans;

import com.lr.ioc.beans.io.ResourceLoader;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

    @Getter
    private Map<String, BeanDefinition> registry;

    @Getter
    private ResourceLoader resourceLoader;

    protected AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.registry = new HashMap<>();
    }

}
