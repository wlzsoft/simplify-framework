package com.meizu.simplify.dao.orm;

/**
  * <p><b>Title:</b><i>sql语句回调接口-sql生成处理回调器</i></p>
 * <p>Desc: 获取数据库中数据之前，会回调次接口的call方法</p>
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
public interface IParamCallback<T> {

	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: TODO<br>
	 * @param columnLabel
	 * @param object
	 * @return
	 */
	T call(Object object);

}
