package com.meizu.cache.redis;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import redis.clients.jedis.ShardedJedis;

/**
 * 原生REDIS操作
 *
 */
public class OriginalCacheClient extends BaseCacheClient {
	private final Logger log = LoggerFactory.getLogger(getClass());
    public OriginalCacheClient(String mod_name) {
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
    	OriginalCacheClient client = new OriginalCacheClient("redis_ref_hosts");
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
