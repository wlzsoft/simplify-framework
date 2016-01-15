package com.meizu.cache.redis.dao;

import java.io.Serializable;

import com.meizu.cache.enums.CacheExpireTimeEnum;
import com.meizu.cache.enums.TimeEnum;
import com.meizu.cache.redis.RedisPool;
import com.meizu.cache.redis.exception.RedisException;

import redis.clients.jedis.ShardedJedis;

/**
 * 缓存操作基类
 *
 */
public abstract class BaseRedisDao<K extends Serializable>  {
	
	public ShardedJedis jedis = null;
	public BaseRedisDao(String mod_name) {
    	jedis = RedisPool.getConnection(mod_name);
	}
	/**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param export
	 * @param seconds
	 * @return
	 */
	public long expire(K key, CacheExpireTimeEnum export, TimeEnum seconds) {
		if(key instanceof String) {
			return jedis.expire(key.toString(), export.timesanmp());
		} else if(key instanceof byte[]){
			return jedis.expire((byte[])key, export.timesanmp());
		} else {
			throw new RedisException("无效key");
		}
	}
	/**
	 * 
	 * 方法用途: 获取指定key的剩余过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long getExpire(K key, TimeEnum seconds) {
		return 0L;
	}
}