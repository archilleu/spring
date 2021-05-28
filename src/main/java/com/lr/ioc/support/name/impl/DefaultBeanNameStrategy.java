package com.lr.ioc.support.name.impl;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.support.name.BeanNameStrategy;
import org.apache.commons.lang3.StringUtils;

public class DefaultBeanNameStrategy implements BeanNameStrategy {

    /**
     * 1.默认直接类名首字母小写
     * 2.如果已经指定{@link BeanDefinition#getId()},则直接返回。
     *
     * @param beanDefinition
     * @return
     */
    @Override
    public String generateBeanName(BeanDefinition beanDefinition) {
        String id = beanDefinition.getId();
        if (StringUtils.isNoneEmpty(id)) {
            return id;
        }

        String className = beanDefinition.getBeanClassName()
                .substring(beanDefinition.getBeanClassName().lastIndexOf(".") + 1);
        id = Character.toLowerCase(className.charAt(0)) + className.substring(1);
        return id;
    }

    @Override
    public String generateBeanNameByClass(Class<?> clazz) {
        String className = clazz.getSimpleName()
                .substring(clazz.getSimpleName().lastIndexOf(".") + 1);
        return Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }

}
