package vip.simplify.mvc.util;
/**
  * <p><b>Title:</b><i>未使用</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午3:43:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午3:43:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class HttpRequestPool {

	/**
	 * 
	 * 方法用途: 从池中获取request对象<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @return
	 */
	public static <T>  T getRequest(Class<T> clazz) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 从池中获取request对象，默认Object类型<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static Object getRequest() {
		return getRequest(Object.class);
	}

}
