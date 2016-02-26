package com.meizu.simplify.aop.cache;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;

/**
 * @author  wanghb 
 * @Company :meizu
 * @version  V1.0
 * @Date  2016年2月24日
 * @Copyright :Copyright(c)2015
 */
public class CacheProxyDao {
	private static final ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	public static ICacheDao<String, Object> getCache() {
		return commonRedisDao;
	}
}