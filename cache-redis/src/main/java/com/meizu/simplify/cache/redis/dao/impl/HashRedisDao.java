package com.meizu.simplify.cache.redis.dao.impl;


import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IHashCacheDao;
import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;
import com.meizu.simplify.utils.JsonUtil;

import redis.clients.jedis.ShardedJedis;

/**
 * <p><b>Title:</b><i>SET 操作集合</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:50:57</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:50:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HashRedisDao extends BaseRedisDao<String> implements IHashCacheDao {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    public HashRedisDao(String mod_name) {
		super(mod_name);
	}

    /**
     * 
     * 方法用途: 将哈希表key中的域field的值设为value。如果key不存在，一个新的哈希表被创建并进行hset操作。如果域field已经存在于哈希表中，旧值将被覆盖<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean set(String key,  String field, Object value,int seconds){
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				long status = CacheExecute.getJedis(mod_name).hset(key, field, JsonUtil.ObjectToJson(value));
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				if(status >= 0){
					return true;
				}
				return false;
			}
		}, mod_name);
		return ret;
    }
    
    /**
     * 
     * 方法用途: 返回哈希表key中指定的field的值<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param field
     * @return
     */
    public Object get(String key,  String field){
    	Object ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Object>() {

			@Override
			public Object call(String key) {
				String str = CacheExecute.getJedis(mod_name).hget(key,field);
				if(str != null && str.length() > 0){
					return JsonUtil.JsonToObject(str);
				}
				return null;
			}
		}, mod_name);
		return ret;
    }
    
    /**
     * 
     * 方法用途: 将哈希表key中的域field的值设置为value，当且仅当域field不存在。若域field已经存在，该操作无效。如果key不存在，一个新哈希表被创建并执行hsetnx命令<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param field
     * @param value
     * @param seconds
     * @return
     */
    public boolean hsetnx(String key,  String field, Object value,int seconds){
    	
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				long status = CacheExecute.getJedis(mod_name).hsetnx(key, field, JsonUtil.ObjectToJson(value));
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				if(status >= 0){
					return true;
				}
				return false;
			}
		}, mod_name);
		return ret;
    	
    }
    
    /**
     * 
     * 方法用途: 同时将多个field - value(域-值)对设置到哈希表key中。此命令会覆盖哈希表中已存在的域。如果key不存在，一个空哈希表被创建并执行hmset操作<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param hash
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
    	
    	
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				String ret = CacheExecute.getJedis(mod_name).hmset(key, map);
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				return ret.equalsIgnoreCase("OK");
			}
		}, mod_name);
		return ret;
    	
    	
    }
   
    /**
     * 
     * 方法用途: 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param seconds
     * @param fields
     * @return
     */
    public long hdel(String key,int seconds,String ... fields){
    	
    	
    	Long ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Long>() {

			@Override
			public Long call(String key) {
				long ret = CacheExecute.getJedis(mod_name).hdel(key, fields);
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				return ret;
			}
		}, mod_name);
		return ret;
    	
    }
    
    /**
     * 
     * 方法用途: 返回哈希表 key 中域的数量<br>
     * 操作步骤: TODO<br>
     * @param key
     * @return
     */
    public long hlen(String key){
    	Long ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Long>() {

			@Override
			public Long call(String key) {
				return CacheExecute.getJedis(mod_name).hlen(key);
			}
		}, mod_name);
		return ret;
    }
    
    /**
     * 
     * 方法用途: 查看哈希表 key 中，给定域 field 是否存在<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param field
     * @return
     */
    public boolean hexists(String key,String field){
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				return CacheExecute.getJedis(mod_name).hexists(key, field);
			}
		}, mod_name);
		return ret;
    }
	
}
