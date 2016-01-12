package com.meizu.cache.redis;

import com.meizu.cache.Constants;
import com.meizu.cache.util.IdentifierGenerator;

public class RedisGenerator implements IdentifierGenerator {
	private static StringCacheClient client = new StringCacheClient(
			Constants.REDIS_INDEX_MOD);

	
	/**
	 * 将 key 中储存的数字值增一。
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作
	 * 
	 */
	public long incr(String key) {
		return client.incr(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public long incrBy(String key, long value) {
		return client.incrBy(key, value);
	}
}
