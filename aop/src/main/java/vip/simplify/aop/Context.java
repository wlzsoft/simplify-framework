package vip.simplify.aop;

import vip.simplify.aop.enums.ContextTypeEnum;

/**
  * <p><b>Title:</b><i>aop上下文环境</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午1:57:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午1:57:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Context {
	private ContextTypeEnum type;
	//当前业务对象-一般是xxxController或是xxxService的对象
	private Object thiz;
	//方法全名
	private String methodFullName;
	//结果值-注意：这个值是请求共享变量，在同一个请求中，值的引用是同一个
	private InterceptResult result;

	public Context(InterceptResult result) {
		this.result = result;
	}
	
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

	/**
	 * 
	 * 方法用途: 获取链条环境中的结果值<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public InterceptResult getCallback() {
		return result;
	}
}
