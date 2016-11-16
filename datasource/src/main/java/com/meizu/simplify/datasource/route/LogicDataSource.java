package com.meizu.simplify.datasource.route;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.log.Logger;

/**
 * <p><b>Title:</b><i>逻辑数据源</i></p>
 * <p>Desc: 由动态数据源产生，屏蔽物理数据源的细节</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月29日 下午3:00:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月29日 下午3:00:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class LogicDataSource implements DataSource{
	
	@Resource
	private  Logger LOGGER;
	
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		LOGGER.info("getLogWriter");
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		LOGGER.info("setLogWriter");
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		LOGGER.info("setLoginTimeout");
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		LOGGER.info("getLoginTimeout");
		return -1;
	}

	@Override
	public java.util.logging.Logger getParentLogger() throws SQLFeatureNotSupportedException {
		LOGGER.info("getParentLogger");
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		LOGGER.info("unwrap");
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		LOGGER.info("isWrapperFor");
		return false;
	}

	@Override
	public Connection getConnection() throws SQLException {
		return new DynamicDataSourceConnectionWrapper();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return new DynamicDataSourceConnectionWrapper();
	}

}

