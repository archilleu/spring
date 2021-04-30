package com.lr.ioc.beans;

import com.lr.ioc.beans.PropertyValue;
import com.lr.ioc.beans.PropertyValues;
import org.junit.Assert;
import org.junit.Test;

import java.util.LinkedList;

public class PropertyValueTest {
    @Test
    public void test() {
        PropertyValue propertyValue1 = new PropertyValue("a", "b");
        PropertyValue propertyValue2 = new PropertyValue("b", 1);
        PropertyValue propertyValue3 = new PropertyValue("c", new LinkedList<>());

        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(propertyValue1);
        propertyValues.addPropertyValue(propertyValue2);
        propertyValues.addPropertyValue(propertyValue3);
        propertyValues.addPropertyValue(propertyValue1);
        propertyValues.addPropertyValue(propertyValue2);
        propertyValues.addPropertyValue(propertyValue3);

        Assert.assertEquals(3, propertyValues.getPropertyValues().size());
    }
}
