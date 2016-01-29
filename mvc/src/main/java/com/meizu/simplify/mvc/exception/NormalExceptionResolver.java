package com.meizu.simplify.mvc.exception;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <p><b>Title:</b><i>安全异常处理</i></p>
 * <p>Desc: TODO：为测试，未使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年1月13日 下午3:32:30</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年1月13日 下午3:32:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class NormalExceptionResolver  /*extends SimpleMappingExceptionResolver*/ {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	public NormalExceptionResolver() {
		//spring-repositories.xml文件中已经有全局配置了，这里是具体异常处理类的配置了。重复配置，看需要做差异配置
//		setExceptionAttribute("ex");//TODO 注意，目前大部分的异常都不会指定到错误页面，要求需要有ex属性名才会。需要调试。
		//500,404,403在这里其实jsp页面，比如500.jsp，400.jsp
//		setDefaultErrorView("500");
//		addStatusCode("500", HttpStatus.INTERNAL_SERVER_ERROR.value());
//		addStatusCode("404", HttpStatus.NOT_FOUND.value());
//		addStatusCode("403", HttpStatus.FORBIDDEN.value());
//		setWarnLogCategory(NormalExceptionResolver.class.getName());
	}
	
	
//	@Override
	protected ModelAndView doResolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
//		HandlerMethod method = (HandlerMethod) handler;
//		Annotation[] s = method.getBean().getClass().getAnnotations();
//		for (Annotation annotation : s) {
//			if(annotation.toString().contains("RestController")) {
//				System.out.println(annotation.toString());
//				break;
//			}
//		}
		
		
        
     
        
//		LogUtil logger = SpringContext.getInstance(LogUtil.class);
        logger.error(exception.getMessage());// TODO 分析出业务数据，展现更个性化的日志信息，可以配合  解析LogByMethod的信息

//		class StringPrintWriter extends PrintWriter{
//
//		    public StringPrintWriter(){
//		        super(new StringWriter());
//		    }
//		   
//		    public StringPrintWriter(int initialSize) {
//		          super(new StringWriter(initialSize));
//		    }
//		   
//		    public String getString() {
//		          flush();
//		          return ((StringWriter) this.out).toString();
//		    }
//		    @Override
//		    public String toString() {
//		        return getString();
//		    }
//		}
//
//		StringPrintWriter strintPrintWriter = new StringPrintWriter();  
//		exception.printStackTrace(strintPrintWriter);  
//      mav.addObject("errorMsg", strintPrintWriter.getString());
//		使用错误的ErrorResult实体来定义，或是JsonResult.error()类实现， 是否需要为ErrorResult和JsonResult.error,提供属性值url，来支持错误或是成功之后的具体跳转页面。 TODO 
//		if (AjaxUtils.isAjaxRequest(request)) {
//			response.setStatus(HttpStatus.MULTIPLE_CHOICES.value());
//			mav.addObject("result", JsonResult.fail());
//			processCustomExceptions(mav, exception);
//		} else {
//			if(true) {
//				mav = new ModelAndView();
//				response.setStatus(HttpStatus.MULTIPLE_CHOICES.value());
//				mav.addObject("result", JsonResult.error(exception.getMessage()));
//			}
//		}
		// 根据不同错误转向不同页面  
//        if (exception instanceof HttpRequestMethodNotSupportedException) {  
//            logger.error("Http Method Not Supported");  
//        } else if(exception instanceof UncheckedException) {  
//        	mav.addObject("result", JsonResult.error(exception.getMessage()));
//        	mav.setViewName("/error"); //TODO
//        } else if(exception instanceof NullPointerException) {
//        	mav.addObject("result", JsonResult.error(exception.getMessage()));
//        	mav.setViewName("/error"); //TODO
//        } else {
//        	mav.addObject("result", JsonResult.error(exception.getMessage()));
//        	mav.setViewName("/error"); //TODO
//        }
        //非200状态的请求全禁用缓存。
//        BrowserUtil.disableBrowerCache(response);
        
		return null;
	}

	/**
	 * 方法用途: 自定义异常处理<br>
	 * 操作步骤: TODO<br>
	 * @param mav
	 * @param exception
	 */
//	protected void processCustomExceptions(ModelAndView mav, Exception exception) {
//		if (exception instanceof BusinessException) {
//			mav.addObject("result", JsonResult.error(exception.getMessage()));
//		}
//	}
	

}
