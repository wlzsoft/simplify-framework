package com.meizu.simplify.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
  * <p><b>Title:</b><i>基于Map的缓存容器实现</i></p>
 * <p>Desc: TODO和caches模块的MapCache需要合并</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月31日 下午7:04:14</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月31日 下午7:04:14</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class CacheManager {
	
	private ConcurrentMap<String,Map<String,?>> container = new ConcurrentHashMap<>();
	private static CacheManager cacheManager = new CacheManager();
	private CacheManager() {}
	public static Map<String,?> getCache(String key) {
		Map<String,?> valueMap = cacheManager.container.get(key);
		if(valueMap == null) {
			valueMap= new ConcurrentHashMap<>();
			cacheManager.container.put(key, valueMap);
		}
		return valueMap;
	}
	public static void addCache(String key,Map<String,?> value) {
		Map<String,?> valueMap = cacheManager.container.get(key);
		if(valueMap == null) {
			cacheManager.container.put(key, value);
		}
	}
}
