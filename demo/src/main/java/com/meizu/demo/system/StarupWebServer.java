package com.meizu.demo.system;

import com.meizu.WebServer;
import com.meizu.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>使用webserver容器</i></p>
 * <p>Desc: 这个类，可以不用，直接指定com.meizu.WebServer类来执行它的main方法就可以</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月2日 下午4:18:51</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月2日 下午4:18:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class StarupWebServer {
	public static void main(String[] args) {
		try {
			Startup.start();
			WebServer.init();
			new WebServer().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
