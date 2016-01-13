package com.meizu.cache.redis.dao.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.enums.CacheExpireTimeEnum;
import com.meizu.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.utils.ReflectionUtil;

import redis.clients.jedis.ShardedJedis;

/**
 * <p><b>Title:</b><i>简单缓存操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:03:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:03:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ExpireRedisDao<K,V,T> extends BaseRedisDao<Integer,V,Serializable> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExpireRedisDao.class);
	
	public ExpireRedisDao(String mod_name) {
		super(mod_name);
	}
	
//	@Override
//	public String expire(K key, CacheExpireTimeEnum export, TimeUnit seconds) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public String getExpire(K key, TimeUnit seconds) {
//		// TODO Auto-generated method stub
//		return null;
//	}	
	
}
