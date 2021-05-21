package com.lr.ioc.beans.scan.service.impl;


import com.lr.ioc.annotation.Service;
import com.lr.ioc.beans.scan.service.ServiceOutput;

@Service
public class ServiceOutputImpl implements ServiceOutput {

    private String data;

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        return this.data;
    }
}
