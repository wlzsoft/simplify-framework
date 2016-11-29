package com.meizu.simplify.net.rest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAddressInfo {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(RestAddressInfo.class);

	private List<URL> urls = new ArrayList<URL>();

    public RestAddressInfo(String url, List<String> hostList) {
        for (String host : hostList) {
            try {
                urls.add(new URL(host + url));
            } catch (MalformedURLException e) {
                LOGGER.error(e.toString());
            }
        }
    }

    public List<URL> getUrls() {
        return urls;
    }
}
