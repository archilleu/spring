package com.lr.ioc.beans;

import com.lr.ioc.annotation.Bean;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.annotation.Primary;

@Configuration
public class AppMultiBeanConfig {

    @Bean
    @Primary
    public Banana banana() {
        return new Banana("primary");
    }

    @Bean
    public Banana banana1() {
        return new Banana();
    }
}
