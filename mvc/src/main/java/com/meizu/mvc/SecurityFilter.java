package com.meizu.mvc;

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

import com.meizu.mvc.directives.SecurityContoller;
import com.meizu.mvc.dto.ControllerAnnotationInfo;
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
		while ( thisUrl.indexOf("//") != -1 ) thisUrl = thisUrl.replace("//", "/"); // 防止用户避过正则
		//if (StringUtils.notNull(request.getQueryString()).length() > 0) thisUrl += "?" + request.getQueryString();
		
		for ( String key : MvcInit.controllerMap.keySet() ) {
			Pattern pattern = Pattern.compile("^" + key);
			Matcher matcher = pattern.matcher(thisUrl);
			if (matcher.find()) {
				String[] params = new String[matcher.groupCount() + 1];
				for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
				ControllerAnnotationInfo model = MvcInit.controllerMap.get(key);
				try {
					long time = System.currentTimeMillis();
					
					Statistics.getReadMap().put(thisUrl, 0);
					
					request.setAttribute("params", params);
					String parName = model.getMethod().substring(2, model.getMethod().length());
					request.setAttribute("cmd", Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
					SecurityContoller<?> bs = (SecurityContoller<?>)model.getObj();
					bs.process(request, response);
					
					long readtime = System.currentTimeMillis() - time;
//					LOGGER.debug(StringUtil.format("{0} 耗时:{1}毫秒", thisUrl, (readtime)));
					
					// STATISTICS
					Statistics.incReadcount();
					Statistics.setReadMaxTime(readtime, thisUrl);
					Statistics.getReadMap().remove(thisUrl);
					
				} catch ( Exception e ) {
					e.printStackTrace();
					throw new UncheckedException(e);
				}
				return;
			}
		}
		chain.doFilter(req, res);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}
