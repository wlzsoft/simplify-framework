package vip.simplify.mvc.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.dto.AnnotationInfo;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.mvc.annotation.AjaxAccess;
import vip.simplify.mvc.annotation.AjaxAccess.Methods;
import vip.simplify.mvc.annotation.RequestParam;
import vip.simplify.mvc.dto.AnnotationListInfo;
import vip.simplify.mvc.dto.WebCacheInfo;
import vip.simplify.mvc.model.Model;
import vip.simplify.mvc.resolver.ControllerAnnotationResolver;
import vip.simplify.mvc.util.AjaxUtil;
import vip.simplify.utils.DataUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.webcache.annotation.WebCache;
import vip.simplify.webcache.util.BrowserUtil;
import vip.simplify.webcache.web.Cache;
import vip.simplify.webcache.web.CacheBase;


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
	 * @param methodFullName
	 * @return 如果返回空，那么是有缓存，否则无缓存
	 * @throws IOException
	 */
	public static WebCacheInfo analysisWebCache(HttpServletResponse response, String staticName, String methodFullName) throws IOException {
		if(config == null) {
			config = BeanFactory.getBean(PropertiesConfig.class);
		}
		WebCacheInfo webCacheInfo = new WebCacheInfo();
		AnnotationInfo<WebCache> webCacheAnno = ControllerAnnotationResolver.webCacheMap.get(methodFullName);
		if (webCacheAnno == null) {
//			Message.error("严重错误，无法获取["+methodFullName+"]的WebCache注解信息");
			webCacheInfo.setIsCache(false);
			return webCacheInfo;
		}
		WebCache webCache = webCacheAnno.getAnnotatoionType();
		webCacheInfo.setWebcache(webCache);
		Cache cache = CacheBase.getCache(webCache);
		if(cache == null){
			webCacheInfo.setIsCache(false);
			return webCacheInfo;
		}
		String cacheContent = cache.readCache(webCache, staticName);
		if(cacheContent == null){
			webCacheInfo.setIsCache(false);
			return webCacheInfo;
		}
		if(response!=null&&webCache.enableBrowerCache()) {
//			 TODO cache-control max-age 0    需要验证是否google浏览器刷新，都会强制带这个标识，去检测下，只是通过浏览器的banner导航，才会使用缓存，因为浏览器的刷新，就是为了检测服务器是否有新内容
			BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
		}
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
		response.getWriter().print(cacheContent);
		System.out.println("页面缓存 : 读取页面缓存");
		webCacheInfo.setIsCache(true);
		return webCacheInfo;
	}
	/**
	 * 
	 * 方法用途: 设置参数值<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param model
	 * @param methodFullName
	 * @return
	 */
	public static <T extends Model> Object[] analysisRequestParam(HttpServletRequest request, T model, String methodFullName) {
		return null;
	}
	/**
	 * 
	 * 方法用途: 设置参数值<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param model
	 * @param methodFullName
	 * @return
	 */
	public static <T extends Model> Object[] analysisRequestParamByAnnotation(HttpServletRequest request, T model,String methodFullName) {
		AnnotationListInfo<AnnotationInfo<RequestParam>> annoListInfo = ControllerAnnotationResolver.requestParamMap.get(methodFullName);
		if(annoListInfo == null) {
			//TODO 后续如果需要兼容没有参数的情况下，就不会是严重错误，而是更好的用户体验
//			Message.error("严重错误，无法获取["+methodFullName+"]的RequestParam注解信息");
			System.err.println("严重错误，无法获取["+methodFullName+"]的RequestParam注解信息");//提供lambada表达式的请求映射，修改为这个方式，后续考虑调整
			return null;
		}
		int methodParamLength = annoListInfo.getCount();
		Object[] parameValue = new Object[methodParamLength];
		List<AnnotationInfo<RequestParam>> annoInfoList = annoListInfo.getAnnoList();
		if(annoInfoList == null) {
			return parameValue;
		}
		for (int i = 0; i < annoInfoList.size(); i++) {
			AnnotationInfo<RequestParam> annoInfo = annoInfoList.get(i);
			RequestParam requestParam = annoInfo.getAnnotatoionType();
			if(requestParam == null) {
				continue;
			}
			int index = requestParam.index();
			String name = requestParam.name();
			String defaultValue = requestParam.defaultValue();
			defaultValue = "null".equals(defaultValue) ? null : defaultValue;
			Object value = null;
			String paramValue = request.getParameter(name);
			if (!StringUtil.isEmpty(paramValue)) {//表单参数获取并设置,格式 http://url/?a=1&b=2，获取参数1和2
				value = paramValue;
//				if (!"json"&&!"stream"!"jsonp")//不满足这些视图才需要做以下设置，只有模版引擎才需要用到 TODO 后续独立多视图功能需要重构这里,目前使用判断方式不够优雅
				request.setAttribute(name,value);//提供表单注解获取功能 检测到会和formData冲突，不建议formData.xxx这种写法
			} else if (index>0) {//url的rest风格的参数获取并设置,格式 http://url/1/2  获取参数1和2
				if(model!=null && model.getParams() != null && model.getParams().length > 0 && index < model.getParams().length) {
					value = model.getParams()[index];
				}
			} else {
				value = defaultValue;
			}
			if (value == null) {
				continue;
			}
			// 将值进行格式化后注入
			parameValue[i+3] = DataUtil.convertType(annoInfo.getReturnType(), value.toString(),false);
		}
		return parameValue;
	}

	/**
	 * 
	 * 方法用途: 检查并设置Ajax跨域配置<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param methodFullName
	 */
	public static void analysisAjaxAccess(HttpServletRequest request, HttpServletResponse response, String methodFullName) {
		/*注意：大部分的ajax请求都可以在服务端使用这个头信息判断，但是由于这个头信息不是标准规范，不一定所有浏览器和js库都会支持这个头信息，或是不使用这个名字的头标记
		 * 所以框架中，要强制要求，所有使用ajax的请求，都必须有X-Requested-With头标记，如果没有，需要强制加上
		*/
		if(!AjaxUtil.isAjaxRequest(request)) {
			return;
		}
		AnnotationInfo<AjaxAccess> ajaxAccessAnno = ControllerAnnotationResolver.ajaxAccessMap.get(methodFullName);
		if (ajaxAccessAnno == null) {
//			Message.error("严重错误，无法获取["+methodFullName+"]的AjaxAccess注解信息");
			return;
		}
		AjaxAccess ajaxAccess = ajaxAccessAnno.getAnnotatoionType();
		if (!StringUtil.isEmpty(ajaxAccess.allowOrigin())) {
			response.addHeader("Access-Control-Allow-Origin", ajaxAccess.allowOrigin());
		}
		if (!StringUtil.isEmpty(ajaxAccess.allowHeaders())) {
			response.addHeader("Access-Control-Allow-Headers", ajaxAccess.allowHeaders());
		}
		if (ajaxAccess.allowCredentials()) {
			response.addHeader("Access-Control-Allow-Credentials", String.valueOf(ajaxAccess.allowCredentials()));
		}
		response.addHeader("Access-Control-Max-Age", String.valueOf(ajaxAccess.maxAge()));
		if (ajaxAccess.allowMethods() != null) {
			StringBuffer allowMethodSb = new StringBuffer();
			Methods[] allowMethodArr = ajaxAccess.allowMethods();
			for (int i=0; i < allowMethodArr.length; i++) {
				Methods method = allowMethodArr[i];
				allowMethodSb.append(method.name()).append(",");
			}
			if (allowMethodSb.length() > 0) {
				allowMethodSb.delete(allowMethodSb.length() - 1, allowMethodSb.length());
			}
			response.addHeader("Access-Control-Allow-Methods", allowMethodSb.toString());
		}
	}

}