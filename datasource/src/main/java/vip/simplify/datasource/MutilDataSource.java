package vip.simplify.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

import javax.sql.DataSource;

import vip.simplify.dao.datasource.IDataSource;
import vip.simplify.datasource.route.DynamicDataSourceConnectionWrapper;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.log.Logger;

/**
 * <p><b>Title:</b><i>多数据源实现(逻辑数据源)</i></p>
 * <p>Desc: 由动态数据源产生，屏蔽物理数据源的细节</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月14日 上午11:14:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月14日 上午11:14:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class MutilDataSource implements IDataSource,DataSource{

	@Override
	public DataSource value() {
		return this;
	}

	@Override
	public String print() {
		return null;
	}

	@Override
	public void init() {
	}

	@Override
	public void close() {
	}

	@Override
	public String getName() {
		return null;
	}
	
	@Inject
	private Logger LOGGER;
	
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
		DynamicDataSourceConnectionWrapper connection = new DynamicDataSourceConnectionWrapper();
		return connection;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		DynamicDataSourceConnectionWrapper connection = new DynamicDataSourceConnectionWrapper();
		return connection;
	}

}
