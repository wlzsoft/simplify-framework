package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

/**
 * <p><b>Title:</b><i>错误页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:01</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:01</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ErrorView {

	public static void exe(HttpServletResponse response,String msg) throws IOException  {
		response.sendError(403, msg);
	}
	public static void exe(HttpServletResponse response,int errorCode,String msg) throws  IOException {
		response.sendError(errorCode, msg);
	}
}
