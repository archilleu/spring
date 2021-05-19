package com.lr.ioc.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Import {

    /**
     * 导入配置类
     *
     * @return
     */
    Class[] value();
   
}
