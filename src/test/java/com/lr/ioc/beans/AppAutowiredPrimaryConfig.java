package com.lr.ioc.beans;

import com.lr.ioc.annotation.Autowired;
import com.lr.ioc.annotation.Configuration;
import com.lr.ioc.annotation.Import;
import lombok.Data;

@Data
@Configuration
@Import({AppMultiBeanConfig.class, BeanServiceImpl1.class, BeanServiceImpl.class})
public class AppAutowiredPrimaryConfig {

    @Autowired
    private Banana banana;

    @Autowired
    private BeanService beanService;
}
