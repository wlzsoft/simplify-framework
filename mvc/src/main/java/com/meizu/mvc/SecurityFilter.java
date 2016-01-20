package com.meizu.mvc;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;




/**
 * <p><b>Title:</b><i>验证Servlet过滤器</i></p>
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
public class SecurityFilter /*implements Filter*/ {

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

//	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) res;
//		
//		String thisUrl = request.getRequestURI().substring(request.getContextPath().length());// 当前地址
//		while ( thisUrl.indexOf("//") != -1 ) thisUrl = thisUrl.replace("//", "/"); // 防止用户避过正则
//		//if (StringUtils.notNull(request.getQueryString()).length() > 0) thisUrl += "?" + request.getQueryString();
//		
//		for ( String key : MvcInit.servletMap.keySet() ) {
//			Pattern pattern = Pattern.compile("^" + key);
//			Matcher matcher = pattern.matcher(thisUrl);
//			if (matcher.find()) {
//				String[] params = new String[matcher.groupCount() + 1];
//				for ( int i = 0; i <= matcher.groupCount(); params[i] = matcher.group(i++) );
//				ServletModel model = MvcInit.servletMap.get(key);
//				try {
//					long time = System.currentTimeMillis();
//					
//					Statistics.getReadMap().put(thisUrl, 0);
//					
//					request.setAttribute("params", params);
//					String parName = model.getMethod().substring(2, model.getMethod().length());
//					request.setAttribute("cmd", Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
//					Method method = model.getCls().getMethod("doPost", new Class[] { HttpServletRequest.class, HttpServletResponse.class });
//					method.invoke(model.getCls().newInstance(), new Object[] { request, response });
//					
//					long readtime = System.currentTimeMillis() - time;
//					PrintHelper.getPrint().debug(StringUtils.format("{0} 耗时:{1}毫秒", thisUrl, (readtime)));
//					
//					// STATISTICS
//					Statistics.incReadcount();
//					Statistics.setReadMaxTime(readtime, thisUrl);
//					Statistics.getReadMap().remove(thisUrl);
//					
//				} catch ( Exception e ) {
//					e.printStackTrace();
//					throw new BusinessException(e);
//				}
//				return;
//			}
//		}
//		chain.doFilter(req, res);
//	}
//
//	public void init(FilterConfig filterConfig) throws ServletException {
//	}
}
