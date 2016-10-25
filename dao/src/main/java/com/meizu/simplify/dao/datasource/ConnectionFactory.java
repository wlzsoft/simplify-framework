package com.meizu.simplify.dao.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.exception.DataAccessException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: druid 整合dbcp活c3p0
 *          说明：1.Druid针对Oracle和MySql做了特别优化，比如Oracle的PS Cache内存占用优化，MySql的ping检测优化。 Druid可与其他数据库连接池集成，druid只作为连接池代理
				2.Druid 阿里连接池： 支持特性：LRU，PSCache,PSCache-Oracle-Optimized,ExceptionSorter,监控,扩展 ，推荐使用 
				3.问题分析参考地址：https://github.com/alibaba/druid/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98
				4.项目作者访谈问题解答地址：http://www.iteye.com/magazines/90</p>
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
public class ConnectionFactory {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionFactory.class);
	
	// 线程共享变量,用于事务管理
	public static final ThreadLocal<Connection> container = new ThreadLocal<>();
	
	private ConnectionFactory(){
	}
	
	
	public static Connection getConnection(javax.sql.DataSource dataSource)   {
		Connection connection = container.get();
		if (connection != null) {
			return connection;
		}
		try {
			if (dataSource != null)  {
				connection = dataSource.getConnection();
			}
			if(connection.isClosed()) {
				LOGGER.info("连接已关闭");
				throw new DataAccessException("连接已关闭");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		}
		LOGGER.debug("线程["+Thread.currentThread().getName() + "]连接已经开启......");
		container.set(connection);
		return connection;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void startTransaction(javax.sql.DataSource dataSource) {
		//获取当前线程的连接
		Connection conn = container.get();
		if (conn == null) {
			conn = getConnection(dataSource);
			container.set(conn);
			LOGGER.info(Thread.currentThread().getName() + "已从数据源中成功获取连接");
		} else {
			LOGGER.info(Thread.currentThread().getName() + "从缓存中获取连接");
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
		commit(null);
	}
	
	/**
	 * 
	 * 方法用途: 提交事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void commit(Integer transactionISO) {
		try {
			Connection conn = container.get();
			if (null != conn) {
				conn.commit();
				conn.setAutoCommit(true);//开启事务自动提交，无需干预
				if(transactionISO!=null) {
					try {
						conn.setTransactionIsolation(transactionISO);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				LOGGER.info(Thread.currentThread().getName() + "事务已经提交......");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	/**
	 * 
	 * 方法用途: 回滚事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void rollback() {
		rollback(null);
	}
	/**
	 * 
	 * 方法用途: 回滚事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void rollback(Integer transactionISO) {
		try {
			Connection conn = container.get();
			if (conn != null) {
				conn.rollback();
				conn.setAutoCommit(true);//开启事务自动提交，无需干预
				if(transactionISO!=null) {
					try {
						conn.setTransactionIsolation(transactionISO);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				LOGGER.info(Thread.currentThread().getName() + "事务已经回滚......");
				container.remove();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
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
				LOGGER.debug(Thread.currentThread().getName() + "连接关闭");
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
}

