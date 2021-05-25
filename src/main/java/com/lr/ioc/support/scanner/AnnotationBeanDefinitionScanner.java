package com.lr.ioc.support.scanner;

import com.lr.ioc.beans.BeanDefinition;

import java.util.List;

public interface AnnotationBeanDefinitionScanner {

    /**
     * 扫面指定的包
     *
     * @param context 定义
     * @return {@link BeanDefinition}集合
     */
    List<BeanDefinition> scan(final BeanDefinitionScannerContext context);

}
