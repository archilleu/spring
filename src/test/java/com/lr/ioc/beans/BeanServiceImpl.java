package com.lr.ioc.beans;

public class BeanServiceImpl implements BeanService {

    public static final String MESSAGE = "first bean";

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
