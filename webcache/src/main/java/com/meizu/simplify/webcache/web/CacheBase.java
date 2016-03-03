package com.meizu.simplify.webcache.web;

import com.meizu.simplify.utils.PropertieUtil;
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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class CacheBase {
	private static PropertieUtil config = new PropertieUtil("properties/config.properties");//注意，要合并成一个实例，不要太多重复实例 TODO
	public static FiFoMap<String, Object[]> urlCache = new FiFoMap<String, Object[]>(config.getInteger("system.urlcacheCount", 100)); // url请求缓存,对urlcache的缓存记录方式做了先进先出模式
	/**
	 * 取缓冲器
	 * 
	 * @param cacheSet
	 * @return
	 */
	public static Cache getCache(WebCache cacheSet) {
		Cache cache = null;
//		if(MvcInit.debug)return null; //TODO 从配置文件中读取,后续打开
		if (WebCache.CacheMode.Mem == cacheSet.mode()) {
			cache = new MemCache();
		} else if (WebCache.CacheMode.File == cacheSet.mode()) {
			cache = new FileCache();
		} else if (WebCache.CacheMode.browser == cacheSet.mode()) {
			cache = new BrowserCache();
		}
		return cache;
	}
}
