package com.meizu.authfilter.interfacefilter;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月19日 上午11:30:10</p>
 * <p>Modified By:Administrator-</p>
 * <p>Modified Date:2016年4月19日 上午11:30:10</p>
 * @author <a href="mailto:wanglizong@meizu.com" title="邮箱地址">wanglizong</a>
 * @version Version 0.1
 *
 */
@WebFilter(filterName="interfaceAuth" ,urlPatterns="/*",dispatcherTypes={DispatcherType.REQUEST})
public class InterfaceAuth implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}

}
