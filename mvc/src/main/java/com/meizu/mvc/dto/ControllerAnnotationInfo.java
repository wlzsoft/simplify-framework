package com.meizu.mvc.dto;


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
public class ControllerAnnotationInfo {
	private Object obj;
	
	private String method = "doView";

	public Object getObj() {
		return obj;
	}

	public String getMethod() {
		return method;
	}

	public ControllerAnnotationInfo(Object obj, String method){
		this.obj = obj;
		this.method = method;
	}

}
