package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.annotation.Bean;
/**
 * <p><b>Title:</b><i>基于map类型结果集的基础dao实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午11:35:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午11:35:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class SearchByMapDao {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	public List<Map<String,Object>> find(String sql,Object... params) {
		logger.info(sql);
		List<Map<String,Object>> tList = SQLExecute.executeQuery(sql, new IDataCallback<Map<String,Object>>() {
			@Override
			public Map<String,Object> paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}
			@Override
			public Map<String,Object> resultCall(String columnLabel, Object val,Map<String,Object> t) {
				if(t == null) {
					t = new HashMap<>();
				}
				t.put(columnLabel, val);
				return t;
			}
		},null);
		return tList;
	}
}
