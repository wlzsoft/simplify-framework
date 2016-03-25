package com.meizu.simplify.webcache.web;


import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.webcache.annotation.WebCache;
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
public class BrowserCache implements Cache {
	
	@Override
	public String readCache(WebCache webCache, String staticName,Object obj) {
		//浏览器缓存的读取操作由浏览器自己完成，无需程序员控制。 TODO
		return null;
	}

	/* 
	 * CacheAspect 类的浏览器缓存设置不起作用
	 */
	public boolean doCache(WebCache webCache, String staticName, String content,Object obj) {
		if(obj == null) {
			return true;
		}
		
		HttpServletResponse response = (HttpServletResponse) obj;
		BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
//		BrowserUtil.enableBrowerCache(response,20000);
		return true;
	}

	
}
