package com.meizu.data.util;

import com.meizu.data.exception.HttpServletRequest;

public class AjaxUtils {
//	protected static final String X_REQUESTED_WITH = "x-requested-with";
	protected static final String X_REQUESTED_WITH = "X-Requested-With";
	public static boolean isAjaxRequest(HttpServletRequest webRequest) {
		String requestedWith = webRequest.getHeader(X_REQUESTED_WITH);
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}

	public static boolean isAjaxUploadRequest(WebRequest webRequest) {
		return webRequest.getParameter("ajaxUpload") != null;
	}
	
	private AjaxUtils() {}

}
