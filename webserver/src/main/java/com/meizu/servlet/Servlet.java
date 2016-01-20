package com.meizu.servlet;

import com.meizu.HttpRequest;
import com.meizu.HttpResponse;
public interface Servlet {
	
	default void service(HttpRequest request,HttpResponse response) {
		doGet(request, response);
		doPost(request, response);
	}
	
	public void doGet(HttpRequest request, HttpResponse response);
	public void doPost(HttpRequest request, HttpResponse response);
}
