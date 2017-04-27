package vip.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.dao.datasource.ConnectionManager;
import vip.simplify.dao.orm.base.ISqlDataCallback;
import vip.simplify.dao.orm.base.SQLExecute;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.DataUtil;

/**
 * <p><b>Title:</b><i>sql的count语句公用dao封装</i></p>
 * <p>Desc: TODO暂未实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月31日 下午5:45:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月31日 下午5:45:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class CountDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CountDao.class);
	
	public static Integer count(ConnectionManager connectionManager, String sql, Object... params) {
		LOGGER.info(sql);//后续不在这里处理sql日志 TODO
		List<Integer> list = SQLExecute.executeQuery(connectionManager,sql, new ISqlDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return ISqlDataCallback.super.paramCall(prepareStatement,params);
			}

			@Override
			public Integer resultCall(String columnLabel, Object object,Integer t) {
				return DataUtil.parseInt(object);
			}
		},null);
		if(CollectionUtil.isEmpty(list)) {
			return 0;
		}
		return list.get(0);
	}
	
	/**
	 * 
	 * 方法用途: 构建Count语句<br>
	 * 操作步骤: TODO<br>
	 * @param connectionManager
	 * @param sql
	 * @param params
	 * @return
	 */
	public static Integer buildCountSql(ConnectionManager connectionManager,String sql, Object... params) {
		String countSql = "";
		String lowerCaseSql = sql.toLowerCase();
		if(lowerCaseSql.indexOf("distinct")>-1) {
			countSql = "select count(1) from ("+sql+") t";
		} else {
			countSql = sql.substring(lowerCaseSql.indexOf("from"));
			countSql = "select count(1) "+countSql;
		}
		countSql = countSql.replaceAll("order\\s*by.*(desc|asc)", "");
		Integer count = count(connectionManager,countSql, params);
		return count;
	}
}
