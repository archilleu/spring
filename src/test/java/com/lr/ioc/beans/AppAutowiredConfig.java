package com.lr.ioc.beans;

import com.lr.ioc.annotation.Autowired;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.annotation.Import;
import com.lr.ioc.beans.factory.ColorTomato;
import lombok.Data;

@Data
@Configuration
@Import(AppBeanConfigImport.class)
public class AppAutowiredConfig {

    @Autowired
    private AppConfig appConfig;

    @Autowired("colorTomato")
    private ColorTomato tomato;

    @Autowired("redTomato")
    private ColorTomato redTomato;

    private String nullValue;

    public String getColor() {
        return tomato.getColor();
    }
}
