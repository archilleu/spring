package com.lr.ioc.beans;

import com.lr.ioc.annotation.Bean;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.annotation.Lazy;
import com.lr.ioc.annotation.Scope;
import com.lr.ioc.beans.factory.ColorTomato;
import com.lr.ioc.beans.factory.Tomato;
import com.lr.ioc.constant.ScopeConst;

@Configuration
public class AnnoLazyScopeConfig {

    @Bean
    @Lazy(value = true)
    public ColorTomato colorTomato() {
        return new ColorTomato();
    }

    @Bean
    @Scope(ScopeConst.PROTOTYPE)
    public Tomato tomato() {
        return new Tomato();
    }
}
