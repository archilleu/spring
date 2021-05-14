package com.lr.ioc.beans.factory;

import lombok.Data;

@Data
public class ColorTomato {

    private String color;

    public ColorTomato() {
    }

    public ColorTomato(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "tomato";
    }

}
