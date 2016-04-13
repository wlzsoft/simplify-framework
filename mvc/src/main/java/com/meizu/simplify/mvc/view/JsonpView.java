package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.cache.redis.util.JsonUtil;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>json处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public  class  JsonpView {

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param webCache
	 * @param staticName
	 * @param obj
	 * @param model
	 * @param domain 通过cookie获取的名字为domain，可以跨域的域名
	 * @throws ServletException
	 * @throws IOException
	 */
	public static <T extends Model> void exe(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,Object obj,T model,String domain,PropertiesConfig config)
			throws ServletException, IOException {
		String message = JsonUtil.ObjectToString(obj);
		if (model.getScript() == 1) { 
			message = StringUtil.format("<script>document.domain='{0}';{1}({2})</script>", domain, model.getCallback(), message);
		} else {
			message = StringUtil.format("{0}({1})", model.getCallback(), message);
		}
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/javascript; charset=" + config.getCharset());
		response.getWriter().print(message);
	}
}
