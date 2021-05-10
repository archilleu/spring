package com.lr.ioc.context;

import com.lr.ioc.beans.BeanService;
import com.lr.ioc.beans.BeanServiceImpl;
import com.lr.ioc.beans.OutputService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        BeanService beanService = (BeanService) applicationContext.getBean("beanService");
        Assert.assertEquals(beanService.getMessage(), BeanServiceImpl.MESSAGE);
        beanService.helloWorld();
    }

    @Test
    public void testPrototype() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        BeanService beanService = (BeanService) applicationContext.getBean("beanService1");
        Assert.assertEquals(beanService.getMessage(), BeanServiceImpl.MESSAGE);
        beanService.helloWorld();
    }

    @Test
    public void testLazyInit() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        OutputService outputService = (OutputService) applicationContext.getBean("outputServiceLazy");
        outputService.output("out put hello world");
    }

    @Test
    public void testPostBeanProcessor() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext("bean-post-processor.json");
        BeanService beanService = (BeanService) applicationContext.getBean("beanService");
        Assert.assertEquals(beanService.getMessage(), BeanServiceImpl.MESSAGE);
    }

}
