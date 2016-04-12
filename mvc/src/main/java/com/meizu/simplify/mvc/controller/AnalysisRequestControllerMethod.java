package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.mvc.annotation.AjaxAccess;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.annotation.AjaxAccess.Methods;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.util.AjaxUtils;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.util.BrowserUtil;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>解析控制器的每个请求映射方法的注解设置</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月25日 下午5:59:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月25日 下午5:59:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AnalysisRequestControllerMethod {
	private static PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
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
	public static boolean analysisWebCache(HttpServletResponse response, String staticName, Method doMethod,WebCache webCache) throws IOException {
		if(config == null) {
			config = BeanFactory.getBean(PropertiesConfig.class);
		}
		if (doMethod.isAnnotationPresent(WebCache.class)) {
			Cache cache = CacheBase.getCache(webCache);
			if(cache != null){
				String cacheContent = cache.readCache(webCache, staticName);
				if(cacheContent != null){
					if(response!=null&&webCache.enableBrowerCache()) {
						BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
					}
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
	public static <T extends Model> Object[] analysisRequestParam(HttpServletRequest request, HttpServletResponse response, T t,Method doMethod) {
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
	public static void analysisAjaxAccess(HttpServletRequest request, HttpServletResponse response, Method doMethod) {
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

}