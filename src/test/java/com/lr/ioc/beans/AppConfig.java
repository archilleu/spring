package com.lr.ioc.beans;

import com.lr.ioc.annotation.Configuration;

@Configuration
public class AppConfig {

    public String name() {
        return "app config";
    }
}
