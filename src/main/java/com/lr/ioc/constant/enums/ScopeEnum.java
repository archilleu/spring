package com.lr.ioc.constant.enums;

public enum ScopeEnum {

    SINGLETON("singleton"),

    PROTOTYPE("prototype"),
    ;

    private final String code;

    ScopeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
