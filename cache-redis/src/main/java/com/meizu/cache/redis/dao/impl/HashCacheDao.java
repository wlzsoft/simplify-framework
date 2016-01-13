package com.meizu.cache.redis.dao.impl;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.dao.RedisCacheDao;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.JsonUtil;

import redis.clients.jedis.ShardedJedis;


/**
 * SET 操作集合
 *
 */
public class HashCacheDao extends RedisCacheDao {
	private final Logger log = LoggerFactory.getLogger(getClass());
    public HashCacheDao(String mod_name) {
		super(mod_name);
	}

    /**
     * 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行hset操作。如果域field已经存在于哈希表中，旧值将被覆盖。
     * @param key
     * @param field
     * @param value
     * @return
     */
    public boolean set(String key,  String field, Object value,int seconds){
    	ShardedJedis jedis = this.getClient();
		try {
			long status = jedis.hset(key, field, JsonUtil.ObjectToJson(value));
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			if(status >= 0){
				return true;
			}
		} catch (Exception e) {
			log.error("set error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return false;
    }
    
    /**
     * 返回哈希表key中指定的field的值
     * @param key
     * @param field
     * @return
     */
    public Object get(String key,  String field){
    	ShardedJedis jedis = this.getClient();
		try {
			String str = jedis.hget(key,field);
			if(str != null && str.length() > 0){
				return JsonUtil.JsonToObject(str);
			}
		} catch (Exception e) {
			log.error("get error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return null;
    }
    
    /**
     * 将哈希表key中的域field的值设置为value，当且仅当域field不存在。若域field已经存在，该操作无效。如果key不存在，一个新哈希表被创建并执行hsetnx命令。
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hsetnx(String key,  String field, Object value,int seconds){
    	ShardedJedis jedis = this.getClient();
		try {
			long status = jedis.hsetnx(key, field, JsonUtil.ObjectToJson(value));
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			if(status >= 0){
				return true;
			}
		} catch (Exception e) {
			log.error("hsetnx error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return false;
    }
    
    /**
     * 同时将多个field - value(域-值)对设置到哈希表key中。此命令会覆盖哈希表中已存在的域。如果key不存在，一个空哈希表被创建并执行hmset操作。
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hmset(String key, Map<String, Object> hash,int seconds){
    	if(hash.isEmpty()){
    		return false;
    	}
    	
    	Map<String,String> map = new HashMap<String, String>();
    	for(Iterator<String> it = hash.keySet().iterator();it.hasNext();){
    		String k = it.next();
    		Object value = hash.get(k);
    		map.put(k, JsonUtil.ObjectToJson(value));
    	}
    	
    	ShardedJedis jedis = this.getClient();
		try {
			String ret = jedis.hmset(key, map);
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			return ret.equalsIgnoreCase("OK");
		} catch (Exception e) {
			log.error("hmset error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return false;
    }
   
    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * @param key
     * @param seconds
     * @param fields
     * @return
     */
    public long hdel(String key,int seconds,String ... fields){
    	ShardedJedis jedis = this.getClient();
		try {
			long ret = jedis.hdel(key, fields);
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			return ret;
		} catch (Exception e) {
			log.error("hdel error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return 0;
    }
    
    /**
     * 返回哈希表 key 中域的数量。
     * @param key
     * @return
     */
    public long hlen(String key){
    	ShardedJedis jedis = this.getClient();
		try {
			return jedis.hlen(key);
		} catch (Exception e) {
			log.error("hlen error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return 0;
    }
    
    /**
     * 查看哈希表 key 中，给定域 field 是否存在。
     * @param key
     * @param field
     * @return
     */
    public boolean hexists(String key,String field){
    	ShardedJedis jedis = this.getClient();
		try {
			 return jedis.hexists(key, field);
		} catch (Exception e) {
			log.error("hlen error!", e);
			 this.returnBrokenResource(jedis);
		} finally {
			this.returnClient(jedis);
		}
		return false;
    }

	
	
}
