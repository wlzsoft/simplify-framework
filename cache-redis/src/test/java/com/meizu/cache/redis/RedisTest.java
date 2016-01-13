    package com.meizu.cache.redis;  

import java.util.Comparator;
import java.util.List;

import com.meizu.cache.redis.dao.impl.ListRedisDao;
import com.meizu.cache.redis.dao.impl.SearchRedisDao;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.ShardedJedis;
public class RedisTest {

	public static void main(String[] args) {
		testPushListCache();
		System.exit(0);
		
	}
	
	public static void testConnect(){
		ListRedisDao client = new ListRedisDao("redis_ref_hosts");
		client.lpush("test", "test211112223", 60);
		String value = client.lpop("test");
		System.out.println(value);
		System.out.println("-----------------------");
	}
	
	public static void performance1(){
		ListRedisDao client = new ListRedisDao("redis_ref_hosts");
		long begin = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			client.lpush("test", "test"+i, 60);
		}
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
		
	}
	
	 public static void originalCache(){
		 RedisManager client = new RedisManager("redis_ref_hosts");
	    	ShardedJedis jedis = client.getClient();
	    	String key = "aaa";
	    	for(int i=0;i<10;i++){
	    		jedis.sadd(key, String.valueOf(i));
	    	}
	    	System.out.println(jedis.scard(key));
	    	client.returnClient(jedis);
	    	System.out.println("ok");
	    	
	    }
	
	public static void performance2(){
		RedisManager client = new RedisManager("redis_ref_hosts");
		ShardedJedis jedis =  client.getClient();
		long begin = System.currentTimeMillis();
		for(int i=0;i<10000;i++){
			jedis.lpush("test", "test"+i);
		}
		jedis.expire("test", 60);
		client.returnClient(jedis);
		long end = System.currentTimeMillis();
		System.out.println(end-begin);
		
	}
	
	
	public static void testBinarySearch(){
		RedisManager client = new RedisManager("redis_ref_hosts");
		ShardedJedis jedis =  client.getClient();
		String key = "test";
		long begin = System.currentTimeMillis();
		for(int i=0;i<20;i++){
			jedis.lpush(key, i+"");
		}
		jedis.expire(key, 60*5);
		
		long length = jedis.llen(key);
		long index = SearchRedisDao.findCacheIndex(key,jedis,9,0L,length-1,new Comparator<Long>(){

			@Override
			public int compare(Long o1, Long o2) {
				if(o1 > o2){
					return 1;
				}else if(o1 < o1){
					return -1;
				}else{
					return 0;
				}
			}
			
		}
		);
		System.out.println(index);
		
		jedis.del(key);
		client.returnClient(jedis);
	}
	
	public static void testPushListCache(){
		Long[] values = {1L,4L,5L,2L,4L};
		RedisManager client = new RedisManager("redis_ref_hosts");
		ShardedJedis jedis =  client.getClient();
		String key = "test";
		String tmp = jedis.lindex(key, 0);
		long begin =  tmp == null ? 0 : Long.valueOf(tmp);
		tmp = jedis.lindex(key, -1);
		long end = tmp == null ? 0 : Long.valueOf(tmp);
		
		for(long sid :values){
			if(begin < sid){
				jedis.lpush(key, String.valueOf(sid));
				begin = sid;
				if(end==0){
					end = begin;
				}
			}else if(end > sid){
				jedis.rpush(key, String.valueOf(sid));
				end = sid;
			}else{
				long endIndex = jedis.llen(key) == null ? 0 : jedis.llen(key);
				if(endIndex>1){
					endIndex = endIndex - 1;
				}
				long beforeValue = SearchRedisDao.findCacheValueForInsert(key, jedis, sid, 0, endIndex);
//				System.out.println(beforeValue);
				if(beforeValue!=-1){
					jedis.linsert (key, LIST_POSITION.AFTER,String.valueOf(beforeValue), String.valueOf(sid));
				}
				
			}
		}
		List<String> arr = jedis.lrange(key, 0, -1);
		for(String v :arr){
			System.out.println(v);
		}
		jedis.del(key);
		client.returnClient(jedis);
	}
	
}
  