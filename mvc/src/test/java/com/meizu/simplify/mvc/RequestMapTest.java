package com.meizu.simplify.mvc;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月22日 下午5:58:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月22日 下午5:58:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class RequestMapTest {
	/**
	 * 
	 * 方法用途: 这个controller的多个请求地址映射解析的方式已经过时了，采用了数组的方式了<br>
	 * 操作步骤: TODO<br>
	 */
	@Test
	@Deprecated
	public void testRequestMap() {
		String rquestPath = "/(.+)/(.+)/demo/(.+)$ /(.+)/(.+)/demo2$ /demo/demo_(.+).html$ /demo/demo.html$ /demo/$ /demo/(.+)/(.+)$";
		for (String path : rquestPath.split("\\s+", -1)) {
			System.out.println(path);
		}
	}
}
