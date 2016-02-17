package com.meizu.simplify.dao.orm;

/**
  * <p><b>Title:</b><i>数据回调接口</i></p>
 * <p>Desc: 获取数据库中数据后，会回调次接口的call方法</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月17日 上午10:06:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月17日 上午10:06:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IDataCallback<T> {

	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: TODO<br>
	 * @param columnLabel
	 * @param object
	 * @return
	 */
	T call(String columnLabel, Object object);

	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: 剥离到单独的接口中<br>
	 * @param rs
	 * @return
	 */
//	T call(ResultSet rs);
}
