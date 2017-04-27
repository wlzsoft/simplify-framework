package vip.simplify.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
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
public class MessageView {

	public static void exe(HttpServletRequest request, HttpServletResponse response,String msg, PropertiesConfig config) throws ServletException, IOException {
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
		response.getWriter().print(msg);
	}
}
