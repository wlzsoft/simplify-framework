package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
  * <p><b>Title:</b><i>回调接口</i></p>
 * <p>Desc: TODO</p>
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
	default T resultCall(String columnLabel, Object object,T t) {
		return null;
	}

	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: 获取数据之前，where条件参数回调<br>
	 * @param prepareStatement
	 * @param params
	 * @return
	 */
	default T paramCall(PreparedStatement prepareStatement,Object... params ) throws SQLException {
		if(params == null) {
			return null;
		}
		for (int i=1; i <= params.length;i++) {
			Object obj = params[i-1];
			prepareStatement.setObject(i, obj);
		}
		return null;
	}
	
	/**
	 * 
	 * 方法用途: 回调方法<br>
	 * 操作步骤: TODO<br>
	 * @param rs
	 * @return
	 */
	default T resultCall(ResultSet rs,Class<?> clazz) {
		try {
			T t = (T) clazz.newInstance();
			return t;
		} catch (InstantiationException e) {
			LOGGER.error("封装查询结果时，实例化对象(" + clazz + ")时，出现异常!"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			LOGGER.error("封装查询结果时，实例化对象(" + clazz + ")时，出现异常!"
					+ e.getMessage());
		}
		return null;
	}
}