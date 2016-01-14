package com.meizu.cache.redis;

import org.junit.Test;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 下午1:39:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 下午1:39:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

public class HostAndPortUtilTest {
	@Test
	public  void test() {
		//HostAndPortUtil.getRedisServers();
		
		String str = "192.168.168.208:6379";
		System.out.println(str.split("\\|").length);
	}
}
