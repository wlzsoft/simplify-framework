package com.meizu.simplify.dao.invoke;

import com.meizu.simplify.ioc.annotation.DefaultBean;

/**
  * <p><b>Title:</b><i>方法选择器</i></p>
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
	public  Object invoke(Object t,String columnName);
	public  void invokeSet(Object t,String columnName,Object val);
}
