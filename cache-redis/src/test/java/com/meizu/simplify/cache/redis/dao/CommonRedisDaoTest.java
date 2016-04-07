    package com.meizu.simplify.cache.redis.dao;  

import org.junit.Test;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;
import com.meizu.simplify.stresstester.StressTestUtils;
import com.meizu.simplify.stresstester.core.StressTask;

import redis.clients.jedis.ShardedJedis;
public class CommonRedisDaoTest {
	
	ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	@Test
	public void testInsertStress() {
		StressTestUtils.testAndPrint(1000, 100000, new StressTask(){
			@Override
			public Object doTask() throws Exception {
					ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
					jedis.set("age", "3");
					System.out.println(jedis.get("age"));
				return null;
			}
		});
	}
	
	@Test
	public void testGet() {
		commonRedisDao.set("age", 2);
		System.out.println(commonRedisDao.get("age"));
	}
}
  