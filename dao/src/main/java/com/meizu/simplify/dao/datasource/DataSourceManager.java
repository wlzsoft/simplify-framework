package com.meizu.simplify.dao.datasource;

import java.sql.Connection;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;


/**
 * <p><b>Title:</b><i>数据源生命周期管理</i></p>
 * <p>Desc: TODO</p>
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
public class DataSourceManager {
	
	@Config("system.debug")
	private Boolean debug;
	
	@Resource
	private IDataSource dataSource;
	
	@InitBean
	public void init(){
		if(debug) {
			dataSource.print();
		}
		
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接,如果不存在，创建连接，并从连接池返回<br>
	 * 操作步骤: TODO<br>
	 */
	public  Connection getConnection()   {
		Connection connection = ConnectionFactory.getConnection(dataSource.value());
		return connection;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 */
	public  void startTransaction() {
		ConnectionFactory.startTransaction(dataSource.value());
	}
	
	public  void close() {
		dataSource.close();
	}
}

