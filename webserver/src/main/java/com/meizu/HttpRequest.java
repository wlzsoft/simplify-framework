package com.meizu;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {
	
	
	private String method;// 请求方法
	private String requestUrl;// 请求地址
	private String version;// 请求版本
	private final Map<String, String> cookies = new HashMap<String, String>();// cookies
	private Map<String, String> requestHeader = new HashMap<String, String>();// 用hashmap存储请求头
	private final Map<String, String> parameters = new HashMap<String, String>();// 用来存放datas里面的数据。命名空间里的数据，？后面
	private char[] body;

	private HttpSession session;

	// 解析请求头
	public void parseRequestLine(String line) throws Exception {
		String[] strs = line.split(" ");// 从空格分割
		if (strs.length != 3) {
			throw new Exception("invalide request line !");
		}
		method = strs[0].trim();
		requestUrl = strs[1].trim();
		version = strs[2].trim();
	}

	// 读入开始行
	public void parseRequestHeader(String line) {
		String[] strs = line.split(":", 2);// 只用一次冒号
		if (strs.length == 2) {
			if (strs[0].trim().equals("Cookie")) {
				String[] cookies = strs[1].split(";");
				for (String str : cookies) {
					String[] values = str.split("=");
					if (values.length == 2) {
						this.cookies.put(values[0].trim(), values[1].trim());// 判断是否有cookies，如果有分割的方式就不一样，注意去掉空格
					}
				}
			} else {
				requestHeader.put(strs[0].trim(), strs[1].trim());// 存入请求头
			}

		}
	}

	public String getParameter(String name) {

		return parameters.get(name);

	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Map<String, String> getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Map<String, String> requestHeader) {
		this.requestHeader = requestHeader;
	}

	public char[] getBody() {
		return body;
	}

	public void setBody(char[] body) {
		this.body = body;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}
	
		

}
