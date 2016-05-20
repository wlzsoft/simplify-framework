package com.meizu.simplify.webcache.web;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.utils.collection.FiFoMap;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>缓存基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:49</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class CacheBase {
	private static PropertiesConfig config;
	public static FiFoMap<String, Object[]> urlPageCacheMap; 
	public static void init() {
		config = BeanFactory.getBean(PropertiesConfig.class);
		urlPageCacheMap = new FiFoMap<String, Object[]>(config.getPageCacheCount()); // url请求页面缓存,对页面的缓存记录方式做了先进先出模式
	}
	/**
	 * 
	 * 方法用途: 获取具体页面缓存操作机制实现类<br>
	 * 操作步骤: TODO<br>
	 * @param webCache
	 * @return
	 */
	public static Cache getCache(WebCache webCache) {
		Cache cache = null;
		if(config.getDebug()) {
			return null; 
		}
		if (WebCache.CacheMode.Mem == webCache.mode()) {
			cache = new MemCache();
		} else if (WebCache.CacheMode.File == webCache.mode()) {
			cache = new FileCache();
		} else if (WebCache.CacheMode.browser == webCache.mode()) {
		}
		return cache;
	}
}
