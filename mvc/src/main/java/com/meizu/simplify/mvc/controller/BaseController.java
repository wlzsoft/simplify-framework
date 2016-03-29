package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.view.BeetlForward;
import com.meizu.simplify.mvc.view.HttlForward;
import com.meizu.simplify.mvc.view.JSPForward;
import com.meizu.simplify.mvc.view.JsonForward;
import com.meizu.simplify.mvc.view.JsonpForward;
import com.meizu.simplify.mvc.view.MessageForward;
import com.meizu.simplify.mvc.view.RedirectForward;
import com.meizu.simplify.mvc.view.VelocityForward;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.ReflectionGenericUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>基础控制器</i></p>
 * <p>Desc: 特定系统中可扩展这个积累，加入系统特有的公有功能</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:24:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:24:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class BaseController<T extends Model> {
	
	
	/**
	 * 
	 * 方法用途: 拦截处理所有请求<br>
	 * 操作步骤: TODO<br>
	 * @param req
	 * @param response
	 * @param requestUrl 
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void process(HttpServletRequest request, HttpServletResponse response, String requestUrl) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricType(getClass(),0);
		T model = AnalysisRequestControllerModel.setRequestModel(request,entityClass);
		if (checkPermission(request, response, model)) {
			execute(request, response, model,requestUrl);
		}
		destroy(request, response, model);
		
	}

	/**
	 * 
	 * 方法用途: controller 注销相关处理<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param t
	 */
	public void destroy(HttpServletRequest request, HttpServletResponse response, T t){
//			System.out.println("回收数据库连接到连接池中");
	}
	
	/**
	 * 
	 * 方法用途: 安全权限过程检查<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param t
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T t) throws ServletException, IOException {
		return true;
	}
	
	/**
	 * 
	 * 方法用途: 执行逻辑<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param model
	 * @param staticName 
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, T model,String requestUrl) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException  {
//		RequestAnalysisWrapper.java
		if (model.getCmd() == null || model.getCmd().length() <= 0) {
			return;
		}
		String doCmd = model.getCmd();
		
		//页面静态化名字		
		String staticName = MD5Encrypt.sign(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".lv";
		
		Method[] methods = this.getClass().getMethods();
		Method doMethod = CollectionUtil.getItem(methods,doCmd, (m,w) -> doCmd.equals(m.getName()));
		if (doMethod == null) {
			throw new IllegalArgumentException("The method named, " + doCmd + ", is not specified by " + this.getClass()); 
		}
		if (doMethod.getParameterTypes().length < 3) { //考虑model问题，后续可以做更灵活调整
			throw new IllegalArgumentException("类:["+this.getClass()+"] 的方法 :[" + doCmd + "]的参数的长度不能小于3" ); 
		}

		Object[] parameValue = AnalysisRequestControllerMethod.analysisRequestParam(request, response, model, doMethod);
		
		AnalysisRequestControllerMethod.analysisAjaxAccess(request, response, doMethod);
		
		WebCache webCache = doMethod.getAnnotation(WebCache.class);
		boolean isCache = AnalysisRequestControllerMethod.analysisWebCache(response, staticName, doMethod,webCache);
		if(isCache) {
			return;
		}
		
		request.setAttribute("formData", model);
		Object obj = doMethod.invoke(this,parameValue);
		if(requestUrl.endsWith(".json")) {
			JsonForward.doAction(request, response, webCache, staticName, obj);
		} else if(requestUrl.endsWith(".jsonp")) {
			JsonpForward.doAction(request, response, webCache, staticName, obj,model,"meizu.com");
		} else {
//			String templateUri = "/template/jsp";
			String templateUri = "";
//			String extend = ".jsp";
			String extend = "";
			String uri = "";
			if(obj != null && obj instanceof String) {//尽量避免instanceof操作，后续这里要优化
				uri = String.valueOf(obj);
				uri = templateUri+uri+extend;
				String[] uriArr = uri.split(":");
				switch (uriArr[0]) {
					case "uri":
						new JSPForward().doAction(request, response, webCache, staticName, uriArr[1]);//配置文件中读取
						break;
					case "redirect":
						RedirectForward.doAction(request, response, webCache, staticName, uriArr[1]);
						break;
					case "jsp":
						new JSPForward().doAction(request, response, webCache, staticName, uriArr[1]);
						break;
					case "beetl":
						new BeetlForward().doAction(request, response, webCache, staticName, uriArr[1]);
						break;
					case "httl":
						new HttlForward().doAction(request, response, webCache, staticName, uriArr[1]);
						break;
					case "velocity":
						new VelocityForward().doAction(request, response, webCache, staticName, uriArr[1]);
						break;
					default :
						MessageForward.doAction(request, response, webCache, staticName, uri);
				}
				
			} else {
				if(obj != null) {
					request.setAttribute("result", obj);
				}
				requestUrl = requestUrl.replace(".html", "");
				uri = templateUri+requestUrl+extend;
				new JSPForward().doAction(request, response, webCache, staticName, uri);//配置文件中读取
			}
		}
		
	}

	
}
