package com.lr.ioc.beans;

public class BeanController {

    public static final String MESSAGE = "first bean";

    public void helloWorld() {
        System.out.println("hello world");
    }

    @Override
    public String toString() {
        return MESSAGE;
    }

    private String p1;

    private Integer p2;

    private RefBeanController ref;
}
