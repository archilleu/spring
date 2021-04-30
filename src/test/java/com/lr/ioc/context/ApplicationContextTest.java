package com.lr.ioc.context;

import com.lr.ioc.beans.BeanController;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTest {

    @Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new ClassPathJsonApplicationContext();
        BeanController beanController = (BeanController) applicationContext.getBean("beanController");
        Assert.assertEquals(beanController.toString(), BeanController.MESSAGE);
    }

}
