package com.meizu.simplify.datasource.route;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.meizu.simplify.dao.datasource.DataSourceFactory;
import com.meizu.simplify.dao.datasource.IDataSource;

/**
 * <p><b>Title:</b><i>动态数据源</i></p>
 * <p>Desc: 用于屏蔽具体数据源连接池的实现</p>
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
public class DynamicDataSource implements IDataSource{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSource.class);
	
	private DataSource dataSource = null;
	
	private String name;
	
	/**
	 * 数据源类型：{0:master数据源,1:slave数据源}
	 */
	private Integer type;
	
	public DynamicDataSource(Properties properties){
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
	
	//set和get方法
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * 方法用途: 数据源类型<br>
	 * 操作步骤: TODO<br>
	 * @return {0:master数据源,1:slave数据源}
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 
	 * 方法用途: 数据源类型<br>
	 * 操作步骤: TODO<br>
	 * @param type {0:master数据源,1:slave数据源}
	 */
	public void setType(Integer type) {
		this.type = type;
	}

}

