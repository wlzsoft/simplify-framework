package com.meizu.simplify.mvc;

import java.io.IOException;
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

import com.meizu.simplify.mvc.directives.SecurityContoller;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.exception.UncheckedException;




/**
 * <p><b>Title:</b><i>安全过滤器</i></p>
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
@WebFilter(urlPatterns="/*",dispatcherTypes={DispatcherType.REQUEST},filterName="SecurityFilter")
public class SecurityFilter implements Filter {

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
		ControllerAnnotationInfo controllerAnnotationInfo = MvcInit.controllerMap.get(thisUrl);
		if(controllerAnnotationInfo !=null) {
			analysisAndProcess(request, response, thisUrl, controllerAnnotationInfo, null);
		} else {
			for ( String key : MvcInit.controllerMap.keySet() ) {//TODO: 提供快速查找的算法，可以key的string转成整型，然后比较整型,不过由于正则无法确定具体值的访问，所以也没法比较
				if(!key.contains("$")) {
					continue;
				}
				Pattern pattern = Pattern.compile("^" + key);
				Matcher matcher = pattern.matcher(thisUrl);
				if (matcher.find()) {
					String[] params = new String[matcher.groupCount() + 1];
					for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
					controllerAnnotationInfo = MvcInit.controllerMap.get(key);
					analysisAndProcess(request, response, thisUrl,controllerAnnotationInfo, params);
					break;
				}
			}
		}
		
		chain.doFilter(req, res);
	}
	
	/**
	 * 
	 * 方法用途: 分析请求地址最终调用分发处理业务<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param thisUrl
	 * @param key
	 * @param params
	 */
	private void analysisAndProcess(HttpServletRequest request, HttpServletResponse response, String thisUrl,ControllerAnnotationInfo controllerAnnotationInfo,
			String[] params) {
		
		try {
			long time = System.currentTimeMillis();
			
			Statistics.getReadMap().put(thisUrl, 0);
			
			request.setAttribute("params", params);
			String parName = controllerAnnotationInfo.getMethod().substring(2, controllerAnnotationInfo.getMethod().length());
			request.setAttribute("cmd", Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
			SecurityContoller<?> bs = (SecurityContoller<?>)controllerAnnotationInfo.getObj();
			bs.process(request, response);
			
			long readtime = System.currentTimeMillis() - time;
//			LOGGER.debug(StringUtil.format("{0} 耗时:{1}毫秒", thisUrl, (readtime)));
			
			// 记录统计信息
			Statistics.incReadcount();
			Statistics.setReadMaxTime(readtime, thisUrl);
			Statistics.getReadMap().remove(thisUrl);
			
		} catch ( Exception e ) {
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
