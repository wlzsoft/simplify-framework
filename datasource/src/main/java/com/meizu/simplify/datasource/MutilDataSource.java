package com.meizu.simplify.datasource;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.meizu.simplify.dao.datasource.DataSourceFactory;
import com.meizu.simplify.dao.datasource.IDataSource;

/**
 * <p><b>Title:</b><i>多数据源实现</i></p>
 * <p>Desc: TODO</p>
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
public class MutilDataSource implements IDataSource{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MutilDataSource.class);
	
	private DataSource dataSource = null;
	
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MutilDataSource(Properties properties){
		this.dataSource = DataSourceFactory.createDataSource(properties);
	}
	
	@Override
	public DataSource value() {
		return dataSource;
	}
	
	@Override
	public String print() {
		String info = ((DruidDataSource)dataSource).toString();
		LOGGER.info("SQL数据库的激活的数据源信息:"+info);
		return info;
	}
	
	/**
	 * 
	 * 方法用途: 初始化数据源<br>
	 * 操作步骤:  init-method="init"<br>
	 */
	@Override
	public void init() {
		try {
			((DruidDataSource)dataSource).init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 方法用途: 关闭数据源<br>
	 * 操作步骤:  destroy-method="close"<br>
	 */
	@Override
	public void close() {
		((DruidDataSource)dataSource).close();
	}

}

