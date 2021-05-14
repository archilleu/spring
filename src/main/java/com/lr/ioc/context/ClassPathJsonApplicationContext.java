package com.lr.ioc.context;

import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.beans.io.ResourceLoader;
import com.lr.ioc.beans.json.JsonBeanDefinitionReader;

import java.util.Map;

public class ClassPathJsonApplicationContext extends AbstractApplicationContext {

    private String configLocation;

    public ClassPathJsonApplicationContext() throws Exception {
        this(CLASSPATH_JSON_DEFAULT_FILE, new AbstractBeanFactory());
    }

    public ClassPathJsonApplicationContext(String configLocation) throws Exception {
        this(configLocation, new AbstractBeanFactory());
    }

    public ClassPathJsonApplicationContext(String configLocation, AbstractBeanFactory beanFactory) throws Exception {
        super(beanFactory);
        this.configLocation = configLocation;
        refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) {
        JsonBeanDefinitionReader jsonBeanDefinitionReader = new JsonBeanDefinitionReader(new ResourceLoader());

        // 1.读取配置文件
        jsonBeanDefinitionReader.loadBeanDefinitions(this.configLocation);

        // 2.初始化BeanFactory并注册beans
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : jsonBeanDefinitionReader.getRegistry().entrySet()) {
            super.beanFactory.registerBeanDefinition(beanDefinitionEntry.getKey(), beanDefinitionEntry.getValue());
        }
    }

    private final static String CLASSPATH_JSON_DEFAULT_FILE = "bean.json";
}
