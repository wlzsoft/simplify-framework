package com.meizu.simplify.aop;
/**
  * <p><b>Title:</b><i>结果值-注意：这个值是请求共享变量，在同一个请求中，值的引用是同一个</i></p>
 * <p>Desc: 用于拦截器中的中间结果值保持</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月29日 下午2:59:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月29日 下午2:59:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class InterceptResult {
	private Object result;

	@SuppressWarnings("unchecked")
	public <T extends Object> T getResult() {
		return (T) result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
