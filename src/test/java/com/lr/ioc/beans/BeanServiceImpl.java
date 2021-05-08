package com.lr.ioc.beans;

public class BeanServiceImpl implements BeanService {

    public static final String MESSAGE = "first bean";

    @Override
    public void helloWorld() {
        System.out.println("hello world");
    }

    @Override
    public String toString() {
        return MESSAGE;
    }

    private String p1;

    private Integer p2;

    private RefBeanService ref;

    private OutputService outputService;
}
