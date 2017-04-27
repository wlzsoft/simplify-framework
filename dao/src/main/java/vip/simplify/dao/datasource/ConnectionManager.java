package vip.simplify.dao.datasource;

import java.sql.Connection;

import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;


/**
 * <p><b>Title:</b><i>连接管理</i></p>
 * <p>Desc: 连接相关，当前数据源的连接,后续抽取成独立的连接管理类来管理</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月18日 下午4:12:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月18日 下午4:12:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class ConnectionManager {
	
	@Inject
	private IDataSource dataSource;
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接,如果不存在，创建连接，并从连接池返回<br>
	 * 操作步骤: TODO<br>
	 */
	public Connection getConnection()   {
		Connection connection = ConnectionFactory.getConnection(dataSource.value());
		return connection;
	}
	
	/**
	 * 
	 * 方法用途: 设置事务隔离级别<br>
	 * 操作步骤: TODO<br>
	 * @param iso
	 * @return
	 */
	public int setTransactionISO(int iso) {
		return ConnectionFactory.setTransactionISO(dataSource.value(), iso);
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 * @param iso
	 * @return 设置之前的隔离级别
	 */
	public int startTransaction(int iso) {
		return ConnectionFactory.startTransaction(dataSource.value(),iso);
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 */
	public void startTransaction() {
		ConnectionFactory.startTransaction(dataSource.value());
	}
}

