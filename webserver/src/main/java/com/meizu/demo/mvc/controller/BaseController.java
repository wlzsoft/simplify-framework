package com.meizu.demo.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.mvc.controller.IForward;
import com.meizu.simplify.mvc.directives.Model;
import com.meizu.simplify.mvc.directives.SecurityContoller;

/**
 * <p><b>Title:</b><i>controller基类</i></p>
 * <p>Desc: 目前这个类没有实质的作用，具体业务开发时，部分业务公用代码移植到这里</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午2:34:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午2:34:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class BaseController<T extends Model> extends SecurityContoller<T> {
	
	public BaseController() {}

	@Override
	public final void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		request.setAttribute("url", request.getRequestURI());
		super.process(request, response);
	}
	
	@Override
	public final IForward execute(final HttpServletRequest request, final HttpServletResponse response, final T t) throws ServletException, IOException {
		return super.execute(request, response, t);
	}
}
