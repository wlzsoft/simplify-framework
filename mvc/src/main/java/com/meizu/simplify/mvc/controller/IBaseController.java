package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
  * <p><b>Title:</b><i>请求处理器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月29日 下午5:44:59</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月29日 下午5:44:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IBaseController<T> {
	/**
	 * 方法用途: 开始处理请求<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @return
	 */
	public Object exec(HttpServletRequest request,HttpServletResponse response);
	
	public default void process(final HttpServletRequest request, final HttpServletResponse response,String requestUrl,String requestMethodName,String[] urlparams) {
		exec(request,response);
	}
	
	public default void execute(HttpServletRequest request, HttpServletResponse response,String cmd, T model,String requestUrl)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			ServletException {
		
	}

	public default boolean checkPermission(HttpServletRequest request, HttpServletResponse response,String cmd, T model) throws ServletException, IOException {
		return true;
	}
}
