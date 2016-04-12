package com.meizu.simplify.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.redis.RedisPool;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月3日 上午11:11:55</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月3日 上午11:11:55</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class CachePool {
	private static final Logger LOGGER = LoggerFactory.getLogger(CachePool.class);
	
	
	public static boolean cache = true;//注入,从config.properties 的 cache.switch //是否启用缓存 
	public static boolean jvmcache = false;//注入，从config.properties 的 redis.switch //是否启用redis缓存
	
	public static void init() {
		if (cache) {
			if (jvmcache) {
				LOGGER.info("Framework jvm cached -> ok.");
			} else {
				RedisPool.initCachePool();
				
//				LRU 数据淘汰机制 TODO [LRU 数据淘汰机制,TTL 数据淘汰机制]
//				设置最大使用内存大小 server.maxmemory
//				redis 提供 6种数据淘汰策略：
//				volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
//				volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
//				volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
//				allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
//				allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
//				no-enviction（驱逐）：禁止驱逐数据
//				LOGGER.info("Framework TODO -> ");
//				只有针对key的过期时间设置 [目前这个超时时间的配置，是写成枚举类型了，在这里读取枚举类型的当前启用值 TODO]
				LOGGER.info("Framework key timeout seconds -> ");
			}
		} else {
			LOGGER.info("Framework cached is closed.");
		}
	}
		
}
