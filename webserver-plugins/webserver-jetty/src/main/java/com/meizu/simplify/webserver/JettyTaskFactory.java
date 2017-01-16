package com.meizu.simplify.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.plugin.annotation.Plugin;
import com.meizu.simplify.plugin.enums.PluginTypeEnum;
import com.meizu.simplify.webserver.ITaskFactory;

/**
  * <p><b>Title:</b><i>抽象的Jetty任务工厂</i></p>
 * <p>Desc: TODO</p>
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
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		Server server = new Server(new InetSocketAddress(host, port));
		server.setHandler(new AbstractHandler() {
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
					throws IOException, ServletException {
				response.setContentType("text/html; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				response.getWriter().println("<h1>Hello World</h1>");
				// Inform jetty that this request has now been handled
				baseRequest.setHandled(true);
			}
		});
		try {
			server.start();
			server.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

