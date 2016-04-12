package com.meizu.simplify.mvc.dto;

import com.meizu.simplify.mvc.controller.BaseController;

/**
 * <p><b>Title:</b><i>Servlet模型</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 下午12:59:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 下午12:59:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class ControllerAnnotationInfo<T extends BaseController<?>> {
	private T obj;
	
	private String method = "doView";

	public T getObj() {
		return obj;
	}

	public String getMethod() {
		return method;
	}

	public ControllerAnnotationInfo(T obj, String method){
		this.obj = obj;
		this.method = method;
	}

}
