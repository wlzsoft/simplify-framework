package com.meizu;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	
	
	private String method;// POST 或 GET
	private String requestUrl;
	private String version;// 请求版本
	private final Map<String, String> cookies = new HashMap<String, String>();
	private Map<String, String> requestHeader = new HashMap<String, String>();
	private final Map<String, String> parameters = new HashMap<String, String>();//存储post和get请求的参数值 
	private char[] body;

	private HttpSession session;

	// 解析请求头
	public void parseRequestLine(String line) throws Exception {
		String[] strs = line.split(" ");
		if (strs.length != 3) {
			throw new Exception("invalide request line !");
		}
		method = strs[0].trim();
		requestUrl = strs[1].trim();
		String[] requestUrlArr = requestUrl.split("\\?");
		if(requestUrlArr.length>1) {
			requestUrl = requestUrlArr[0];
			String[] parameters = requestUrlArr[1].split("&");
			for (String str : parameters) {
				String[] datas = str.split("=");
				getParameters().put(datas[0], datas[1]);
			}
		}
		
		
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
				requestHeader.put(strs[0].trim(), strs[1].trim());
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
