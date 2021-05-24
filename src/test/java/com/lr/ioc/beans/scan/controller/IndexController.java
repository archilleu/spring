package com.lr.ioc.beans.scan.controller;

import com.lr.ioc.annotation.Autowired;
import com.lr.ioc.annotation.Controller;
import com.lr.ioc.beans.scan.service.ServiceOutput;

@Controller("index")
public class IndexController {

    public static final String INDEX = "index";

    @Autowired
    ServiceOutput serviceOutput;

    public String index() {
        return INDEX;
    }

    public String serviceOutput() {
        serviceOutput.setData(INDEX);
        return serviceOutput.getData();
    }
}
