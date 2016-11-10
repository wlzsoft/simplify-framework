package com.meizu.simplify.dao.datasource;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * <p><b>Title:</b><i>单数据源实现</i></p>
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
public class SingleDataSource implements IDataSource{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SingleDataSource.class);
	
	private static final SingleDataSource singDataSource = new SingleDataSource();
	
	private DataSource dataSource = null;
	
	
	public static IDataSource getDataSource() {
		return singDataSource;
	}
	
	private SingleDataSource(){
		this.dataSource = DataSourceFactory.createDataSource();
	}
	
	@Override
	public DataSource value() {
		return dataSource;
	}
	
	@Override
	public String print() {
		String info = ((DruidDataSource)dataSource).getProperties().toString();
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

