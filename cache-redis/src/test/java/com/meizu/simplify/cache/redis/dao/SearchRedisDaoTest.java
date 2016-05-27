package com.meizu.simplify.cache.redis.dao;

import java.util.Comparator;
import java.util.List;

import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.impl.SearchRedisDao;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.ShardedJedis;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 上午11:18:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 上午11:18:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

public class SearchRedisDaoTest {
	public static void testBinarySearch(){
			ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
			String key = "test";
			long begin = System.currentTimeMillis();
			for(int i=0;i<20;i++){
				jedis.lpush(key, i+"");
			}
			jedis.expire(key, 60*5);
			
			long length = jedis.llen(key);
			long index = new SearchRedisDao("redis_ref_hosts").findCacheIndex(key,9,0L,length-1,new Comparator<Long>(){
	
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
			jedis.close();
			System.out.println("SearchRedisDaoTest.testBinarySearch消耗时间"+(System.currentTimeMillis()-begin));
		}
		
		public static void testPushListCache(){
			Long[] values = {1L,4L,5L,2L,4L};
			ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
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
					long beforeValue = new SearchRedisDao("redis_ref_hosts").findCacheValueForInsert(key, sid, 0, endIndex);
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
			jedis.close();
		}
}
