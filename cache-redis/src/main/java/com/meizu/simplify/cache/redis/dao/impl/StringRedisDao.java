package com.meizu.simplify.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IStringCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;

import redis.clients.jedis.ShardedJedis;

/**
 * <p><b>Title:</b><i>redis  String 结构操作</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:03:13</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:03:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class StringRedisDao extends BaseRedisDao<String> implements IStringCacheDao{
	
	public StringRedisDao(String mod_name) {
		super(mod_name);
	}

	
	public String getAndSet(String key, String value) {
		String ret = CacheExecute.execute(key, (k,jedis) ->  CacheExecute.getJedis(mod_name).getSet(k, value), mod_name);
		return ret;
	}

	public String get(String key) {
		String ret = CacheExecute.execute(key, (k,jedis) ->  CacheExecute.getJedis(mod_name).get(k), mod_name);
		return ret;
	}

	public boolean set(String key, String value,int seconds) {
		Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
				String result = jedis.set(k, value);
				if(seconds > 0){
					jedis.expire(k, seconds);
				}
				return result.equalsIgnoreCase("OK");
		}, mod_name);
		return ret;
	}
	
	  
    public boolean setnx(String key, String value) {
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {
			@Override
			public Boolean call(String k,ShardedJedis jedis) {
				long result = jedis.setnx(k, value);
				return result > 0;
			}
		}, mod_name);
    	return ret;
    }

    public boolean setex(String key, int seconds, String value) {
    	Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
				String result = CacheExecute.getJedis(mod_name).setex(k,seconds, value);
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(k, seconds);
				}
				return result.equalsIgnoreCase("OK");
		}, mod_name);
    	return ret;
    }

}
