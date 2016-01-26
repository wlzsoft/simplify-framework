package com.meizu.demo.system;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * <p><b>Title:</b><i>系统初始化servlet</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月21日 下午1:01:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月21日 下午1:01:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@WebServlet(loadOnStartup=0,urlPatterns="/initservlet")
@WebInitParam(name = "type", value = "ALL")
public class StartUpServlet extends HttpServlet {
	private static final long serialVersionUID = -3818664573588631645L;

	@Override
	public final void init(final ServletConfig _config) throws ServletException {
		System.out.println("initservlet");
	}

	@Override
	public void destroy() {
		System.out.println("Servlet开始销毁，系统即将停止运行");
	}
	

}


   