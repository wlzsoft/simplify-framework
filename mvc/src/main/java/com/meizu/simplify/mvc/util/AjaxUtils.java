package com.meizu.simplify.mvc.util;

import javax.servlet.http.HttpServletRequest;

/**
 * <p><b>Title:</b><i>ajax工具类</i></p>
 * <p>Desc: TODO：暂未使用，暂未测试</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午3:07:46</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午3:07:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class AjaxUtils {
	
	private static final String X_REQUESTED_WITH = "X-Requested-With";
	
	private AjaxUtils() {}
	
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader(X_REQUESTED_WITH);
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	public static boolean isAjaxUploadRequest(HttpServletRequest request) {
		return request.getParameter("ajaxUpload") != null;
	}
	

}
