package com.meizu.simplify.dao.orm.base;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
public interface ISqlDataCallback<T> extends IDataCallback<T> {

	static final Logger LOGGER = LoggerFactory.getLogger(ISqlDataCallback.class);

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
			LOGGER.info("[参数索引:"+i+",值:"+obj+"]");
		}
		return null;
	}
	
}