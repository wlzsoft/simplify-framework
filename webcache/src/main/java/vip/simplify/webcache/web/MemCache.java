package vip.simplify.webcache.web;

import vip.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>内存模式缓存</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午12:59:28</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午12:59:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class MemCache implements Cache {
	
	@Override
	public String readCache(WebCache webCache, String staticName) {
		
		try {
			Object[] values = CacheBase.urlPageCacheMap.get(staticName);
			long time = values != null ? System.currentTimeMillis() - Long.valueOf(values[1].toString()) : -1;
			// 检查存活时间
			if (time > 0 && time < webCache.timeToLiveSeconds() * 1000) {
				return values[0].toString();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean setCache(WebCache webCache, String staticName, String content) {
		CacheBase.urlPageCacheMap.put(staticName, new Object[] { content, System.currentTimeMillis() });
		return true;
	}
	
}
