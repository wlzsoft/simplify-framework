package com.meizu.simplify.dao.orm.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * <p><b>Title:</b><i>回调接口</i></p>
 * <p>Desc: 上级数据回调接口，和具体的数据实现无关，可以是关系型数据库或是其他非关系信息数据等等的处理</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月17日 上午10:06:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月17日 上午10:06:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IDataCallback<T> {

	static final Logger LOGGER = LoggerFactory.getLogger(IDataCallback.class);

	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: 获取数据库中数据后，结果集或是结果集回调<br>
	 * @param columnLabel
	 * @param object
	 * @param t
	 * @return
	 */
	default  T resultCall(String columnLabel, Object object,T t) {
		if(t == null) {
			return null;
		}
		return t;
	}

}