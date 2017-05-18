package vip.simplify.net.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class RestCallBack<T>  {

    protected static final Logger LOGGER = LoggerFactory.getLogger(RestCallBack.class);
    
    private Class<T> clazz;
    
    private URL url;

    public RestCallBack(Class<T> clazz, URL url) {
    	this.clazz = clazz;
    	this.url = url;
    }

    public T call() throws Exception {
    	String content = "";
    	HttpURLConnection conn = null;
    	InputStream inputStream = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
	    	if(conn.getResponseCode() == 200) {
	    		inputStream = conn.getInputStream();
	    	} else {
	    		inputStream = conn.getErrorStream();
	    	}
		
	    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	    	String line = "";
	    	while((line = bufferedReader.readLine()) != null) {
	    		content += line;
	    	}
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			/*if(conn != null) {
				conn.disconnect();
			}*/
		}
		LOGGER.debug("["+url.toString()+"]的请求结果："+content);
    	T result = JSON.parseObject(content, clazz);
        return result;
    }
}
