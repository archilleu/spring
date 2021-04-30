package com.lr.ioc.beans.io;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

public class ResourceLoaderTest {

    @Test
    public void test() throws URISyntaxException {
        ResourceLoader resourceLoader = new ResourceLoader();
        Resource resource = resourceLoader.getResource("bean.json");
        File file = resource.getFile();
        Assert.assertNotNull(file);
    }
}
