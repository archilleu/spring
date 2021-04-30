package com.lr.ioc.aop;

import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 代理相关的元数据
 */

@Data
public class AdvisedSupport {

    private TargetSource targetSource;

    private MethodInterceptor methodInterceptor;

}
