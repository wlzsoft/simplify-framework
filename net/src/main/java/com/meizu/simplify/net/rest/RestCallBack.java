package com.meizu.simplify.net.rest;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestCallBack<T>  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestCallBack.class);

    public RestCallBack(Class<T> clazz, URL url) {

    }

    public T call() throws Exception {
        return null;
    }
}
