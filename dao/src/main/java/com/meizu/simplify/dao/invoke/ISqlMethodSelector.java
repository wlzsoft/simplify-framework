package com.meizu.simplify.dao.invoke;

import com.meizu.simplify.ioc.annotation.DefaultBean;

/**
  * <p><b>Title:</b><i>sql对应的实体的set和get方法选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:37:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:37:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface ISqlMethodSelector {
	/**
	 * 方法用途: get方法选择器处理<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @param columnName
	 * @return
	 */
	public  Object invoke(Object t,String columnName);
	/**
	 * 方法用途: set方法选择器处理<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @param columnName
	 * @param val
	 */
	public  void invokeSet(Object t,String columnName,Object val);
}
