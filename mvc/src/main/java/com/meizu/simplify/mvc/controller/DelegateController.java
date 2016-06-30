package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.encrypt.sign.md5.MD5Encrypt;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.dto.WebCacheInfo;
import com.meizu.simplify.mvc.exception.MappingExceptionResolver;
import com.meizu.simplify.mvc.invoke.IMethodSelector;
import com.meizu.simplify.mvc.invoke.IModelSelector;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.resolver.ControllerAnnotationResolver;
import com.meizu.simplify.mvc.view.IPageTemplate;
import com.meizu.simplify.mvc.view.JsonView;
import com.meizu.simplify.mvc.view.JsonpView;
import com.meizu.simplify.mvc.view.MessageView;
import com.meizu.simplify.mvc.view.RedirectView;
import com.meizu.simplify.mvc.view.TemplateFactory;
import com.meizu.simplify.util.JsonResolver;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>委托控制器</i></p>
 * <p>Desc: IBaseController的委托实现，不在具体IBaseController实现类中处理具体逻辑,需要优化重构，后续和BaseController整合减少重复代码</p>
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
@Bean
public class DelegateController<T extends Model> implements IBaseController<T> {
	
	@Resource
	private IPageTemplate template;
	
	@Resource
	private PropertiesConfig config;
	
	@Resource
	private IMethodSelector methodSelector;
	
	@Resource
	private IModelSelector modelSelector;
	
	@Resource
	private JsonResolver jsonResolver;	
	/**
	 * 
	 * 方法用途: 拦截处理所有请求<br>
	 * 操作步骤: TODO<br>
	 * @param req
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
	public void process(HttpServletRequest request, HttpServletResponse response, String requestUrl,String requestMethodName,String[] urlparams, IBaseController<T> iBaseController)  {
		try {
//			Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricTypeForFirst(getClass());//TODO 不要限定class级别的Model范围,可以下放到方法级别
			@SuppressWarnings("unchecked")
			Class<T> entityClass = (Class<T>) ControllerAnnotationResolver.pojoParamMap.get(iBaseController.getClass().getName()+":"+requestMethodName);
			T model = null;
			if(entityClass != null) {
				model = modelSelector.setRequestModel(request, entityClass);
				model = AnalysisRequestControllerModel.setBaseModel(entityClass, urlparams, model);
			}
			if (iBaseController.checkPermission(request, response,requestMethodName, model)) {
				execute(request, response,requestMethodName, model,requestUrl,iBaseController);
			}
			destroy(request, response, model);
		} catch ( InvocationTargetException e ) {//所有的异常统一在这处理，这是请求处理的最后一关 TODO
			Throwable throwable = e.getTargetException();
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, throwable,config,jsonResolver);
		} catch (BaseException throwable) {//由于在反射优化模式下，不是抛InvocationTargetException异常，而会进入到BaseExceptin及其衍生异常,这里独立处理
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, throwable,config,jsonResolver);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
		
	}

	/**
	 * 
	 * 方法用途: controller 注销相关处理<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param model
	 */
	public void destroy(HttpServletRequest request, HttpServletResponse response, T model){
//			System.out.println("回收数据库连接到连接池中");
	}
	
	/**
	 * 方法用途: 执行逻辑<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestMethodName
	 * @param model
	 * @param requestUrl
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws ServletException
	 */
	public void execute(HttpServletRequest request, HttpServletResponse response,String requestMethodName, T model,String requestUrl, IBaseController<?> iBaseController) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException  {
		if (requestMethodName == null || requestMethodName.length() <= 0) {
			return;
		}
		String className = iBaseController.getClass().getName();
		String methodFullName = className+":"+requestMethodName;
		//页面静态化名字		
		String staticName = MD5Encrypt.sign(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".ce";

		AnalysisRequestControllerMethod.analysisAjaxAccess(request, response, methodFullName);
		
		WebCacheInfo webCache = AnalysisRequestControllerMethod.analysisWebCache(response, staticName, methodFullName);
		if(webCache.getIsCache()) {
			return;
		}
//		AnalysisRequestControllerMethod.analysisRequestParam(request, model, methodFullName);
		Object[] parameValue = AnalysisRequestControllerMethod.analysisRequestParamByAnnotation(request, model, methodFullName);
		Object obj = null;
		if(requestMethodName.equals("exec")) {
			obj = iBaseController.exec(request,response);
		} else {
			obj = methodSelector.invoke(request,response,model,iBaseController,requestMethodName, parameValue);
		}
		dispatchView(request, response, model, requestUrl, staticName, obj, webCache.getWebcache());
		
	}
	
	/**
	 * 
	 * 方法用途: 转发到指定视图解析，并输出到浏览器<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param model
	 * @param requestUrl
	 * @param staticName
	 * @param method
	 * @param parameValue
	 * @param webCache
	 * @throws IllegalAccessException
	 * @throws ServletException
	 * @throws IOException
	 */
	private void dispatchView(HttpServletRequest request, HttpServletResponse response, T model, String requestUrl,
			String staticName, Object obj, WebCache webCache)
					throws IllegalAccessException, ServletException, IOException {
		if(requestUrl.endsWith(".json")) {
			JsonView.exe(request, response, obj,config,jsonResolver);
		} else if(requestUrl.endsWith(".jsonp")) {
			JsonpView.exe(request, response, obj,model,"meizu.com",config,jsonResolver);
		} else {
			request.setAttribute("formData", model);
			String reactive = getDeviceInfo(request);
			String resolution = request.getHeader("resolution");//800x600
			if(resolution != null) {//判断分辨率
				reactive+=resolution;
			}
			if(obj != null && obj instanceof String) {//尽量避免instanceof操作，后续这里要优化
				String uri = String.valueOf(obj);
				String[] uriArr = uri.split(":");
				String templateType = uriArr[0];
				String templateUrl = "";
				if(uriArr.length>1) {
					templateUrl = uriArr[1];
				}
				templateUrl += reactive;
				switch (templateType) {
					case "uri":
						template.render(request, response, webCache, staticName, templateUrl);
						break;
					case "redirect":
						RedirectView.exe(request, response, webCache, staticName, templateUrl);
						break;
					default :
						//messageView和ITemplate的综合处理不优雅，并且会导致一个大的文本对象用于匹配Template的key值 TODO
						IPageTemplate temp = TemplateFactory.getTemplate(templateType);
						if(temp != null) {
							temp.render(request, response, webCache, staticName, templateUrl);
						} else {
							MessageView.exe(request, response, uri,config);
						}
				}
			} else {
				if(obj != null) {
					request.setAttribute("result", obj);
				}
				requestUrl = requestUrl.replace(".html", "");
				requestUrl += reactive;
				template.render(request, response, webCache, staticName, requestUrl);
			}
		}
	}

	/**
	 * 
	 * 方法用途: 判断设备类型<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @return
	 */
	private String getDeviceInfo(HttpServletRequest request) {
		String device = request.getHeader("User-Agent");
		if(device != null) {//判断设备
			 if(device.contains("Pad")){
				return "Pad";
			} else if(device.contains("Mobile")) {
				return "Mobile";
			} else if(device.contains("ndroid")) {
				return "Pad";
			}
		}
		return "";
	}
	/**
	 * 永远不会被调用，无效的方法，由于实现接口的关系，没办法不实现一个空接口
	 */
	@Override
	public Object exec(HttpServletRequest request, HttpServletResponse response) {
		return null;
	}
}