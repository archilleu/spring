package com.lr.ioc.beans;

import java.util.HashSet;
import java.util.Set;

public class PropertyValues {

    private final Set<PropertyValue> propertyValues = new HashSet<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }

    public Set<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }
}
