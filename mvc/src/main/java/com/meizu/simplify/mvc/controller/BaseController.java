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
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.dto.WebCacheInfo;
import com.meizu.simplify.mvc.exception.MappingExceptionResolver;
import com.meizu.simplify.mvc.invoke.IMethodSelector;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.view.IPageTemplate;
import com.meizu.simplify.mvc.view.JsonView;
import com.meizu.simplify.mvc.view.JsonpView;
import com.meizu.simplify.mvc.view.MessageView;
import com.meizu.simplify.mvc.view.RedirectView;
import com.meizu.simplify.mvc.view.TemplateFactory;
import com.meizu.simplify.util.JsonResolver;
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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class BaseController<T extends Model> {
	
	@Resource
	public IPageTemplate template;
	
	@Resource
	private PropertiesConfig config;
	
	@Resource
	private IMethodSelector methodSelector;
	
	@Resource
	private JsonResolver jsonResolver;	
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
	public void process(HttpServletRequest request, HttpServletResponse response, String requestUrl,String cmd)  {
		try {
			Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricTypeForFirst(getClass());//TODO 不要限定class级别的Model范围,可以下放到方法级别
			T model = AnalysisRequestControllerModel.setRequestModel(request,entityClass,cmd);
			if (checkPermission(request, response, model)) {
				execute(request, response, model,requestUrl);
			}
			destroy(request, response, model);
		} catch ( InvocationTargetException e ) {//所有的异常统一在这处理，这是请求处理的最后一关 TODO
			Throwable throwable = e.getTargetException();
			MappingExceptionResolver.resolverException(request, response, requestUrl, this, throwable,config,jsonResolver);
		} catch (BaseException throwable) {//由于在反射优化模式下，不是抛InvocationTargetException异常，而会进入到BaseExceptin及其衍生异常,这里独立处理
			MappingExceptionResolver.resolverException(request, response, requestUrl, this, throwable,config,jsonResolver);
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
	 * 
	 * 方法用途: 安全权限过程检查<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T model) throws ServletException, IOException {
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
		
		String className = this.getClass().getName();
		String methodFullName = className+":"+doCmd;
		//页面静态化名字		
		String staticName = MD5Encrypt.sign(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".ce";

		AnalysisRequestControllerMethod.analysisAjaxAccess(request, response, methodFullName);
		
		WebCacheInfo webCache = AnalysisRequestControllerMethod.analysisWebCache(response, staticName, methodFullName);
		if(webCache.getIsCache()) {
			return;
		}
//		AnalysisRequestControllerMethod.analysisRequestParam(request, model, methodFullName);
		Object[] parameValue = AnalysisRequestControllerMethod.analysisRequestParamByAnnotation(request, model, methodFullName);
		Object obj = methodSelector.invoke(request,response,model,this,doCmd, parameValue);
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

	
}
