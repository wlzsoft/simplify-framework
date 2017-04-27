package vip.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.dao.datasource.ConnectionManager;
import vip.simplify.dao.orm.base.CommonSqlBuilder;
import vip.simplify.dao.orm.base.ISqlDataCallback;
import vip.simplify.dao.orm.base.SQLExecute;
import vip.simplify.entity.page.Page;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;

/**
 * <p><b>Title:</b><i>基于map类型结果集的基础dao实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午11:35:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午11:35:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class SearchByMapDao {
	
	private static final Logger logger = LoggerFactory.getLogger(SearchByMapDao.class);
	
	@Inject
	private ConnectionManager connectionManager;
	
	public List<Map<String,Object>> find(String sql,Object... params) {
		logger.info(sql);
		List<Map<String,Object>> tList = SQLExecute.executeQuery(connectionManager,sql, new ISqlDataCallback<Map<String,Object>>() {
			@Override
			public Map<String,Object> paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return ISqlDataCallback.super.paramCall(prepareStatement,params);
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
		return SQLExecute.executeUpdate(connectionManager,sql, params);
	}

	/**
	 * 
	 * 方法用途: 分页查询结果集-结果集存储于Map结构中<br>
	 * 操作步骤: 不支持排序<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sql
	 * @param isReturnLastPage
	 * @param params
	 * @return
	 */
	public  Page<Map<String, Object>> findPage(Integer currentPage,Integer pageSize,String sql,boolean isReturnLastPage,Object... params) {
		return findPage(currentPage, pageSize, null, null, sql, isReturnLastPage, params);
	}
	
	/**
	 * 
	 * 方法用途: 分页查询结果集-结果集存储于Map结构中<br>
	 * 操作步骤: 支持排序<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sort
	 * @param isDesc
	 * @param sql
	 * @param isReturnLastPage
	 * @param params
	 * @return
	 */
	public  Page<Map<String, Object>> findPage(Integer currentPage,Integer pageSize,String sort, Boolean isDesc,String sql,boolean isReturnLastPage,Object... params) {
		Integer count = CountDao.buildCountSql(connectionManager,sql, params);
		Page<Map<String,Object>> page = new Page<>(currentPage, pageSize, count,isReturnLastPage);
		
		String endSearchSql = CommonSqlBuilder.buildEndSearchSql(page.getCurrentRecord(), pageSize, sort, isDesc).toString();
		
		List<Map<String,Object>> mapList = find(sql+endSearchSql,params);
		page.setResults(mapList);
		return page;
	}
	
	public  Page<Map<String, Object>> findPage(Integer currentPage,Integer pageSize,String sql,Object... params) {
		return findPage(currentPage, pageSize, sql, true, params);
	}

	/**
	 * 
	 * 方法用途: 通用count语句执行<br>
	 * 操作步骤: 注意：后续这个方法的位置会调整，这个方法不完全属于这个类<br>
	 * @param sql
	 * @param params
	 * @return
	 */
	public Integer count(String sql,Object... params) {
		return CountDao.count(connectionManager, sql, params);
	}
}
