package com.meizu.simplify.mvc;

import javax.servlet.http.HttpServlet;

/**
 * <p><b>Title:</b><i>Servlet模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午12:59:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午12:59:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class ServletModel {
	Class<HttpServlet> cls;
	private String method = "doView";

	public Class<HttpServlet> getCls() {
		return cls;
	}

	public void setCls(Class<HttpServlet> cls) {
		this.cls = cls;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	public ServletModel(Class<HttpServlet> cls, String method){
		this.cls = cls;
		this.method = method;
	}

}
