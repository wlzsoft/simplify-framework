package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.util.JsonResolver;
import com.meizu.simplify.utils.JsonUtil;
import com.meizu.simplify.utils.StringUtil;


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
	 * @param obj
	 * @param model
	 * @param domain 通过cookie获取的名字为domain，可以跨域的域名
	 * @throws ServletException
	 * @throws IOException
	 */
	public static <T extends Model> void exe(HttpServletRequest request, HttpServletResponse response,Object obj,T model,String domain,PropertiesConfig config,JsonResolver jsonResolver)
			throws ServletException, IOException {
		
		String message = jsonResolver.ObjectToString(obj);
		//可以通过请求头来限制不合理的请求(request head referer) TODO
		if (model.getScript() == 1) { //form提交到iframe后，自动执行方式,必须在父级域名下，才能调用iframe的js函数
			message = StringUtil.format("<script>document.domain='{0}';{1}({2});</script>", domain, model.getCallback(), message);
//		} else if (model.getScript() == 2) {//通过js 的 document.write 来实现wiget
		} else {
			message = StringUtil.format("{0}({1})", model.getCallback(), message);
		}
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
//		response.setContentType("text/javascript; charset=" + config.getCharset());
		response.getWriter().print(message);
	}
}
