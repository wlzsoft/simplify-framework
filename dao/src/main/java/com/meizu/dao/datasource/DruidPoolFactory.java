package com.meizu.dao.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月29日 下午3:00:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月29日 下午3:00:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class DruidPoolFactory {
	private static class  DataSource {
		private static DruidDataSource dataSource = new DruidDataSource(); 
		private DataSource() {
			dataSource.setDriverClassName("org.h2.Driver"); 
//			dataSource.setUsername("root"); 
//			dataSource.setPassword("root"); 
			dataSource.setUrl("jdbc:h2:d:/ms_db/MESSAGE_DATA;MVCC\\=true"); 
			dataSource.setInitialSize(5); 
			dataSource.setMinIdle(1); 
			dataSource.setMaxActive(16); // 启用监控统计功能 
			dataSource.setTestOnBorrow(true);
			try {
				dataSource.setFilters("stat");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			dataSource.setPoolPreparedStatements(false);
		}
		public static DruidDataSource getDataSource() {
			return dataSource;
		}
	}
	public static Connection getConnection() throws SQLException  {
		return DataSource.getDataSource().getConnection();
	}
}
