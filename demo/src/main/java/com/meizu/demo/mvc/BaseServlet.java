package com.meizu.demo.mvc;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.system.SystemConfig;
import com.meizu.mvc.controller.IForward;
import com.meizu.mvc.directives.Model;
import com.meizu.mvc.directives.SecurityServlet;

public class BaseServlet<T extends Model> extends SecurityServlet<T> {
	
	public BaseServlet() {}

	@Override
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		request.setAttribute("url", request.getRequestURI());
		super.doPost(request, response);
	}
	
	@Override
	public final IForward execute(final HttpServletRequest request, final HttpServletResponse response, final T t) throws ServletException, IOException {
		return super.execute(request, response, t);
	}
}
