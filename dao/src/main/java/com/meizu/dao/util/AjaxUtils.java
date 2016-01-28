package com.meizu.dao.util;

import com.meizu.dao.HttpServletRequest;

public class AjaxUtils {
	
	private static final String X_REQUESTED_WITH = "X-Requested-With";
	
	private AjaxUtils() {}
	
	public static boolean isAjaxRequest(HttpServletRequest webRequest) {
		String requestedWith = webRequest.getHeader(X_REQUESTED_WITH);
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	public static boolean isAjaxUploadRequest(HttpServletRequest webRequest) {
		return webRequest.getParameter("ajaxUpload") != null;
	}
	

}
