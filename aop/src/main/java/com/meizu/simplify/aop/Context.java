package com.meizu.simplify.aop;

import com.meizu.simplify.aop.enums.ContextTypeEnum;

/**
  * <p><b>Title:</b><i>aop上下文环境</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午1:57:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午1:57:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Context {
	private ContextTypeEnum type;
	private Object thiz;
	private String methodFullName;
	public void setMethodFullName(String methodFullName) {
		this.methodFullName = methodFullName;
	}

	public void setThis(Object o) {
		this.thiz = o;
	}

	public Object getThiz() {
		return thiz;
	}

	public String getMethodFullName() {
		return methodFullName;
	}
	public ContextTypeEnum getType() {
		return type;
	}

	public void setType(ContextTypeEnum type) {
		this.type = type;
	}
}
