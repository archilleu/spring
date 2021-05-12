package com.lr.ioc.support.lifecycle.init;

/**
 * 方法使用注解{@PostConstruct}
 */
public interface InitializingBean {

    /**
     * 对象初始化完成后调用
     */
    void initialize();

}
