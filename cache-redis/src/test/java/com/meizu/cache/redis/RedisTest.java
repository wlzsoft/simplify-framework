    package com.meizu.cache.redis;  

import java.util.Comparator;
import java.util.List;

import com.meizu.cache.redis.dao.impl.ListRedisDao;
import com.meizu.cache.redis.dao.impl.SearchRedisDao;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.ShardedJedis;
public class RedisTest {

	
	
	 public static void originalCache(){
	    	ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
	    	String key = "aaa";
	    	for(int i=0;i<10;i++){
	    		jedis.sadd(key, String.valueOf(i));
	    	}
	    	System.out.println(jedis.scard(key));
	    	jedis.close();
	    	System.out.println("ok");
	    	
	    }
	
	public static void performance2(){
		ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
		long begin = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			jedis.lpush("test", "test"+i);
		}
		jedis.expire("test", 60);
		jedis.close();
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
		
	}
	
	
	
	
}
  