package com.meizu.simplify.webserver;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.ControllerFilter;
import com.meizu.simplify.plugin.annotation.Plugin;
import com.meizu.simplify.plugin.enums.PluginTypeEnum;

/**
  * <p><b>Title:</b><i>Tomcat任务工厂</i></p>
 * <p>Desc: TODO启用失败，待修复</p>
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
@Plugin(type=PluginTypeEnum.WEBSERVER,value="tomcat")
public class TomcatTaskFactory implements ITaskFactory {//implements IPageTemplate{
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		Tomcat tomcat = new Tomcat();  
        tomcat.setPort(port);
//      tomcat.setBaseDir("e:/tmp/tomcat");  
//      tomcat.getHost().setAutoDeploy(false);
        tomcat.getHost().setAppBase(".");
        //--------Server--------
        Server server = tomcat.getServer();  
//      server.addLifecycleListener(new FixContextListener());
        server.addLifecycleListener(new AprLifecycleListener());
        //--------context-------
        Context context = null;
		try {
			context = tomcat.addWebapp("/", ".");
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //-------filter--------
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName(ControllerFilter.class.getSimpleName());
        filterDef.setFilterClass(ControllerFilter.class.getName());
        context.addFilterDef(filterDef);
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName(ControllerFilter.class.getSimpleName());
        filterMap.addURLPattern("/*");
        context.addFilterMap(filterMap);
        //-------filter--------
        try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}   
        tomcat.getServer().await();  
	}
}
