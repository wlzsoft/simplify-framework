package com.meizu.simplify.mvc.exception;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.dto.JsonResult;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.MessageException;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.view.JsonView;
import com.meizu.simplify.mvc.view.JsonpView;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.webcache.util.BrowserUtil;

/**
 * 
 * <p><b>Title:</b><i>异常解析器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年1月13日 下午3:32:30</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年1月13日 下午3:32:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class MappingExceptionResolver {
	private static final Logger LOGGER = LoggerFactory.getLogger(MappingExceptionResolver.class);
	public MappingExceptionResolver() {
//		通用异常处理的处理映射，提前处理好，用于后面的通过系统级异常的匹配
//		Properties prop = new Properties();
//		prop.setProperty("java.sql.SQLException", "error-sql");
//		prop.setProperty("java.lang.RuntimeException", "error-runtime");
//		setExceptionMappings(prop);
//	          定义在发生异常时视图跟返回码的对应关系
//		addStatusCode(HttpStatus.NOT_FOUND.value(),"404");
//		addStatusCode("error-runtime","502");
//		addStatusCode("error-sql","503");
	}
	
	/**
	 * 
	 * 方法用途: 异常解析<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestUrl
	 * @param bs
	 * @param e
	 */
	public static void resolverException(HttpServletRequest request, HttpServletResponse response, String requestUrl,
			BaseController<?> bs, Throwable throwable,PropertiesConfig config) {
		String exceptionMessage = throwable.getMessage();
		if(exceptionMessage == null) {
			if(throwable.getClass() == NullPointerException.class) {
				exceptionMessage = "空指针异常";
			}
		}
		if(throwable instanceof MessageException) {
			int statuscode = ((BaseException)throwable).getErrorCode();
			response.setStatus(statuscode);
//	                      设置日志输出级别，不定义则默认不输出警告等错误日志信息
//			setWarnLogCategory(MappingExceptionResolver.class.getName());
			if(statuscode==208) {
				LOGGER.info("message:"+exceptionMessage);
			} else if(statuscode == 300) {
				LOGGER.warn("message:"+exceptionMessage);
			} else {
				LOGGER.error("message:"+exceptionMessage);// TODO 分析出业务数据，展现更个性化的日志信息，可以配合  解析LogByMethod的信息
			}
		} else {
			throwable.printStackTrace();
//	                     指定默认的返回码，默认是200
//		    setDefaultStatusCode("500");
			response.setStatus(500);
		}
//			不同请求风格的异常处理-通过请求后缀来处理不同的请求风格的异常视图start
		if(requestUrl.endsWith(".json")) {
			try {
				JsonView.exe(request, response, JsonResult.error(exceptionMessage),config);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		} else if(requestUrl.endsWith(".jsonp")){
			response.setStatus(208);//特殊情况下，5xx和4xx的状态状态码jsonp是无法处理的，由于不是真的ajax(jQuery框架的实现,自己模拟实现更精细的控制)，使用208来代替错误状态
			try {
				Model model = new Model() {
					@Override
					public void setScript(Integer script) {
						super.setScript(script);
					}
					@Override
					public void setCallback(String call) {
						super.setCallback(call);
					}
					
				};
				model.setScript(DataUtil.parseInt(request.getParameter("script")));
				model.setCallback(request.getParameter("callback"));
				JsonpView.exe(request, response, JsonResult.error(exceptionMessage),model,"meizu.com",config);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		} else {//其他方式的请求,都走html业务视图，可以支持jsp,velocity 等模板引擎
			try {
//	                                方法一
//				ErrorView.exe(response,500,throwable.getMessage());
//				方法二：推荐
//				定义异常处理页面用来获取异常信息的变量名，默认名为exception
//				setExceptionAttribute("exception");
				request.setAttribute("exception", throwable);
				//没有在exceptionMappings里面找到对应的异常时 返回defaultErrorView指定的异常处理默认视图:500,404,403在这里其实jsp页面，比如500.jsp，400.jsp，exception.jsp
//				setDefaultErrorView("500");//exception
				bs.template.render(request, response, null, null, "500");
			} catch (ServletException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	    //非200状态的请求全禁用缓存。
        BrowserUtil.disableBrowerCache(response);
//		不同请求风格的异常处理end
	}

}
