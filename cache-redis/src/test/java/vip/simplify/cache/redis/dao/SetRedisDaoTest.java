package vip.simplify.cache.redis.dao;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import vip.simplify.cache.redis.CacheExecute;
import vip.simplify.cache.redis.dao.impl.HashRedisDao;

public class SetRedisDaoTest {
	public static HashRedisDao client = new HashRedisDao("redis_ref_hosts");
//	private static final String LIST_KEY = "appMgr:domainhost:list";

	@Test
	public void test() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", new Object());
		map.put("pppp", new Object());
		System.out.println(client.hmset("testkey", map, 60));

	}

	@Test
	public void originalCache() {
		String key = "aaa";
		CacheExecute.execute(key, (k,jedis)->{
			for (int i = 0; i < 10; i++) {
				jedis.sadd(key, String.valueOf(i));
			}
			System.out.println(jedis.scard(key));
			return null;
		},"redis_ref_hosts");
	}

}
