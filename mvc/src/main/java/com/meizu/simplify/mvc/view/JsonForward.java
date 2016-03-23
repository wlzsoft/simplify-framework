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
public  class  JsonForward extends MessageForward {
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	public JsonForward(Object obj) {
		super("");
		//TODO 需要判断是否是集合，如果是集合，判断长度是否是1，如果是1，那么取出来，转成单一一个对象，再转成json
		String message = JsonUtil.ObjectToJson(obj);
		super.setMsg(message);
	}

	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response, WebCache cacheSet, String staticName)
			throws ServletException, IOException {
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("application/json; charset=" + config.getCharset());
		response.getWriter().print(super.getMsg());
	}
}
