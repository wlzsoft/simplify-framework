package com.meizu.demo.system;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.mvc.MvcInit;
import com.meizu.mvc.controller.VelocityForward;
import com.meizu.simplify.ioc.Startup;

/**
 * <p><b>Title:</b><i>系统初始化</i></p>
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
//	private SystemConfig systemConfig = SystemConfig.getInstance(); 
	@Override
	public final void init(final ServletConfig _config) throws ServletException {
		Startup.start();
		MvcInit.init();
		VelocityForward.init();
//		systemConfig.setAppPath(_config.getServletContext().getRealPath(""));
//		"meizu demo Services v1.0.0.0 Start");
//		StringUtils.format("Current path -> {0}", systemConfig.getAppPath()));
//		"Start in " + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void destroy() {
	}
	

}


   