package com.meizu.simplify.mvc.dto;

import com.meizu.simplify.webcache.annotation.WebCache;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月25日 下午6:44:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月25日 下午6:44:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class WebCacheInfo {
	private WebCache webcache;
	private Boolean isCache;
	public WebCache getWebcache() {
		return webcache;
	}
	public void setWebcache(WebCache webcache) {
		this.webcache = webcache;
	}
	public Boolean getIsCache() {
		return isCache;
	}
	public void setIsCache(Boolean isCache) {
		this.isCache = isCache;
	}
	
}
