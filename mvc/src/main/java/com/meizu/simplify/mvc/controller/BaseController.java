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
	
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	public void init() {}
	
	/**
	 * 
	 * 方法用途: 拦截处理所有请求<br>
	 * 操作步骤: TODO<br>
	 * @param req
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void process(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		HttpServletRequest request = req;
		T model = setRequestModel(request);
		
		//页面静态化名字		
		String staticName = MD5Encrypt.sign(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".lv";
		
		if (checkPermission(request, response, model)) {
			execute(request, response, model,staticName);
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
	 * @param cacheSet 
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response, T model, String staticName) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException  {
//		RequestAnalysisWrapper.java
		if (model.getCmd() == null || model.getCmd().length() <= 0) {
			return;
		}
		String doCmd = model.getCmd();
		Method[] methods = this.getClass().getMethods();
		Method doMethod = CollectionUtil.getItem(methods,doCmd, (m,w) -> doCmd.equals(m.getName()));
		if (doMethod == null) {
			throw new IllegalArgumentException("The method named, " + doCmd + ", is not specified by " + this.getClass()); 
		}
		if (doMethod.getParameterTypes().length < 3) { //考虑model问题，后续可以做更灵活调整
			throw new IllegalArgumentException("类:["+this.getClass()+"] 的方法 :[" + doCmd + "]的参数的长度不能小于3" ); 
		}

		Object[] parameValue = analysisRequestParam(request, response, model, doMethod);
		
		analysisAjaxAccess(request, response, doMethod);
		
		WebCache webCache = doMethod.getAnnotation(WebCache.class);
		boolean isCache = analysisWebCache(response, staticName, doMethod,webCache);
		if(isCache) {
			return;
		}
		
		IForward result = null;
		if(doMethod.getReturnType() == IForward.class) {
			result = (IForward) doMethod.invoke(this, parameValue);
		} else {
//			if(thisUrl.endsWith(".json")) {
				Object obj = doMethod.invoke(this,parameValue);
				result = new JsonForward(obj);
//			}
		}
		
		if (result != null) {
			request.setAttribute("formData", model);
			result.doAction(request, response, webCache, staticName);
		}
	}

	/**
	 * 
	 * 方法用途: 解析页面缓存配置及返回缓存页面<br>
	 * 操作步骤: TODO<br>
	 * @param response
	 * @param staticName
	 * @param doMethod
	 * @param webCache
	 * @return
	 * @throws IOException
	 */
	private boolean analysisWebCache(HttpServletResponse response, String staticName, Method doMethod,WebCache webCache) throws IOException {
		if (doMethod.isAnnotationPresent(WebCache.class)) {
			Cache cache = CacheBase.getCache(webCache);
			if(cache != null){
				String cacheContent = cache.readCache(webCache, staticName,response);
				if(cacheContent != null){
					response.setCharacterEncoding(config.getCharset());
					response.setContentType("text/html; charset=" + config.getCharset());
					response.getWriter().print(cacheContent);
					System.out.println("页面缓存 : 读取页面缓存");
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * 方法用途: 设置参数值<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param t
	 * @param doMethod
	 * @return
	 */
	private Object[] analysisRequestParam(HttpServletRequest request, HttpServletResponse response, T t,Method doMethod) {
		Object[] parameValue = new Object[doMethod.getParameterTypes().length];
		parameValue[0] = request;
		parameValue[1] = response;
		parameValue[2] = t;
		for ( int i = 3; i < doMethod.getParameterTypes().length; i++ ) {
			for ( int j = 0; j < doMethod.getParameterAnnotations()[i].length; j++ ) {
				if (doMethod.getParameterAnnotations()[i][j].annotationType() == RequestParam.class) {
					parameValue[i] = null;
					RequestParam requestParam = ((RequestParam) doMethod.getParameterAnnotations()[i][j]);
					int index = requestParam.index();
					String name = requestParam.name();
					String defaultValue = ((RequestParam) doMethod.getParameterAnnotations()[i][j]).defaultValue();
					defaultValue = "null".equals(defaultValue) ? null : defaultValue;
					Object value = null;
					
					if (!StringUtil.isEmpty(request.getParameter(name))) {
						value = request.getParameter(name);
					} else if (ObjectUtil.isInt(index)) {
						if(t.getParams() != null && t.getParams().length > 0 && index < t.getParams().length) {
							value = t.getParams()[index];
						}
					} else {
						value = defaultValue;
					}

					if (value == null) {
						break;
					}

					// 将值进行格式化后注入
					parameValue[i] = DataUtil.convertType(doMethod.getParameterTypes()[i], value.toString());
				}
			}
		}
		return parameValue;
	}

	/**
	 * 
	 * 方法用途: 检查并设置Ajax跨域配置<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param doMethod
	 */
	private void analysisAjaxAccess(HttpServletRequest request, HttpServletResponse response, Method doMethod) {
		if(!AjaxUtils.isAjaxRequest(request)) {
			return;
		}
		if (!doMethod.isAnnotationPresent(AjaxAccess.class)) {
			return;
		}
		AjaxAccess ajaxAccess = doMethod.getAnnotation(AjaxAccess.class);
		if (ajaxAccess == null) {
			return;
		}
		if (!StringUtil.isEmpty(ajaxAccess.allowOrigin())) {
			response.addHeader("Access-Control-Allow-Origin", ajaxAccess.allowOrigin());
		}
		if (!StringUtil.isEmpty(ajaxAccess.allowHeaders())) {
			response.addHeader("Access-Control-Allow-Headers", ajaxAccess.allowHeaders());
		}
		response.addHeader("Access-Control-Max-Age", String.valueOf(ajaxAccess.maxAge()));
		if (ajaxAccess.allowMethods() != null) {
			StringBuffer allowMethodSb = new StringBuffer();
			for (Methods method : ajaxAccess.allowMethods()) {
				allowMethodSb.append(method.name()).append(",");
			}
			if (allowMethodSb.length() > 0) {
				allowMethodSb.delete(allowMethodSb.length() - 1, allowMethodSb.length());
			}
			response.addHeader("Access-Control-Allow-Methods", allowMethodSb.toString());
		}
	}

	/**
	 * 
	 * 方法用途: 获取表单数据<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @return
	 */
	private T setRequestModel(HttpServletRequest request) {
		try {
			Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricType(getClass(),0);
			T model = entityClass.newInstance();
			for ( Method method : entityClass.getMethods() ) {
				if (method != null && method.getName().indexOf("set") == 0) {
					int parLength = method.getParameterTypes().length;
					if (parLength != 1) continue;
					Class<?> type = method.getParameterTypes()[0];
					String parName = method.getName().substring(3, method.getName().length());
					String par = request.getParameter(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));

					par = analysisModelScope(method, par);
					
					par = analysisModelCharsFilter(method, par);

					// 是否滤过
					if (method.isAnnotationPresent(ModelSkip.class)) {
						continue;
					}

					// 取消兼容方式
					// par = par == null ? request.getParameter(parName) : par;
					if (par != null) {
						Method method2 = entityClass.getMethod(method.getName(), new Class[] { type });
						// 将值进行格式化后注入
						if (type.isArray()) {
							String[] pars = request.getParameterValues(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
							if (pars != null && pars.length == 1) pars = pars[0].split(",");
							method2.invoke(model, new Object[] { DataUtil.convertType(type, pars) });
						} else {
							method2.invoke(model, new Object[] { DataUtil.convertType(type, par) });
						}
					}
				}
			}
			Method method = entityClass.getMethod("setParams", new Class[] { String[].class });
			Object paramObj = request.getAttribute("params");
			if(paramObj != null) {
				String[] params = (String[])paramObj;
				method.invoke(model, new Object[] { params });
			} else {
				method.invoke(model, new Object[] { null });
			}

			String cmd = (String) request.getAttribute("cmd");
			if (cmd != null) {
				method = entityClass.getMethod("setCmd", new Class[] { String.class });
				method.invoke(model, new Object[] { cmd });
			}
			return model;
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
	}

	/**
	 * 
	 * 方法用途: 解析表单属性值的非法字符过滤清除的设置<br>
	 * 操作步骤: TODO<br>
	 * @param method
	 * @param par
	 * @return
	 */
	private String analysisModelCharsFilter(Method method, String par) {
		ModelCharsFilter stringFilter = null;
		if (method.isAnnotationPresent(ModelCharsFilter.class)) {
			stringFilter = (ModelCharsFilter) method.getAnnotation(ModelCharsFilter.class);
			if(stringFilter.filters() != null){
				for(ModelCharsFilter.Filter filter : stringFilter.filters()){
					if(ModelCharsFilter.Filter.Html == filter){
						par = StringUtil.removeHtmlLabel(par);
					} else if(ModelCharsFilter.Filter.Script == filter){
						par = StringUtil.removeScript(par);
					} else if(ModelCharsFilter.Filter.Style == filter){
						par = StringUtil.removeStyle(par);
					} else if(ModelCharsFilter.Filter.iframe == filter){
						par = StringUtil.removeIframe(par);
					} else if(ModelCharsFilter.Filter.trim == filter){
						par = StringUtil.removeHtmlSpace(par);
					}
				}
			}
		}
		return par;
	}

	/**
	 * 
	 * 方法用途: 解析表单属性作用域的设置<br>
	 * 操作步骤: TODO<br>
	 * @param method
	 * @param par
	 * @return
	 */
	private String analysisModelScope(Method method, String par) {
		ModelScope mset = null;
		if (method.isAnnotationPresent(ModelScope.class)) {
			mset = (ModelScope) method.getAnnotation(ModelScope.class);
			if(mset.scope() != null){
				if(ModelScope.Scope.session == mset.scope()){
					
				} else if(ModelScope.Scope.cookie == mset.scope()){
					
				} else if(ModelScope.Scope.application == mset.scope()){
					
				}
			}
			if(mset.charset() != null && mset.charset().length() > 0) {
				par = StringUtil.coding(par, mset.charset());
			}
		}
		return par;
	}
}
