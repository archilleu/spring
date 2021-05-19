package com.lr.ioc.beans;

import lombok.Data;

@Data
public class Banana {
    public Banana() {
        name = "default";
    }

    public Banana(String name) {
        this.name = name;
    }

    private String name;
}
