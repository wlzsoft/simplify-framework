package vip.simplify.datasource.route;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.dao.datasource.IDataSource;
import vip.simplify.dao.exception.BaseDaoException;
import vip.simplify.datasource.config.HostRouteConfigResolver;

/**
 * <p><b>Title:</b><i>动态数据源连接包装类</i></p>
 * <p>Desc: 使用装饰者模式增强Connection原有方法的功能</p>
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
public class DynamicDataSourceConnectionWrapper implements Connection{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceConnectionWrapper.class);
	
	/**
	 * 是否自动提交事务
	 */
	private boolean isAutoCommit = true;
	
	/**
	 * 当前隔离级别
	 */
	private int iso = -1;
	
	/**
	 * 保存连接上一次事务隔离级别的旧值
	 */
	private int oldIso = -1;
	
	/**
	 * 真实连接
	 */
	private Connection connection;
	
	/**
	 * @param virtualConnection 虚拟连接
	 */
	public DynamicDataSourceConnectionWrapper() {
		
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.isAutoCommit = autoCommit;
		LOGGER.debug("虚拟连接设置是否自动提交事务");
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-无需设置是否自动提交");
			return;
		}
		connection.setAutoCommit(autoCommit);
	}

	@Override
	public boolean getAutoCommit() throws SQLException {
		return connection.getAutoCommit();
	}
	
	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		if(level<0) {
			throw new BaseDaoException("事务隔离级别设置的值不能小于0");
		}
		this.iso = level;
		LOGGER.debug("虚拟连接设置事务隔离级别");
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-无需设置事务隔离级别");
			return;
		}
		if(iso>-1) {
			connection.setTransactionIsolation(iso);
			iso = -1;
		}
	}

	@Override
	public int getTransactionIsolation() throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-无需获取的事务隔离级别，默认返回-1");
			return -1;
		}
		return connection.getTransactionIsolation();
	}
	
	/**
	 * 方法用途: sql路由解析获取相应数据源的连接<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection(String sql) throws SQLException {
		//开始路由选择
		boolean isRead = false;
		if(sql.trim().startsWith("select")) {
			isRead = true;
		}
		IDataSource dataSource = null;
		if(isRead) {
			if(isAutoCommit) {
				dataSource = HostRouteService.switchHost();
			} else {//事务开启，那么走的是写节点去读取数据
				dataSource = HostRouteConfigResolver.writeDataSource;
			}
		} else {
			dataSource = HostRouteConfigResolver.writeDataSource;
		}
//		dataSource.print();
		//结束路由选择
		//连接获取和设置相关条件
		this.connection = dataSource.value().getConnection();
		connection.setAutoCommit(this.isAutoCommit);
		oldIso = connection.getTransactionIsolation();
		if(iso>-1) {
			connection.setTransactionIsolation(iso);
			iso = -1;
		}
		return connection;
	}
	
	@Override
	public String nativeSQL(String sql) throws SQLException {
		getConnection(sql);
		return connection.nativeSQL(sql);
	}
	
	@Override
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql);
	}

	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		getConnection(sql);
		return connection.prepareCall(sql);
	}

	@Override
	public void commit() throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-不提交");
			return;
		}
		if(oldIso>-1) {
			connection.setTransactionIsolation(oldIso);
			oldIso = -1;
		}
		connection.commit();
	}
	
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-不回滚");
			return;
		}
		if(oldIso>-1) {
			connection.setTransactionIsolation(oldIso);
			oldIso = -1;
		}
		connection.rollback(savepoint);		
	}

	@Override
	public void rollback() throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-无需回滚");
			return;
		}
		if(oldIso>-1) {
			connection.setTransactionIsolation(oldIso);
			oldIso = -1;
		}
		connection.rollback();
	}

	@Override
	public void close() throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-无需关闭");
			return;
		}
		connection.close();
	}

	@Override
	public boolean isClosed() throws SQLException {
		if(connection == null) {
			LOGGER.info("虚拟连接已被初始化，但暂未初始化真实连接(无实际查询操作)-非关闭状态");
			return false;
		}
		return connection.isClosed();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return connection.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return connection.isWrapperFor(iface);
	}
	
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		return connection.getMetaData();
	}

	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		connection.setReadOnly(readOnly);
	}

	@Override
	public boolean isReadOnly() throws SQLException {
		return connection.isReadOnly();
	}

	@Override
	public void setCatalog(String catalog) throws SQLException {
		connection.setCatalog(catalog);
	}

	@Override
	public String getCatalog() throws SQLException {
		return connection.getCatalog();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return connection.getWarnings();
	}

	@Override
	public void clearWarnings() throws SQLException {
		connection.clearWarnings();
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		getConnection(sql);
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
	}

	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		return connection.getTypeMap();
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		connection.setTypeMap(map);
	}

	@Override
	public void setHoldability(int holdability) throws SQLException {
		connection.setHoldability(holdability);
	}

	@Override
	public int getHoldability() throws SQLException {
		return connection.getHoldability();
	}

	@Override
	public Savepoint setSavepoint() throws SQLException {
		return connection.setSavepoint();
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);		
	}

	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		return connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
			int resultSetHoldability) throws SQLException {
		getConnection(sql);
		return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql, autoGeneratedKeys);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql, columnIndexes);
	}

	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
		getConnection(sql);
		return connection.prepareStatement(sql, columnNames);
	}

	@Override
	public Clob createClob() throws SQLException {
		return connection.createClob();
	}

	@Override
	public Blob createBlob() throws SQLException {
		return connection.createBlob();
	}

	@Override
	public NClob createNClob() throws SQLException {
		return connection.createNClob();
	}

	@Override
	public SQLXML createSQLXML() throws SQLException {
		return connection.createSQLXML();
	}

	@Override
	public boolean isValid(int timeout) throws SQLException {
		return connection.isValid(timeout);
	}

	@Override
	public void setClientInfo(String name, String value) throws SQLClientInfoException {
		connection.setClientInfo(name, value);		
	}

	@Override
	public void setClientInfo(Properties properties) throws SQLClientInfoException {
		connection.setClientInfo(properties);		
	}

	@Override
	public String getClientInfo(String name) throws SQLException {
		return connection.getClientInfo(name);
	}

	@Override
	public Properties getClientInfo() throws SQLException {
		return connection.getClientInfo();
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
		return connection.createArrayOf(typeName, elements);
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
		return connection.createStruct(typeName, attributes);
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		connection.setSchema(schema);		
	}

	@Override
	public String getSchema() throws SQLException {
		return connection.getSchema();
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		connection.abort(executor);		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
		connection.setNetworkTimeout(executor, milliseconds);		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		return connection.getNetworkTimeout();
	}

}

