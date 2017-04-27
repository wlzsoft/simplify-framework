package vip.simplify.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.ioc.annotation.DefaultBean;
import vip.simplify.utils.ClearCommentUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.webcache.annotation.WebCache;
import vip.simplify.webcache.web.BrowserCache;
import vip.simplify.webcache.web.Cache;
import vip.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>模版处理回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface IPageTemplate {
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName, String templateUrl) throws ServletException, IOException;
	public default void checkCacheAndWrite(HttpServletRequest request, HttpServletResponse response, WebCache webCache,
			String staticName, String content,PropertiesConfig config) throws ServletException, IOException {
		if (webCache != null && webCache.mode() != WebCache.CacheMode.nil) {
			
			BrowserCache.setCache(webCache, response);

			if(webCache.removeSpace()) {
				content = ClearCommentUtil.clear(content);
				content = StringUtil.removeHtmlSpace(content);
			}
			Cache cache = CacheBase.getCache(webCache);
			if(cache != null && cache.setCache(webCache, staticName, content)){
				// 缓存成功
			}
			
		}
		MessageView.exe(request, response, content, config);
	}
	
	/**
	 * 设置内容类型和编码
	 * 
	 * @param request
	 * @param response
	 */
	public default void setContentType(HttpServletRequest request, HttpServletResponse response,PropertiesConfig config) {
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
	}
}
