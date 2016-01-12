package com.meizu.cache.redis;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.meizu.cache.ICacheManager;
import com.meizu.cache.impl.Cache;



/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午5:47:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午5:47:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class RedisCacheManager implements ICacheManager {

	  private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>();
	  private Map<String, Integer> expireMap = new HashMap<String, Integer>();   //缓存的时间
	  private RedisClient redisClient;   

	  public RedisCacheManager() {
	  }

	  @Override
	  public Collection<? extends Cache> loadCaches() {
	    Collection<Cache> values = cacheMap.values();
	    return values;
	  }

	  @Override
	  public Cache getCache(String name) {
	    Cache cache = cacheMap.get(name);
	    if (cache == null) {
	      Integer expire = expireMap.get(name);
	      if (expire == null) {
	        expire = 0;
	        expireMap.put(name, expire);
	      }
	      cache = new RedisCache(name, expire.intValue(), redisClient);
	      cacheMap.put(name, cache);
	    }
	    return cache;
	  }

	  public void setredisClient(RedisClient redisClient) {
	    this.redisClient = redisClient;
	  }

	  public void setConfigMap(Map<String, Integer> configMap) {
	    this.expireMap = configMap;
	  }

	}
