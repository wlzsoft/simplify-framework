package vip.simplify.webserver;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.mvc.ControllerFilter;
import vip.simplify.plugin.annotation.Plugin;
import vip.simplify.plugin.enums.PluginTypeEnum;

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
@Plugin(type= PluginTypeEnum.WEBSERVER,value="tomcat")
public class TomcatTaskFactory implements ITaskFactory {//implements IPageTemplate{
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param host
	 * @param port
	 * @param backlog
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		Tomcat tomcat = new Tomcat();  
        tomcat.setPort(port);
//      tomcat.setBaseDir("e:/tmp/tomcat");  
//      tomcat.getHost().setAutoDeploy(false);
		//appBase在tomcat，是指的应用部署的根目录，这个目录下面的每个文件夹就是一个应用，appbase下面可以有多应用目录，而docBase是其中一个应用的待部署包路径，appbase目录下的应用会被部署
        tomcat.getHost().setAppBase(".");
        //--------Server--------
        Server server = tomcat.getServer();  
//      server.addLifecycleListener(new FixContextListener());
        server.addLifecycleListener(new AprLifecycleListener());
        //--------context-------
        Context context = null;
		try {
			//通过addWebapp，就把docBase的内容映射为一个contextpath，也就是appBase下面的一个应用，在appBase下面对应contextPath的只的目录，内容是docBase指向目录的内容
			context = tomcat.addWebapp("/", ".");
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//注意：如果设置了appbase和docbase也无效的话，那么先确认是否WEB-INF下面没有lib目录或lib目录里为空，并且Classes目录也为空,这里也确认另外的一件事appbase和docbase的设置不是影响controller加载的问题，classpath才是主根
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
