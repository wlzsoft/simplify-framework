package com.meizu.simplify.cache.redis.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.impl.HashRedisDao;

import redis.clients.jedis.ShardedJedis;

public class SetRedisDaoTest {
	public static HashRedisDao client = new HashRedisDao("redis_ref_hosts");
	private static final String LIST_KEY = "appMgr:domainhost:list";

	@Test
	public void test() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", new Object());
		map.put("pppp", new Object());
		System.out.println(client.hmset("testkey", map, 60));

	}

	@Test
	public void originalCache() {
		ShardedJedis jedis = RedisPool.getConnection("redis_ref_hosts");
		String key = "aaa";
		for (int i = 0; i < 10; i++) {
			jedis.sadd(key, String.valueOf(i));
		}
		System.out.println(jedis.scard(key));
		jedis.close();
		System.out.println("ok");

	}

}
