package vip.simplify.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.ControllerFilter;
import vip.simplify.plugin.annotation.Plugin;
import vip.simplify.plugin.enums.PluginTypeEnum;

/**
  * <p><b>Title:</b><i>抽象的Jetty任务工厂</i></p>
 * <p>Desc: 非完整版jetty，如果需支持filter，那么需增加依赖相关的jetty的jar包</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月29日 下午3:51:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月29日 下午3:51:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@Plugin(type=PluginTypeEnum.WEBSERVER,value="jetty")
public class JettyTaskFactory implements ITaskFactory {
	
	private static final ControllerFilter filter = new ControllerFilter();
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param host
	 * @param port
	 * @param backlog
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		Server server = new Server(new InetSocketAddress(host, port));
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		FilterHolder f = new FilterHolder(filter);
		servletContextHandler.addFilter(f,"/*",null);     
		server.setHandler(servletContextHandler);//无session管理
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

