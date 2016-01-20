package com.meizu.mvc.directives;

import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.exception.UncheckedException;


/**
 * 验证Servlet过滤器
 * 
 */
public class SecurityFilter implements Filter {

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public @interface RequestSet {
		String path();
	}

	@Target(ElementType.PARAMETER)
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@Inherited
	public @interface QueryParam {
		String defaultValue();
		String param();
	}

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String thisUrl = request.getRequestURI().substring(request.getContextPath().length());// 当前地址
		while ( thisUrl.indexOf("//") != -1 ) thisUrl = thisUrl.replace("//", "/"); // 防止用户避过正则
		//if (StringUtils.notNull(request.getQueryString()).length() > 0) thisUrl += "?" + request.getQueryString();
		
		for ( String key : MvcInit.servletMap.keySet() ) {
			Pattern pattern = Pattern.compile("^" + key);
			Matcher matcher = pattern.matcher(thisUrl);
			if (matcher.find()) {
				String[] params = new String[matcher.groupCount() + 1];
				for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
				ServletModel model = MvcInit.servletMap.get(key);
				try {
					long time = System.currentTimeMillis();
					
					Statistics.getReadMap().put(thisUrl, 0);
					
					request.setAttribute("params", params);
					String parName = model.getMethod().substring(2, model.getMethod().length());
					request.setAttribute("cmd", Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
					Method method = model.getCls().getMethod("doPost", new Class[] { HttpServletRequest.class, HttpServletResponse.class });
					method.invoke(model.getCls().newInstance(), new Object[] { request, response });
					
					long readtime = System.currentTimeMillis() - time;
//					PrintHelper.getPrint().debug(StringUtils.format("{0} 耗时:{1}毫秒", thisUrl, (readtime)));
					
					// STATISTICS
					Statistics.incReadcount();
					Statistics.setReadMaxTime(readtime, thisUrl);
					Statistics.getReadMap().remove(thisUrl);
					
				} catch ( Exception e ) {
					throw new UncheckedException(e);
				}
				return;
			}
		}
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
