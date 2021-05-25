package com.lr.ioc.beans.scan.advisor;

import com.lr.ioc.annotation.Controller;

@Controller
public class TestAop {

    public String text(String text) {
        return text;
    }
}
