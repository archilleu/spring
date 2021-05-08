package com.lr.ioc.beans.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lr.ioc.beans.AbstractBeanDefinitionReader;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.BeanReference;
import com.lr.ioc.beans.PropertyValue;
import com.lr.ioc.beans.io.ResourceLoader;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public JsonBeanDefinitionReader(ResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws Exception {
        File file = super.getResourceLoader().getResource(location).getFile();
        JSONObject jsonObject = JSONObject.parseObject(new String(Files.readAllBytes(Paths.get(file.toURI()))));

        registerBeanDefinitions(jsonObject);
    }

    private void registerBeanDefinitions(JSONObject root) throws ClassNotFoundException {
        JSONArray beans = root.getJSONArray(RESERVE_KEYWORD_BEANS);
        if (null != beans) {
            processBeanDefinition(beans);
        }

        return;
    }

    private void processBeanDefinition(JSONArray beans) throws ClassNotFoundException {
        Iterator<Object> iterator = beans.iterator();
        while (iterator.hasNext()) {
            JSONObject item = (JSONObject) iterator.next();
            JSONArray properties = item.getJSONArray(RESERVE_KEYWORD_BEAN_PROPERTY);

            BeanDefinition beanDefinition = new BeanDefinition();
            processBeanDefinitionProperty(properties, beanDefinition);

            String name = item.getString(RESERVE_KEYWORD_BEAN_ID);
            String clazz = item.getString(RESERVE_KEYWORD_BEAN_CLASS);
            beanDefinition.setBeanClassName(clazz);
            super.getRegistry().put(name, beanDefinition);
        }
    }

    private void processBeanDefinitionProperty(JSONArray properties, BeanDefinition beanDefinition) {
        if (null == properties) {
            return;
        }

        Iterator<Object> iterator = properties.iterator();
        while (iterator.hasNext()) {
            JSONObject item = (JSONObject) iterator.next();
            String name = item.getString(RESERVE_KEYWORD_BEAN_PROPERTY_NAME);
            Object value = item.get(RESERVE_KEYWORD_BEAN_PROPERTY_VALUE);
            // 判断属性值是否为空
            if (value != null) {
                // 1.不为空,排除是ref的可能
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
            } else {
                // 1.为空,不是是ref
                String ref = item.getString(RESERVE_KEYWORD_BEAN_PROPERTY_REF);
                if (ref == null) {
                    throw new IllegalArgumentException("Configuration problem: <property> element for property"
                            + name + " must specify a ref or value");
                }
                //是ref
                BeanReference beanReference = new BeanReference(ref);
                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
            }
            // 2.空
        }
    }

    private static final String RESERVE_KEYWORD_BEANS = "beans";
    private static final String RESERVE_KEYWORD_BEAN_ID = "id";
    private static final String RESERVE_KEYWORD_BEAN_CLASS = "class";
    private static final String RESERVE_KEYWORD_BEAN_PROPERTY = "property";
    private static final String RESERVE_KEYWORD_BEAN_PROPERTY_NAME = "name";
    private static final String RESERVE_KEYWORD_BEAN_PROPERTY_VALUE = "value";
    private static final String RESERVE_KEYWORD_BEAN_PROPERTY_REF = "ref";
}
