package com.lr.ioc.beans.json;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.io.ResourceLoader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class JsonBeanDefinitionReaderTest {
    @Test
    public void test() throws Exception {
        JsonBeanDefinitionReader jsonBeanDefinitionReader = new JsonBeanDefinitionReader(new ResourceLoader());
        jsonBeanDefinitionReader.loadBeanDefinitions("bean.json");
        Map<String, BeanDefinition> registry = jsonBeanDefinitionReader.getRegistry();
        Assert.assertNotNull(registry);
        Assert.assertTrue(registry.size() > 0);
    }
}
