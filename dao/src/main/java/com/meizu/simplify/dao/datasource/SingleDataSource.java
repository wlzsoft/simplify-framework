package com.meizu.simplify.dao.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.PropertieUtil;

/**
 * <p><b>Title:</b><i>单数据源实现</i></p>
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
@Bean
public class SingleDataSource implements IDataSource{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SingleDataSource.class);
	
	private DataSource dataSource = null;
	
	public SingleDataSource(){
		PropertieUtil result = new PropertieUtil("jdbc-pool.properties");
		this.dataSource = DataSourceFactory.createDataSource(result.getProps());
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

	@Override
	public String getName() {
		return "SingleDataSource";
	}

}

