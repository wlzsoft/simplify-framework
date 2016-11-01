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
	
	private Object temp;

	@SuppressWarnings("unchecked")
	public <T extends Object> T getResult() {
		return (T) result;
	}

	/**
	 * 
	 * 方法用途: 设置拦截器的结果集<br>
	 * 操作步骤: TODO<br>
	 * @param result 用于设置返回的结果集，注意不是一个临时变量，不能随意存放数据。否则会有未知异常.
	 * @return
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	@SuppressWarnings("unchecked")
	public <T extends Object> T getTemp() {
		return (T) temp;
	}

	/**
	 * 
	 * 方法用途: 设置拦截器的临时变量<br>
	 * 操作步骤: TODO<br>
	 * @param temp 临时变量，可随意存放数据。但是要注意不要滥用，避免大对象.
	 * @return
	 */
	public void setTemp(Object temp) {
		this.temp = temp;
	}
}
