package com.lr.ioc.beans;

import com.lr.ioc.annotation.Configuration;

@Configuration
public class BeanServiceImpl1 implements BeanService {

    public static final String MESSAGE = "first bean1";

    @Override
    public String getMessage() {
        return MESSAGE;
    }

    @Override
    public void helloWorld() {
        System.out.println("hello world");
    }

    @Override
    public void outputHelloWorld(String text) {
        outputService.output(text);
    }

    @Override
    public String toString() {
        return p1 + " " + p2 + " " + ref + " " + outputService;
    }

    private String p1;

    private Integer p2;

    private RefBeanService ref;

    private OutputService outputService;
}
