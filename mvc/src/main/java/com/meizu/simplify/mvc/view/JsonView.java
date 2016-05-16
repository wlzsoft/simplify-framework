package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.cache.redis.util.JsonResolver;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.utils.JsonUtil;


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
public  class  JsonView {

	public static void exe(HttpServletRequest request, HttpServletResponse response,Object obj,PropertiesConfig config,JsonResolver jsonResolver)
			throws ServletException, IOException {
		String message = jsonResolver.ObjectToString(obj);
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("application/json; charset=" + config.getCharset());
//		TODO 注意使用下面的方式，可能设置头信息(addHead) ,会丢失，无法发送到浏览器的reponse头中
		response.getWriter().print(message);
	}
}
