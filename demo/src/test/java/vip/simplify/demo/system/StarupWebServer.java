package vip.simplify.demo.system;

import vip.simplify.net.Bootstrap;

/**
  * <p><b>Title:</b><i>使用webserver容器</i></p>
 * <p>Desc: 1.这个类，可以不用，直接指定vip.simplify.WebServer类来执行它的main方法就可以
 *          2.测试这种方式，是否可以在IDEA工具中，达到热加载class的需求 TODO
 *          3.这种方式，可以通过exec-maven-plugin插件的功能来代替</p>
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
			Bootstrap.run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
