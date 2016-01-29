package com.meizu.simplify.webcache.web;

import com.meizu.simplify.webcache.annotation.CacheSet;


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
	
	/**
	 * 取缓冲器
	 * 
	 * @param cacheSet
	 * @return
	 */
	public static Cache getCache(CacheSet cacheSet) {
		Cache cache = null;
//		if(Init.test_switch)return null; //TODO 从配置文件中读取,后续打开
		if (CacheSet.CacheMode.Mem == cacheSet.mode()) {
			cache = new MemCache();
		} else if (CacheSet.CacheMode.File == cacheSet.mode()) {
			cache = new FileCache();
		} else if (CacheSet.CacheMode.browser == cacheSet.mode()) {
			cache = new BrowserCache();
		}
		return cache;
	}
}
