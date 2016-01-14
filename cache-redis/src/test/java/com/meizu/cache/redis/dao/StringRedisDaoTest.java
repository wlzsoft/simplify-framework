package com.meizu.cache.redis.dao;

import com.meizu.cache.redis.dao.impl.StringRedisDao;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 上午11:10:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 上午11:10:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */

public class StringRedisDaoTest {
	public static void test() {
			StringRedisDao client = new StringRedisDao("redis_ref_hosts");
			// client.set("vip_send_mail_max_msgid", 20L);
			// long max = (Long) client.get("vip_send_mail_max_msgid");
			// System.out.println(max);
		}
}
