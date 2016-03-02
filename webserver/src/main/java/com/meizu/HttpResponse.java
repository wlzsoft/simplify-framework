package com.meizu;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
	private String version = "HTTP/1.1";// 版本默认值
	private String statusCode;
	private String reason;// 原因短语

	private Map<String, String> responseHeader = new HashMap<String, String>();

	private char[] body;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Map<String, String> getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(Map<String, String> responseHeader) {
		this.responseHeader = responseHeader;
	}

	public char[] getBody() {
		return body;
	}

	public void setBody(char[] body) {
		this.body = body;
	}
	
}
