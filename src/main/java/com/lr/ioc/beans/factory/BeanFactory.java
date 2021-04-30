package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;

/**
 * bean容器接口
 */

public interface BeanFactory {

    Object getBean(String name) throws Exception;

}
