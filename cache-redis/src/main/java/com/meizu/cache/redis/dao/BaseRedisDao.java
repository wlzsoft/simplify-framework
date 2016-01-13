package com.meizu.cache.redis.dao;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.ICacheDao;
import com.meizu.cache.enums.CacheExpireTimeEnum;
import com.meizu.cache.exception.CacheException;
import com.meizu.cache.redis.RedisManager;
import com.meizu.cache.redis.RedisPool;
import com.meizu.cache.util.DefaultCodec;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.ByteUtil;
import com.meizu.simplify.utils.ReflectionUtil;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 缓存操作基类
 *
 */
//@Component
public abstract class BaseRedisDao<K extends Serializable,V,T extends Serializable> {
	private static final Logger LOGGER = LoggerFactory.getLogger(BaseRedisDao.class);
	//序列化
	protected DefaultCodec codec = new DefaultCodec();
    public RedisManager client = null;
	private int expire;

	
    
    public BaseRedisDao(int expire, RedisManager redisClient) {
	    this.expire = expire;
		this.client = redisClient;
	}
    
    public BaseRedisDao(String mod_name) {
    	client = new RedisManager(mod_name);
	}

	protected byte[] getByteKey(K key){
    	if(key == null){
    		return null;
    	}
    	return ((String) key).getBytes();
    }
	
	
}