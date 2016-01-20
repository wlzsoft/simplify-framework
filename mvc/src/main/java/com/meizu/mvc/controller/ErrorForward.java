package com.meizu.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.webcache.annotation.CacheSet;

/**
 * 页面处理返回方式
 * 
 */
public class ErrorForward implements IForward {
	private String msg = "";
	private int errorCode = 403; // 错误Code识别

	public ErrorForward(String msg) {
		this.msg = msg;
	}

	public ErrorForward(String msg, int errorCode) {
		this.msg = msg;
		this.errorCode = errorCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {
		response.sendError(errorCode, msg);
	}
}
