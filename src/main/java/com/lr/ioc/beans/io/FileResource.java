package com.lr.ioc.beans.io;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class FileResource implements Resource {

    private final URL url;

    public FileResource(URL url) {
        this.url = url;
    }

    @Override
    public File getFile() throws URISyntaxException {
        return new File(url.toURI());
    }

}
