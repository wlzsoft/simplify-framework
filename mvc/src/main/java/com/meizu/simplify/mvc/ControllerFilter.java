package com.meizu.simplify.mvc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dto.JsonResult;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.mvc.resolver.ControllerAnnotationResolver;
import com.meizu.simplify.mvc.view.IForward;
import com.meizu.simplify.mvc.view.JsonForward;
import com.meizu.simplify.mvc.view.VelocityForward;
import com.meizu.simplify.utils.StringUtil;




/**
 * <p><b>Title:</b><i>controller过滤器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:15</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@WebFilter(urlPatterns="/*",dispatcherTypes={DispatcherType.REQUEST},filterName="ControllerFilter")
public class ControllerFilter implements Filter {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerFilter.class);
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String thisUrl = request.getRequestURI().substring(request.getContextPath().length());// 当前地址
		while ( thisUrl.indexOf("//") != -1 ) {
			thisUrl = thisUrl.replace("//", "/"); // 防止用户避过正则
		}
//		if (StringUtil.parseString(request.getQueryString(),"").length() > 0) {
//			thisUrl += "?" + request.getQueryString();
//	 	}
		ControllerAnnotationInfo<BaseController<?>> controllerAnnotationInfo = ControllerAnnotationResolver.controllerMap.get(thisUrl);
		if(controllerAnnotationInfo !=null) {
			analysisAndProcess(request, response, thisUrl, controllerAnnotationInfo, null);
			return;// [标示]启用原生 filter的chain,三个地方同时打开注释
		} else {
			//TODO: 提供快速查找的算法，可以key的string转成整型，然后比较整型,不过由于正则无法确定具体值的访问，所以也没法比较
			for ( String key : ControllerAnnotationResolver.controllerMap.keySet() ) {
				if(!key.contains("$")) {
					continue;
				}
				Pattern pattern = Pattern.compile("^" + key);
				Matcher matcher = pattern.matcher(thisUrl);
				if (matcher.find()) {
					String[] params = new String[matcher.groupCount() + 1];
					for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
					controllerAnnotationInfo = ControllerAnnotationResolver.controllerMap.get(key);
					analysisAndProcess(request, response, thisUrl,controllerAnnotationInfo, params);
					return; // [标示]启用原生 filter的chain,三个地方同时打开注释
				}
			}
		}
		/*注意：匹配不上正则，匹配不上action，会走filterchain,大部分情况是不会执行chain,
		如果有多个filter的情况，就要注意，如果这个地方注释掉，可能导致后续的filter不会执行
		[jsp和servlet会走这个chain，不支持直接访问servlet和jsp，可以整个屏蔽掉，jsp还是可以访问，直接会返回200的空白页]
		[会屏蔽掉资源文件的访问，比如js，png等图片等等]*/
		if(chain != null) {
			chain.doFilter(req, res);// [标示]启用原生 filter的chain,三个地方同时打开注释
		}
	}
	
	/**
	 * 
	 * 方法用途: 分析请求地址最终调用分发处理业务<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestUrl
	 * @param key
	 * @param params
	 */
	private void analysisAndProcess(HttpServletRequest request, HttpServletResponse response, String requestUrl,ControllerAnnotationInfo<BaseController<?>> controllerAnnotationInfo,
			String[] params) {
		
		try {
			long time = System.currentTimeMillis();
			
			Statistics.getReadMap().put(requestUrl, 0);
			
			request.setAttribute("params", params);
			String parName = controllerAnnotationInfo.getMethod();
			request.setAttribute("cmd", parName);//请求指令，其实就是controller请求方法名
			BaseController<?> bs = controllerAnnotationInfo.getObj();
			bs.process(request, response,requestUrl);
			long readtime = System.currentTimeMillis() - time;
			LOGGER.debug(StringUtil.format("{0} 耗时:{1}毫秒", requestUrl, (readtime)));
			
			// 记录统计信息
			Statistics.incReadcount();
			Statistics.setReadMaxTime(readtime, requestUrl);
			Statistics.getReadMap().remove(requestUrl);
		} catch ( InvocationTargetException e ) {//所有的异常统一在这处理，这是请求处理的最后一关 TODO
			Throwable throwable = e.getTargetException();
			throwable.printStackTrace();
			String exceptionMessage = throwable.getMessage();
			if(exceptionMessage == null) {
				if(throwable.getClass() == NullPointerException.class) {
					exceptionMessage = "空指针异常";
				}
			}
			response.setStatus(500);
//			不同请求风格的异常处理-通过请求后缀来处理不同的请求风格的异常视图start
			if(requestUrl.endsWith(".json")) {
				try {
					JsonForward.doAction(request, response, null, null, JsonResult.error(exceptionMessage));
				} catch (ServletException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if(requestUrl.endsWith(".xml")){//不实现
				
			} else {//其他方式的请求,都走html业务视图，可以支持jsp,velocity 等模板引擎
//	                                方法一
				//response.sendError(500,throwable.getMessage());
				
//				方法二：推荐
				
				/*String page500 = "<!DOCTYPE html>"+
								 "<html>"+
								 "<head>"+
								 "<meta charset=\"UTF-8\">"+
								 "<title>500 错误</title>"+
								 "</head>"+
								 "<body>"+
								 "<!--"+throwable+"-->"+
								 "哎，出了点问题，先逛逛其他功能，或是联系管理员" +
								"</body>"+
								"</html>";*/
				
				try {
					VelocityForward.doAction(request, response, null, null, "/template/framework/500.html");
				} catch (ServletException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
//				方法三
//				RequestDispatcher requestDispatcher = request.getRequestDispatcher("500");
//				requestDispatcher.forward(request, response);
			}
//			不同请求风格的异常处理end

		} catch (IllegalAccessException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
