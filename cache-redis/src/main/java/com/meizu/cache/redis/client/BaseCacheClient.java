package com.meizu.cache.redis.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.impl.Cache;
import com.meizu.cache.redis.RedisClientPool;
import com.meizu.cache.util.DefaultCodec;

import redis.clients.jedis.ShardedJedis;

/**
 * 缓存操作基类
 *
 */
public abstract class BaseCacheClient implements Cache{
	private static final Logger log = LoggerFactory.getLogger(BaseCacheClient.class);
    //序列化
    protected DefaultCodec codec = new DefaultCodec();
    private String mod_name;
    
    public String getName() {
    	return mod_name;	
    }
    
    public BaseCacheClient(String mod_name){
    	this.mod_name = mod_name;
    }
    
    protected ShardedJedis getClient(){
		return RedisClientPool.getJedisClient(mod_name);
    }
    
    protected void returnClient(ShardedJedis shardedJedis){
    	RedisClientPool.returnJedisClient(mod_name, shardedJedis);
    }
    
    protected void returnBrokenResource(ShardedJedis shardedJedis){
    	RedisClientPool.returnBrokenResource(mod_name, shardedJedis);
    }
    
    protected byte[] getByteKey(String key){
    	if(key == null){
    		return null;
    	}
    	return key.getBytes();
    }
    
    /**
     * del
     * @param key
     * @return
     */
    public Long del(String key){
    	 ShardedJedis jedis = this.getClient();
         try {
             return jedis.del(key);
         } catch (Exception e) {
             log.error("del error  key["+key+"]", e);
             this.returnBrokenResource(jedis);
             return 0L;
         } finally {
             this.returnClient(jedis);
         }
    }
  
    /**
     * 缓存里面是否存在该key
     * @param key
     * @return
     */
    public boolean exists(String key){
    	 ShardedJedis jedis = this.getClient();
         try {
             return jedis.exists(key);
         } catch (Exception e) {
             log.error("exists error  key["+key+"]", e);
             this.returnBrokenResource(jedis);
             return false;
         } finally {
             this.returnClient(jedis);
         }
    }
    
}