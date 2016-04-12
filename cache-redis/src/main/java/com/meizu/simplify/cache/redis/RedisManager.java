package com.meizu.simplify.cache.redis;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.ICacheManager;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;



/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午5:47:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午5:47:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class RedisManager implements ICacheManager {

    @Override
	public ICacheDao<?,?> getCache(String modName) {
		new CommonRedisDao<>(modName);
		return null;
	}
}
