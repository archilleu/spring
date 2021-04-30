package com.lr.ioc.beans;

import lombok.Data;

@Data
public class PropertyValue {

    private final String name;

    private final Object value;

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + this.name.hashCode();
        result = result * 31 + this.value.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        // 1.同一对象
        if (this == o) {
            return true;
        }

        // 2.同一个类别
        if (!(o instanceof PropertyValue)) {
            return false;
        }

        // 3.同样的数据
        PropertyValue propertyValue = (PropertyValue) o;
        return this.name.equals(propertyValue.name)
                && this.value.equals(propertyValue.value);
    }
}
