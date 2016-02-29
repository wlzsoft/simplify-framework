package com.meizu.simplify.dao.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.meizu.simplify.dao.config.PropertiesConfig;
import com.meizu.simplify.dao.exception.DataAccessException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.utils.PropertieUtil;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: druid 整合dbcp活c3p0</p>
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
	// 线程共享变量,用于事务管理
	public static ThreadLocal<Connection> container = new ThreadLocal<Connection>();
	
	private static final DruidPoolFactory factory = new DruidPoolFactory();
	
	private DruidPoolFactory(){
		createDataSource();
	}
	private DruidDataSource dataSource = null;
	private DruidDataSource createDataSource() {
		try	{
			PropertieUtil result = new PropertieUtil("jdbc-pool.properties");
			dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(result.getProps());
		} catch (Exception e){
			try	{
				if (dataSource != null) {
					dataSource.close();
				}
			} catch (Exception ex)	{
				ex.printStackTrace();
			}
			throw new DataAccessException(e);
		}
		if(dataSource == null) {
			dataSource = new DruidDataSource(); 
			dataSource.setDriverClassName("org.h2.Driver"); 
//			数据库相关:1.数据库配置信息密码要经过加密，不能明文写在配置文件中
//			dataSource.setUsername("root"); 
//			dataSource.setPassword("root"); 
			dataSource.setUrl("jdbc:h2:d:/ms_db/MESSAGE_DATA;MVCC\\=true"); 
			dataSource.setInitialSize(5); 
			dataSource.setMinIdle(1); 
			dataSource.setMaxActive(16); // 启用监控统计功能 
			dataSource.setMaxWait(60000);
			dataSource.setValidationQuery("SELECT 1");
			dataSource.setTestOnBorrow(true);
			dataSource.setTestWhileIdle(true);
			
			//start TODO
			//设置开启后，从连接池获取一个连接，操作1800秒，就移除连接，终止sql执行处理
			//这里设定租期，是为了防止长时间占用连接，忘记归还连接，导致连接池爆了导致连接不够用而设置的，
			//如果有长时间处理的任务，要考虑设置这个时间，一般程序中不会考虑长时间任务，可以分批请求，或是使用其他程序处理
//			dataSource.setRemoveAbandoned(true);
//			dataSource.setRemoveAbandonedTimeout(1800);
			//end
			try {
				dataSource.setFilters("stat");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			dataSource.setPoolPreparedStatements(false);
			
		}
		initPool();
		return dataSource;
	}
	
	
	public static Connection getConnection()   {
		Connection connection = null;
		try {
			if (factory.dataSource != null)  {
				connection = factory.dataSource.getConnection();
			}
			if(connection.isClosed()) {
				throw new DataAccessException("连接已关闭");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		}
		System.out.println("线程["+Thread.currentThread().getName() + "]连接已经开启......");
		container.set(connection);
		return connection;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void startTransaction() {
		//获取当前线程的连接
		Connection conn = container.get();
		if (conn == null) {
			conn = getConnection();
			container.set(conn);
			System.out.println(Thread.currentThread().getName() + "已从数据源中成功获取连接");
		} else {
			System.out.println(Thread.currentThread().getName() + "从缓存中获取连接");
		}
		try {
			//手动提交事务
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法用途: 提交事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void commit() {
		try {
			Connection conn = container.get();
			if (null != conn) {
				conn.commit();
				System.out.println(Thread.currentThread().getName() + "事务已经提交......");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法用途: 回滚事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void rollback() {
		try {
			Connection conn = container.get();
			if (conn != null) {
				conn.rollback();
				container.remove();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 方法用途: 关闭连接-关闭会回收到连接池，是逻辑关闭而不是真正的物理关闭<br>
	 * 操作步骤: TODO<br>
	 */
	public static void close() {
		try {
			Connection conn = container.get();
			if (conn != null) {
				if(!conn.isClosed()) {
					conn.close();
				}
				System.out.println(Thread.currentThread().getName() + "连接关闭");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			try {
				container.remove();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 
	 * 方法用途: 初始化数据源<br>
	 * 操作步骤:  init-method="init"<br>
	 */
	public static void initPool() {
		try {
			factory.dataSource.init();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 方法用途: 关闭数据源<br>
	 * 操作步骤:  destroy-method="close"<br>
	 */
	public static void closePool() {
		factory.dataSource.close();
	}
}

