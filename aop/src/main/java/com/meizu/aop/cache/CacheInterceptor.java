package com.meizu.aop.cache;

import com.meizu.aop.IInterceptor;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.redis.dao.impl.CommonRedisDao;

/**
 * <p><b>Title:</b><i>缓存拦截器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class CacheInterceptor implements IInterceptor{

	@Override
	public void after(Object... args) {
		System.out.println("cacheAfter");
	}

	@Override
	public void before(Object... args) {
		ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
		commonRedisDao.set("age", 2);
		System.out.println("cacheBefore");
	}
	
}
