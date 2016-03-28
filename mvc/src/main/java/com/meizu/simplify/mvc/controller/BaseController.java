package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.mvc.annotation.AjaxAccess;
import com.meizu.simplify.mvc.annotation.AjaxAccess.Methods;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.model.ModelCharsFilter;
import com.meizu.simplify.mvc.model.ModelScope;
import com.meizu.simplify.mvc.model.ModelSkip;
import com.meizu.simplify.mvc.util.AjaxUtils;
import com.meizu.simplify.mvc.view.ActionForward;
import com.meizu.simplify.mvc.view.IForward;
import com.meizu.simplify.mvc.view.JsonForward;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.ReflectionGenericUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


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
		} else {
			if(obj instanceof String) {
				ActionForward.doAction(request, response, webCache, staticName, String.valueOf(obj));
			}
		}
		
	}

	
}
