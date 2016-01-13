package com.meizu.cache.redis;

import java.util.HashMap;
import java.util.Map;

import com.meizu.cache.redis.dao.impl.HashCacheDao;


public class SetMainTest {
	public static HashCacheDao client = new HashCacheDao("redis_ref_hosts");
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
