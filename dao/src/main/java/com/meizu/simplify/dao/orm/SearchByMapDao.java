package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.util.Page;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.DataUtil;
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
	
	private static final Logger logger = LoggerFactory.getLogger(SearchByMapDao.class);
	
	public Integer count(String sql,Object... params) {
		logger.info(sql);//后续不在这里处理sql日志 TODO
		List<Integer> list = SQLExecute.executeQuery(sql, new IDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}

			@Override
			public Integer resultCall(String columnLabel, Object object,Integer t) {
				return DataUtil.parseInt(object);
			}
		},null);
		return list.get(0);
	}
	
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
	
	public Integer executeUpdate(String sql,Object... params) {
		logger.info(sql);
		return SQLExecute.executeUpdate(sql, params);
	}

	public  Page<Map<String, Object>> findPage(Integer currentPage,Integer pageSize,String sql,Object... params) {
		String countSql = sql.substring(sql.toLowerCase().indexOf("from"));
		countSql = countSql.toLowerCase().replaceAll("order\\s*by.*(desc|asc)", "");
		Page<Map<String,Object>> page = new Page<>(currentPage, pageSize, count("select count(1) "+countSql,params));
		List<Map<String,Object>> mapList = find(sql,params);
		page.setResults(mapList);
		return page;
	}
}
