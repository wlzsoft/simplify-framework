package com.meizu.mvc.directives;

import javax.servlet.http.HttpServlet;

/**
 * Servlet模型
 * 
 */
public class ServletModel {
	Class<HttpServlet> cls;
	private String method = "doView";

	public Class<HttpServlet> getCls() {
		return cls;
	}

	public void setCls(Class<HttpServlet> cls) {
		this.cls = cls;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public ServletModel(Class<HttpServlet> cls, String method){
		this.cls = cls;
		this.method = method;
	}
}
