package com.meizu.mvc.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.mvc.MvcInit;
import com.meizu.webcache.annotation.CacheSet;


/**
 * 页面处理返回方式
 * 
 */
public class RedirectForward implements IForward {
	private String url = null;
	private HashMap<String, String> paramMap = new HashMap<String, String>();
	
	public RedirectForward(String url) {
		this.url = url;
	}
	
	public RedirectForward(String url, HashMap<String, String> paramMap) {
		this.url = url;
		this.paramMap = paramMap;
	}
	
	public RedirectForward addPrarm(String key, String value) {
		paramMap.put(key, value);
		return this;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public HashMap<String, String> getParamMap() {
		return paramMap;
	}
	
	public void setParamMap(HashMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {
		if (paramMap.keySet().size() > 0) {
			StringBuffer w = new StringBuffer("<html><head></head><body onload=\"form1.submit()\"><form id=\"form1\" method=\"post\" action=\"" + url + "\">");
			for (String key : paramMap.keySet()) {
				w.append("<input type=\"hidden\"  name=\"" + key + "\" value=\"" + paramMap.get(key) + "\"/>");
			}
			w.append("</form></body></html>");
			response.setContentType("text/html; charset=" + MvcInit.charSet);
			response.getWriter().print(w.toString());
		} else {
			response.sendRedirect(url);
		}
	}
}
