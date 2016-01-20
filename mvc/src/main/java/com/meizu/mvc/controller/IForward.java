package com.meizu.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.webcache.annotation.CacheSet;


/**
 * 回调请求接口
 */
public interface IForward {
	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException;
}
