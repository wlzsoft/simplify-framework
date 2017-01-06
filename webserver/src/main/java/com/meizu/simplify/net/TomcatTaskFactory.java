package com.meizu.simplify.net;

import java.io.IOException;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import com.meizu.simplify.mvc.ControllerFilter;

/**
  * <p><b>Title:</b><i>抽象的Tomcat任务工厂</i></p>
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
public class TomcatTaskFactory implements ITaskFactory{
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException{
		Tomcat tomcat = new Tomcat();  
        tomcat.setPort(port);
//      tomcat.setBaseDir("e:/tmp/tomcat");  
        tomcat.getHost().setAutoDeploy(false); 
        //--------context-------
        StandardContext context = new StandardContext();  
        context.setPath("/");  
//        context.addLifecycleListener(new FixContextListener());  
        //-------filter--------
        FilterDef filterDef = new FilterDef();
        filterDef.setFilterName("ControllerFilter");
        filterDef.setFilterClass(ControllerFilter.class.getName());
        context.addFilterDef(filterDef);
        FilterMap filterMap = new FilterMap();
        filterMap.setFilterName("ControllerFilter");
        filterMap.addURLPattern("/*");
        context.addFilterMap(filterMap);
        //-------filter--------
        tomcat.getHost().addChild(context);
        //-------context--------
        
        try {
			tomcat.start();
		} catch (LifecycleException e) {
			e.printStackTrace();
		}   
        tomcat.getServer().await();  
        
	}
}
