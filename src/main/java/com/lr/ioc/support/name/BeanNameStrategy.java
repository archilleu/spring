package com.lr.ioc.support.name;

import com.lr.ioc.beans.BeanDefinition;

/**
 * Bean 自动命名策略接口
 */

public interface BeanNameStrategy {

    /**
     * 生成对象名称
     *
     * @param beanDefinition
     * @return 生成的对象名称
     */
    String generateBeanName(BeanDefinition beanDefinition);

}
