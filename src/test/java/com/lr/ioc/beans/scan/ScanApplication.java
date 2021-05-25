package com.lr.ioc.beans.scan;

import com.lr.ioc.annotation.ComponentScan;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.beans.scan.advisor.TestAop;
import com.lr.ioc.context.AnnotationApplicationContext;
import com.lr.ioc.context.ApplicationContext;
import org.junit.Assert;
import org.junit.Test;

@Configuration
@ComponentScan
public class ScanApplication {
    @Test
    public void test() throws Exception {
        ApplicationContext applicationContext = new AnnotationApplicationContext(ScanApplication.class);
        TestAop testAop = applicationContext.getBean("testAop", TestAop.class);
        Assert.assertNotNull(testAop);
        String string = "abc";
        Assert.assertEquals(string, testAop.text(string));
    }
}
