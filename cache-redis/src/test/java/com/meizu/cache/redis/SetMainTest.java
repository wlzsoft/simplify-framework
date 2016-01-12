package com.meizu.cache.redis;

import java.util.HashMap;
import java.util.Map;

import com.meizu.cache.redis.HashCacheClient;


public class SetMainTest {
	public static HashCacheClient client = new HashCacheClient("redis_ref_hosts");
	private static final String LIST_KEY = "appMgr:domainhost:list";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("test", new Object());
		map.put("pppp", new Object());
		System.out.println(client.hmset("testkey", map, 60));

	}

}
