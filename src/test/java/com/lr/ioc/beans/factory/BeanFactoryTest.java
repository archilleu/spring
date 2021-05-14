package com.lr.ioc.beans.factory;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanServiceImpl;
import com.lr.ioc.beans.PropertyValue;
import com.lr.ioc.beans.PropertyValues;
import com.lr.ioc.beans.io.ResourceLoader;
import com.lr.ioc.beans.json.JsonBeanDefinitionReader;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class BeanFactoryTest {

    @Test
    public void test() throws Exception {
        // 1.init
        BeanFactory beanFactory = new AbstractBeanFactory();

        // 2.inject
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName("com.lr.ioc.beans.BeanServiceImpl");

        // 3.set property
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("p1", "hello world"));
        propertyValues.addPropertyValue(new PropertyValue("p2", 1));
        beanDefinition.setPropertyValues(propertyValues);

        // 4.register bean
        ((AbstractBeanFactory) beanFactory).registerBeanDefinition(BEAN_NAME, beanDefinition);

        // 3.get bean
        BeanServiceImpl beanController = (BeanServiceImpl) beanFactory.getBean(BEAN_NAME);
        Assert.assertEquals(beanController.getMessage(), BeanServiceImpl.MESSAGE);
    }

    @Test
    public void testInstantiate() throws Exception {
        // 1.读取配置文件
        JsonBeanDefinitionReader jsonBeanDefinitionReader = new JsonBeanDefinitionReader(new ResourceLoader());
        jsonBeanDefinitionReader.loadBeanDefinitions("bean.json");

        // 2.初始化BeanFactory并注册beans
        AbstractBeanFactory beanFactory = new AbstractBeanFactory();
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : jsonBeanDefinitionReader.getRegistry().entrySet()) {
            beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }

        // 3.初始化bean
        beanFactory.preInstantiateSingletons();

        // 4.获取bean
        BeanServiceImpl beanController = (BeanServiceImpl) beanFactory.getBean(BEAN_NAME);
        Assert.assertEquals(beanController.getMessage(), BeanServiceImpl.MESSAGE);
    }

    final String BEAN_NAME = "beanService";
}
