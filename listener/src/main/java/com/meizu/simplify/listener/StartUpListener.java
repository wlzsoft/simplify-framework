package com.meizu.simplify.listener;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;
import com.meizu.simplify.utils.ClassUtil;

/**
  * <p><b>Title:</b><i>系统初始化监听器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 上午9:59:49</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 上午9:59:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@WebListener("系统初始化监听器")
public class StartUpListener implements ServletContextListener,ServletContextAttributeListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		context.setInitParameter("type", "ALL");
		Startup.start();
//		systemConfig.setAppPath(_config.getServletContext().getRealPath(""));
//		"meizu demo Services v1.0.0.0 Start");
//		StringUtils.format("Current path -> {0}", systemConfig.getAppPath()));
//		"Start in " + DateUtils.getDate("yyyy-MM-dd HH:mm:ss"));
		List<Class<?>> listenerHandlerList= ClassUtil.findClassesByInterfaces(IListenerHandler.class, "com.meizu");
		if(listenerHandlerList != null && listenerHandlerList.size()>0) {
			IListenerHandler handler = (IListenerHandler) BeanFactory.getBean(listenerHandlerList.get(0));
			handler.handle(sce);
		}
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("系统停止运行");
		DruidPoolFactory.closePool();
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
