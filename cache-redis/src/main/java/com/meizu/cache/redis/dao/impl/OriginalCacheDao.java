package com.meizu.cache.redis.dao.impl;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.dao.RedisCacheDao;

import redis.clients.jedis.ShardedJedis;

/**
 * 原生REDIS操作
 *
 */
public class OriginalCacheDao extends RedisCacheDao {
	private final Logger log = LoggerFactory.getLogger(getClass());
    public OriginalCacheDao(String mod_name) {
		super(mod_name);
	}

    public ShardedJedis getClient(){
    	return super.getClient();
    }
    
    public void returnClient(ShardedJedis shardedJedis){
    	super.returnClient(shardedJedis);
    }
    
    public void returnBrokenResource(ShardedJedis shardedJedis){
    	super.returnBrokenResource(shardedJedis);
    }
    
    public static void main(String[] args){
    	OriginalCacheDao client = new OriginalCacheDao("redis_ref_hosts");
    	ShardedJedis jedis = client.getClient();
    	String key = "aaa";
    	for(int i=0;i<10;i++){
    		jedis.sadd(key, String.valueOf(i));
    	}
    	System.out.println(jedis.scard(key));
    	client.returnClient(jedis);
    	System.out.println("ok");
    	
    }
}
