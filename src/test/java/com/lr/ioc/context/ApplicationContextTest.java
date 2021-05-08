package com.lr.ioc.context;

import com.lr.ioc.beans.BeanService;
import com.lr.ioc.beans.BeanServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        BeanService beanService = (BeanService) applicationContext.getBean("beanService");
        Assert.assertEquals(beanService.toString(), BeanServiceImpl.MESSAGE);
        beanService.helloWorld();
    }

    @Test
    public void testPostBeanProcessor() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext("bean-post-processor.json");
        BeanService beanService = (BeanService) applicationContext.getBean("beanService");
        Assert.assertEquals(beanService.toString(), BeanServiceImpl.MESSAGE);
    }

}
