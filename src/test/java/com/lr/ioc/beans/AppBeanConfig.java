package com.lr.ioc.beans;

import com.lr.ioc.annotation.Bean;
import com.lr.ioc.annotation.Configuration;

@Configuration
public class AppBeanConfig {

    @Bean
    public AppConfig appConfig() {
        return new AppConfig();
    }

}
