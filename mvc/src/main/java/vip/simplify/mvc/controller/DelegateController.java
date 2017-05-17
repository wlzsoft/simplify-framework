package vip.simplify.mvc.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.config.info.MessageThreadLocal;
import vip.simplify.encrypt.sign.md5.MD5Encrypt;
import vip.simplify.exception.BaseException;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.mvc.dto.WebCacheInfo;
import vip.simplify.mvc.exception.MappingExceptionResolver;
import vip.simplify.mvc.invoke.IMethodSelector;
import vip.simplify.mvc.invoke.IModelSelector;
import vip.simplify.mvc.model.Model;
import vip.simplify.mvc.resolver.ControllerAnnotationResolver;
import vip.simplify.mvc.view.JsonView;
import vip.simplify.mvc.view.JsonpView;
import vip.simplify.mvc.view.RedirectView;
import vip.simplify.mvc.view.TemplateFactory;
import vip.simplify.util.JsonResolver;
import vip.simplify.utils.StringUtil;
import vip.simplify.view.IPageTemplate;
import vip.simplify.view.MessageView;
import vip.simplify.webcache.annotation.WebCache;


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
	
	@Inject
	private IPageTemplate template;
	
	@Inject
	private PropertiesConfig config;
	
	@Inject
	private IMethodSelector methodSelector;
	
	@Inject
	private IModelSelector modelSelector;
	
	@Inject
	private JsonResolver jsonResolver;	
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
	public void process(HttpServletRequest request, HttpServletResponse response, String requestUrl,String requestMethodName,boolean isStatic,String[] urlparams, IBaseController<T> iBaseController)  {
		try {
//			Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricTypeForFirst(getClass());//TODO 不要限定class级别的Model范围,可以下放到方法级别
			@SuppressWarnings("unchecked")
			Class<T> entityClass = (Class<T>) ControllerAnnotationResolver.pojoParamMap.get(iBaseController.getClass().getName()+":"+requestMethodName);
			T model = null;
			if(entityClass != null) {
				model = modelSelector.setRequestModel(request, entityClass);
				model = AnalysisRequestControllerModel.setBaseModel(entityClass, urlparams, model);
			}
			Throwable throwable = null;
			if (iBaseController.checkPermission(request, response,requestMethodName,requestUrl, model)) {
				throwable = execute(request, response,requestMethodName,isStatic, model,requestUrl,iBaseController);
			}
			end(request, response, model,requestUrl,throwable);
		} catch ( InvocationTargetException e ) {//所有的异常统一在这处理，这是请求处理的最后一关
			Throwable throwable = e.getTargetException();
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, throwable, config, jsonResolver, config.getDomain());
		} catch (BaseException throwable) {//由于在反射优化模式下，不是抛InvocationTargetException异常，而会进入到BaseExceptin及其衍生异常,这里独立处理
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, throwable, config, jsonResolver, config.getDomain());
		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		} catch(Exception ex) {
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, ex,config,jsonResolver, config.getDomain());
		}
		
	}
	
	/**
	 * 
	 * 方法用途: 处理请求的收尾工作<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param model
	 */
	public void end(HttpServletRequest request, HttpServletResponse response, T model,String requestUrl,Throwable throwable){
//		System.out.println("回收数据库连接到连接池中");
		if(throwable!= null) {
			MappingExceptionResolver.resolverException(request, response, requestUrl, template, throwable, config, jsonResolver, config.getDomain());
			MessageThreadLocal.threadLocal.remove();
		}
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
	public Throwable execute(HttpServletRequest request, HttpServletResponse response, String requestMethodName, boolean isStatic, T model, String requestUrl, IBaseController<?> iBaseController) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ServletException  {
		if (requestMethodName == null || requestMethodName.length() <= 0) {
			return null;
		}
		String className = iBaseController.getClass().getName();
		String methodFullName = className+":"+requestMethodName;
		//页面静态化名字		
		String staticName = MD5Encrypt.sign(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".ce";

		AnalysisRequestControllerMethod.analysisAjaxAccess(request, response, methodFullName);
		
		WebCacheInfo webCache = AnalysisRequestControllerMethod.analysisWebCache(response, staticName, methodFullName);
		if(webCache.getIsCache()) {
			return null;
		}
//		AnalysisRequestControllerMethod.analysisRequestParam(request, model, methodFullName);
		Object[] parameValue = AnalysisRequestControllerMethod.analysisRequestParamByAnnotation(request, model, methodFullName);
		Object obj = null;
		if(isStatic) {
			obj = requestMethodName;
		} else {
			if(requestMethodName.equals("exec")) {
				obj = iBaseController.exec(request,response);
			} else {
				obj = methodSelector.invoke(request,response,model,iBaseController,requestMethodName, parameValue);
			}
		}
		Throwable throwable = MessageThreadLocal.threadLocal.get();
		if(throwable==null) {
			dispatchView(request, response, model, requestUrl, staticName, obj, webCache.getWebcache());
		}
		return throwable;
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
	 * @param obj
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
		} else if(requestUrl.endsWith(".stream")) {
			//1.支持通用http在浏览器中可以解析的文件流，
			//2.支持自定义的文件流，通过http协议传输，浏览器无法解析，可用于服务端与服务端通讯，采用httpclient的方式，比如rest协议的客户端，虽然建议使用类dubbo的协议，但一些特殊情况，或是为了简单，而不会采用，比如android端。
			if(obj instanceof byte[]) {//支持小文件等小数据流的操作，一次性write和flush到客户端
				ServletOutputStream os = response.getOutputStream();
				os.write((byte[])obj);
				os.flush();
				os.close();
			} else if(obj instanceof InputStream) {//支持大文件下载，每次读取流中的一部分字节，然后写出并flush到客户端
				ServletOutputStream os = response.getOutputStream();
				InputStream is = (InputStream)obj;
				byte[] b = new byte[config.getStreamChunkBuffer()];
				while(is.read(b)!=-1) {
					os.write(b);
					os.flush();
				}
				os.close();
//				is.close(); //TODO 是否需要关闭输入流，待分析确定
			} else {
				throw new UncheckedException("不支持解析的stream的操作对象");
			}
		} else {
			request.setAttribute("formData", model);
			String reactive = getDeviceInfo(request);
			String resolution = request.getHeader("resolution");//800x600
			if(resolution != null) {//判断分辨率
				reactive+=resolution;
			}
			if(obj != null && obj instanceof String) {//尽量避免instanceof操作，后续这里要优化
				String uri = String.valueOf(obj);
				String[] uriArr = uri.split(":",2);//多个http date：20160811
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
