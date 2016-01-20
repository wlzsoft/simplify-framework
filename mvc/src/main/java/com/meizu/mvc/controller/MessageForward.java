package com.meizu.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.mvc.MvcInit;
import com.meizu.webcache.annotation.CacheSet;


/**
 * 页面处理返回方式
 * 
 */
public class MessageForward implements IForward {
	private String msg = "";

	public MessageForward(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {
		response.setCharacterEncoding(MvcInit.charSet);
		response.setContentType("text/html; charset=" + MvcInit.charSet);
		response.getWriter().print(msg);
	}
}
