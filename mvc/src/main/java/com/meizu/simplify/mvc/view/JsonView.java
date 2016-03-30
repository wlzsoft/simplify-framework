package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.cache.redis.util.JsonUtil;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public  class  JsonView {

	public static void doAction(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,Object obj)
			throws ServletException, IOException {
		PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
		String message = JsonUtil.ObjectToString(obj);
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("application/json; charset=" + config.getCharset());
		response.getWriter().print(message);
	}
}
