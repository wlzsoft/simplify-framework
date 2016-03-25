package com.meizu.simplify.webcache.web;

import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.util.BrowserUtil;


/**
 * <p><b>Title:</b><i>内存模式缓存</i></p>
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
public class MemCache implements Cache {
	
	@Override
	public String readCache(WebCache webCache, String staticName,Object obj) {
		
		try {
			Object[] values = CacheBase.urlCache.get(staticName);
			long time = values != null ? System.currentTimeMillis() - Long.valueOf(values[1].toString()) : -1;
			// 检查存活时间
			if (time > 0 && time < webCache.timeToLiveSeconds() * 1000) {
				if(obj!=null&&webCache.enableBrowerCache()) {
					HttpServletResponse response = (HttpServletResponse) obj;
					BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
				}
				return values[0].toString();
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return null;
	}
	@Override
	public boolean doCache(WebCache webCache, String staticName, String content,Object obj) {
		if(obj!=null&&webCache.enableBrowerCache()) {
			HttpServletResponse response = (HttpServletResponse) obj;
			BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
		}
		CacheBase.urlCache.put(staticName, new Object[] { content, System.currentTimeMillis() });
		return true;
	}
	
}
