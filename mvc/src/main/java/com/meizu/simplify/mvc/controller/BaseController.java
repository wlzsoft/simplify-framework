package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.model.Model;


/**
 * <p><b>Title:</b><i>基础控制器</i></p>
 * <p>Desc: 特定系统中可扩展这个积累，加入系统特有的公有功能</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:24:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:24:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class BaseController<T extends Model> implements IBaseController<T> {
	
	
	@Resource
	private DelegateController<T> delegateController;
	
	/**
	 * 
	 * 方法用途: 拦截处理所有请求<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestUrl 
	 * @param requestMethodName 
	 * @param urlparams 
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	@Override
	public void process(HttpServletRequest request, HttpServletResponse response, String requestUrl,String requestMethodName,boolean isStatic,String[] urlparams)  {
		delegateController.process(request, response, requestUrl, requestMethodName,isStatic, urlparams,this);
	}

	/**
	 * 方法用途: 权限拦截检查,类似过滤器的使用<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestMethodName 作用：对调用的controller方法的名称做标识，用于区分并处理方法间的差异逻辑,常用于权限控制和页面权限控制
	 * @param model
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response,String requestMethodName, T model) throws ServletException, IOException {
		return true;
	}
	
	@Override
	public Object exec(HttpServletRequest request, HttpServletResponse response) {
		return delegateController.exec(request, response);
	}
}
