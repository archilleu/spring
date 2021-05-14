package com.lr.ioc.beans.factory;

import com.lr.ioc.annotation.FactoryMethod;
import lombok.Data;

@Data
public class Tomato {

    private String color;

    public Tomato() {
    }

    @FactoryMethod
    public static Tomato newInstance() {
        Tomato tomato = new Tomato();
        tomato.setColor("define");
        return tomato;
    }

    @Override
    public String toString() {
        return "tomato";
    }

}
