package com.lr.ioc.context;

import com.alibaba.fastjson.JSONArray;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.beans.BeanDefinition;
import com.lr.ioc.beans.factory.AbstractBeanFactory;
import com.lr.ioc.constant.enums.ScopeEnum;
import com.lr.ioc.exception.IocRuntimeException;
import com.lr.ioc.support.name.BeanNameStrategy;
import com.lr.ioc.support.name.impl.DefaultBeanNameStrategy;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

public class AnnotationApplicationContext extends AbstractApplicationContext {

    private Class[] configClasses;

    @Setter
    private BeanNameStrategy beanNameStrategy = new DefaultBeanNameStrategy();

    public AnnotationApplicationContext(Class... configClasses) throws IocRuntimeException {
        this(new AbstractBeanFactory(), configClasses);
    }

    public AnnotationApplicationContext(AbstractBeanFactory beanFactory, Class... configClasses) {
        super(beanFactory);

        this.configClasses = configClasses;

        super.refresh();
    }

    @Override
    protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) {
        for (Class clazz : configClasses) {
            if (clazz.isAnnotationPresent(Configuration.class)) {
                BeanDefinition beanDefinition = new BeanDefinition();
                processBeanDefinitionProperty(null, beanDefinition);
                try {
                    beanDefinition.setBeanClassName(clazz.getName());
                } catch (ClassNotFoundException e) {
                    throw new IocRuntimeException(e);
                }
                beanDefinition.setScope(ScopeEnum.SINGLETON.getCode());
                beanDefinition.setLazyInit(false);

                Configuration configuration = (Configuration) clazz.getAnnotation(Configuration.class);
                String id = configuration.value();
                if (StringUtils.isEmpty(id)) {
                    id = beanNameStrategy.generateBeanName(beanDefinition);
                }
                beanDefinition.setId(id);
                super.beanFactory.registerBeanDefinition(id, beanDefinition);
            }
        }
    }

    private void processBeanDefinitionProperty(JSONArray properties, BeanDefinition beanDefinition) {
//        if (null == properties) {
//            return;
//        }
//
//        Iterator<Object> iterator = properties.iterator();
//        while (iterator.hasNext()) {
//            JSONObject item = (JSONObject) iterator.next();
//            String name = item.getString(RESERVE_KEYWORD_BEAN_PROPERTY_NAME);
//            Object value = item.get(RESERVE_KEYWORD_BEAN_PROPERTY_VALUE);
//            // 判断属性值是否为空
//            if (value != null) {
//                // 1.不为空,排除是ref的可能
//                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
//            } else {
//                // 1.为空,不是是ref
//                String ref = item.getString(RESERVE_KEYWORD_BEAN_PROPERTY_REF);
//                if (ref == null) {
//                    throw new IllegalArgumentException("Configuration problem: <property> element for property"
//                            + name + " must specify a ref or value");
//                }
//                //是ref
//                BeanReference beanReference = new BeanReference(ref);
//                beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
//            }
//            // 2.空
//        }
    }
}
