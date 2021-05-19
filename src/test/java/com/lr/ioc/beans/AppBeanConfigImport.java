package com.lr.ioc.beans;

import com.lr.ioc.annotation.Bean;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.annotation.Import;
import com.lr.ioc.beans.factory.ColorTomato;

@Configuration
@Import(AppBeanConfig.class)
public class AppBeanConfigImport {

    @Bean
    public ColorTomato colorTomato() {
        return new ColorTomato();
    }

    @Bean("redTomato")
    public ColorTomato redColorTomato() {
        return new ColorTomato("red");
    }
}
