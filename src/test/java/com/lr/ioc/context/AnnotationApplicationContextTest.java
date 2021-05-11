package com.lr.ioc.context;

import com.lr.ioc.beans.AppConfig;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppConfig.class);
        AppConfig appConfig = applicationContext.getBean("appConfig", AppConfig.class);

        Assert.assertEquals("app config", appConfig.name());
    }

}
