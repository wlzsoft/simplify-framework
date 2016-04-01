package com.meizu.simplify.webcache.web;


import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.annotation.WebCache.CacheMode;
import com.meizu.simplify.webcache.util.BrowserUtil;


/**
 * <p><b>Title:</b><i>浏览器缓存</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午12:59:28</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午12:59:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class BrowserCache {
	
	/**
	 * 
	 * 方法用途: 设置浏览器缓存<br>
	 * 操作步骤: 通过response设置返回头信息，来通知浏览器的缓存策略<br>
	 * @param webCache
	 * @param response
	 * @return
	 */
	public static  boolean setCache(WebCache webCache,HttpServletResponse response) {
		if(response == null) {
			return false;
		}
		if(webCache.enableBrowerCache() || webCache.mode().equals(CacheMode.browser)) {
			BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
//			BrowserUtil.enableBrowerCache(response,20000);
		}
		return true;
	}

	
}
