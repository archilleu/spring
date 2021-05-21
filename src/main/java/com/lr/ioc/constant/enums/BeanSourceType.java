package com.lr.ioc.constant.enums;

/**
 * BeanDefinition来源
 */
public enum BeanSourceType {

    /**
     * 来着资源文件
     */
    RESOURCE,

    /**
     * 来着配置注解
     */
    CONFIGURATION,

    /**
     * 来自COMPONENT注解
     */
    COMPONENT,

    /**
     * 来自注解bean
     */
    CONFIGURATION_BEAN;


    public static boolean isResourceBean(final BeanSourceType beanSourceType) {
        return RESOURCE.equals(beanSourceType);
    }

    public static boolean isConfiguration(final BeanSourceType beanSourceType) {
        return CONFIGURATION.equals(beanSourceType);
    }

    public static boolean isConfigurationBean(final BeanSourceType beanSourceType) {
        return CONFIGURATION_BEAN.equals(beanSourceType);
    }
}
