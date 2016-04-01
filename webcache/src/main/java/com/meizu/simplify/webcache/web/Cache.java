package com.meizu.simplify.webcache.web;

import com.meizu.simplify.webcache.annotation.WebCache;



/**
 * <p><b>Title:</b><i>缓存接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午1:00:57</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午1:00:57</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface Cache {
	/**
	 * 
	 * 方法用途: 读取模版缓存内容<br>
	 * 操作步骤: TODO<br>
	 * @param webCache
	 * @param staticName
	 * @return
	 */
	String readCache(WebCache webCache, String staticName);
	
	/**
	 * 
	 * 方法用途: 设置缓存<br>
	 * 操作步骤: TODO<br>
	 * @param webCache
	 * @param staticName
	 * @param content
	 * @return
	 */
	boolean  doCache(WebCache webCache, String staticName, String content);
}
