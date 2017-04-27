package vip.simplify.mvc.exception;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.dto.HttpStatusEnum;
import vip.simplify.dto.ResultFactory;
import vip.simplify.exception.BaseException;
import vip.simplify.exception.MessageException;
import vip.simplify.exception.UncheckedException;
import vip.simplify.mvc.model.Model;
import vip.simplify.mvc.view.JsonView;
import vip.simplify.mvc.view.JsonpView;
import vip.simplify.util.JsonResolver;
import vip.simplify.utils.DataUtil;
import vip.simplify.view.IPageTemplate;
import vip.simplify.webcache.util.BrowserUtil;

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
//	          定义在发生异常时状态码和视图页面名称的对应关系-第一步存在，以状态码指定页面处理显示
//		addStatusCode("404",HttpStatus.NOT_FOUND.value());
//		addStatusCode("502","error-runtime");
//		addStatusCode("503","error-sql");
//		通用异常处理的处理映射，提前处理好，用于后面的通过系统级异常的匹配-第二步，以异常类型来指定页面显示
//		Properties prop = new Properties();
//		prop.setProperty("java.sql.SQLException", "error-sql");
//		prop.setProperty("java.lang.RuntimeException", "error-runtime");
	}
	
	/**
	 * 
	 * 方法用途: 异常解析<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param requestUrl
	 * @param template
	 * @param throwable
	 */
	public static void resolverException(HttpServletRequest request, HttpServletResponse response, String requestUrl,
			IPageTemplate template, Throwable throwable,PropertiesConfig config,JsonResolver jsonResolver,String domain) {
		String exceptionMessage = throwable.getMessage();
		if(exceptionMessage == null) {
			if(throwable.getClass() == NullPointerException.class) {
				exceptionMessage = "空指针异常";
			}
		}
		if(throwable instanceof MessageException) {
			BaseException baseException = ((BaseException)throwable);
			if(baseException.getTargetException() != null) {
				throwable = baseException.getTargetException();
			}
			int statuscode = baseException.getErrorCode();
			response.setStatus(statuscode);
//	                      设置日志输出级别，不定义则默认不输出警告等错误日志信息
//			setWarnLogCategory(MappingExceptionResolver.class.getName());
			if(statuscode==208) {
				LOGGER.info("message:"+exceptionMessage);
			} else if(statuscode == HttpStatusEnum.MULTIPLE_CHOICES.value()) {
				LOGGER.warn("message:"+exceptionMessage);
			} else {
				LOGGER.error("message:"+exceptionMessage);// TODO 分析出业务数据，展现更个性化的日志信息，可以配合  解析LogByMethod的信息
			}
		} else {
			if(throwable instanceof UncheckedException) {
				BaseException baseException = ((BaseException)throwable);
				if(baseException.getTargetException() != null) {
					throwable = baseException.getTargetException();
				}
			}
			throwable.printStackTrace();
//	                     指定默认的返回码，默认是200
//		    setDefaultStatusCode("500");
			response.setStatus(HttpStatusEnum.INTERNAL_SERVER_ERROR.value());
			if(LOGGER.isErrorEnabled()) {
				LOGGER.error("error:"+exceptionMessage+"["+getStackTraceInfo(throwable,1));
			}
		}
//			不同请求风格的异常处理-通过请求后缀来处理不同的请求风格的异常视图start
		if(requestUrl.endsWith(".json")) {
			try {
				JsonView.exe(request, response, ResultFactory.error(exceptionMessage),config,jsonResolver);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		} else if(requestUrl.endsWith(".jsonp") || requestUrl.endsWith(".stream")) {//限定了stream数据流处理异常的提示方式，这里固定为jsonp方式，比如文件下载报错，属于这个范畴
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
				JsonpView.exe(request, response, ResultFactory.error(exceptionMessage),model,domain,config,jsonResolver);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		} else {//其他方式的请求,都走html业务视图，可以支持jsp,velocity 等模板引擎
			try {
//	                                方法一
//				ErrorView.exe(response,500,throwable.getMessage());
//				方法二：推荐
//				定义异常处理页面用来获取异常信息的变量名，默认名为exception
				request.setAttribute("exception", throwable);
				//没有在exceptionMappings里面找到对应的异常时 返回defaultErrorView指定的异常处理默认视图:500,404,403在这里其实jsp页面，比如500.jsp，400.jsp，exception.jsp
//				setDefaultErrorView("500");//exception
				template.render(request, response, null, null, "/"+response.getStatus());
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		}
	    //非200状态的请求全禁用缓存。
        BrowserUtil.disableBrowerCache(response);
//		不同请求风格的异常处理end
	}
	
	/**
	 * 
	 * 方法用途: 获取堆栈信息<br>
	 * 操作步骤: TODO<br>
	 * @param throwable
	 * @param length 打印的堆栈长度
	 * @return
	 */
	public static String getStackTraceInfo(Throwable throwable,int length) {
		StackTraceElement[] traceList = throwable.getStackTrace();
		if(traceList != null && traceList.length>=length) {
			StackTraceElement traceElement = traceList[0];
			String firstTrace = traceElement.getClassName()+":"+traceElement.getMethodName()+":"+traceElement.getLineNumber()+"==>>"+traceElement.getFileName();
			return firstTrace;
		}
		return null;
	}
}
