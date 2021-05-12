package com.lr.ioc.support.lifecycle.destroy;

/**
 * 方法使用注解{@PreDestroy}
 */

public interface DisposableBean {

    /**
     * 对象销毁时候调用
     */
    void destroy();
}
