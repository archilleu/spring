package com.lr.ioc.beans.io;

import java.io.File;
import java.net.URISyntaxException;

public interface Resource {

    File getFile() throws URISyntaxException;

}
