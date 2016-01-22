package com.meizu.demo.system;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import com.meizu.mvc.MvcInit;
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
public class StartUpServlet extends HttpServlet {
	private static final long serialVersionUID = -3818664573588631645L;
//	private SystemConfig systemConfig = SystemConfig.getInstance(); 

	@Override
	public final void init(final ServletConfig _config) throws ServletException {
		MvcInit.init();
		Startup.start();
//		systemConfig.setAppPath(_config.getServletContext().getRealPath(""));
//		"meizu demo Services v1.0.0.0 Start");
//		StringUtils.format("Current path -> {0}", systemConfig.getAppPath()));
//		"Start in " + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
	}

	@Override
	public void destroy() {
	}

}
