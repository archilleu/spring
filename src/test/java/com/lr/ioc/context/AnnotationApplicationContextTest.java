package com.lr.ioc.context;

import com.lr.ioc.beans.*;
import com.lr.ioc.beans.factory.ColorTomato;
import com.lr.ioc.beans.factory.Tomato;
import com.lr.ioc.beans.scan.ScanApplication;
import com.lr.ioc.beans.scan.controller.IndexController;
import org.junit.Assert;
import org.junit.Test;

public class AnnotationApplicationContextTest {

    @Test
    public void testConfiguration() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppConfig.class);
        AppConfig appConfig = applicationContext.getBean("appConfig", AppConfig.class);

        Assert.assertEquals("app config", appConfig.name());
    }

    @Test
    public void testBeanConfiguration() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppBeanConfig.class);
        AppConfig appConfig = applicationContext.getBean("appConfig", AppConfig.class);

        Assert.assertEquals("app config", appConfig.name());
    }

    @Test
    public void configBeanLazyScopeConfigTest() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AnnoLazyScopeConfig.class);
        ColorTomato colorTomato = applicationContext.getBean("colorTomato", ColorTomato.class);
        Assert.assertNotNull(colorTomato);

        Tomato tomato1 = applicationContext.getBean("tomato", Tomato.class);
        Tomato tomato2 = applicationContext.getBean("tomato", Tomato.class);
        Assert.assertTrue(tomato1 != tomato2);
    }

    @Test
    public void importTest() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppBeanConfigImport.class);
        AppConfig appConfig = applicationContext.getBean("appConfig", AppConfig.class);
        Assert.assertNotNull(appConfig);
    }

    @Test
    public void autowiredTest() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppAutowiredConfig.class);
        AppAutowiredConfig appAutowiredConfig = applicationContext.getBean("appAutowiredConfig", AppAutowiredConfig.class);
        Assert.assertNotNull(appAutowiredConfig);
        Assert.assertNull(appAutowiredConfig.getNullValue());
        Assert.assertNotNull(appAutowiredConfig.getAppConfig());
        Assert.assertNotNull(appAutowiredConfig.getTomato());
        Assert.assertNotNull(appAutowiredConfig.getRedTomato());
    }

    @Test
    public void autowiredPrimaryTest() {
        ApplicationContext applicationContext = new AnnotationApplicationContext(AppAutowiredPrimaryConfig.class);


        AppAutowiredPrimaryConfig appAutowiredPrimaryConfig = applicationContext.getBean("appAutowiredPrimaryConfig", AppAutowiredPrimaryConfig.class);
        BeanService beanService = appAutowiredPrimaryConfig.getBeanService();
        Assert.assertTrue(beanService instanceof BeanServiceImpl);
        Banana banana = appAutowiredPrimaryConfig.getBanana();
        Assert.assertEquals("primary", banana.getName());
    }

    @Test
    public void componentScanTest() {
        ApplicationContext applicationContext = new AnnotationApplicationContext(ScanApplication.class);
        IndexController indexController = applicationContext.getBean(IndexController.INDEX, IndexController.class);
        Assert.assertNotNull(indexController);
        Assert.assertEquals(indexController.serviceOutput(), IndexController.INDEX);
    }
}
