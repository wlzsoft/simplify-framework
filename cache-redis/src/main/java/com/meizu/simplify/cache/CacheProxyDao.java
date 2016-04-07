package com.meizu.simplify.cache;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午6:48:07</p>
 * <p>Modified By:wanghb-</p>
 * <p>Modified Date:2016年2月26日 下午6:48:07</p>
 * @author <a href="mailto:wanghb@meizu.com" title="邮箱地址">wanghb</a>
 * @version Version 0.1
 *
 */
public class CacheProxyDao {
	private static final ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	public static ICacheDao<String, Object> getCache() {
		return commonRedisDao;
	}
}