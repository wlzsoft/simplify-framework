package com.meizu;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

	private String sessionId;// session的一个标识

	private Map<String, Object> attributes = new HashMap<String, Object>();// 给自己session开辟的空间

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
}
