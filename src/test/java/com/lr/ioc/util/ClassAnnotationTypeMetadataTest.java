package com.lr.ioc.util;

import com.lr.ioc.annotation.Component;
import com.lr.ioc.annotation.Controller;
import com.lr.ioc.annotation.Indexed;
import com.lr.ioc.annotation.Service;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.Set;

public class ClassAnnotationTypeMetadataTest {

    private ClassAnnotationTypeMetadata metadata;

    @Before
    public void init() {
        metadata = new ClassAnnotationTypeMetadata(TestAnnotationClass.class);
    }

    @Test
    public void isAnnotatedTest() throws Exception {
        Assert.assertTrue(metadata.isAnnotated(TestAnnotation.class));
        Assert.assertFalse(metadata.isAnnotated(Indexed.class));
    }

    @Test
    public void getAnnotationMetaTest() throws Exception {
        Set<Annotation> annotationSet = metadata.getAnnotationMeta(Indexed.class);
        Assert.assertTrue(!annotationSet.isEmpty());
        for (Annotation annotation : annotationSet) {
            Assert.assertTrue(annotation.annotationType().equals(TestAnnotation.class));
        }

        annotationSet = metadata.getAnnotationMeta(Test.class);
        Assert.assertTrue(annotationSet.isEmpty());
    }

    @Test
    public void isAnnotatedOrMetaTest() throws Exception {
        Assert.assertTrue(metadata.isAnnotatedOrMeta(TestAnnotation.class));
        Assert.assertTrue(metadata.isAnnotatedOrMeta(Component.class));
        Assert.assertTrue(metadata.isAnnotatedOrMeta(Indexed.class));
        Assert.assertTrue(metadata.isAnnotatedOrMeta(Service.class));
        Assert.assertTrue(metadata.isAnnotatedOrMeta(Controller.class));
        Assert.assertFalse(metadata.isAnnotatedOrMeta(Test.class));

        Class<?> metaClass = metadata.isAnnotatedOrMeta(new LinkedList<Class<? extends Annotation>>() {{
            add(Controller.class);
            add(Service.class);
        }});
        Assert.assertTrue(metaClass.equals(Controller.class));
    }

    @Test
    public void getDirectComponentAnnotationNameTest() throws Exception {
        Object value = metadata.getDirectComponentAnnotationName(Component.class, "value");
        Assert.assertNotNull(value);
        Assert.assertEquals("test", value);
    }
}
