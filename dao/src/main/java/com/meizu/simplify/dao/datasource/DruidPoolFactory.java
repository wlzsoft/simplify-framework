package com.meizu.simplify.dao.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.meizu.simplify.dao.exception.DataAccessException;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.utils.PropertieUtil;

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
public class DruidPoolFactory {
	
	private String dataSourceStr = "";
	
	public String getDataSourceStr() {
		return dataSourceStr;
	}

	public void setDataSourceStr(String dataSourceStr) {
		this.dataSourceStr = dataSourceStr;
	}
	
	public static String getDataSourceInfo()   {
		return factory.getDataSourceStr();
	}

	private static final DruidPoolFactory factory = new DruidPoolFactory();
	
	private javax.sql.DataSource dataSource = null;
	
	private DruidPoolFactory(){
		this.dataSource = createDataSource();
	}
	
	private javax.sql.DataSource createDataSource() {
		DruidDataSource dataSource = null;
		PropertieUtil result = new PropertieUtil("jdbc-pool.properties");
		try	{
			dataSource = new DruidDataSource();
			//默认配置信息设置
//			数据源驱动类driverClassName可不写，Druid默认会自动根据URL识别DriverClass
//			dataSource.setDriverClassName("org.h2.Driver"); 
//			数据库相关:1.数据库配置信息密码要经过加密，不能明文写在配置文件中
//			dataSource.setUsername("root"); 
//			dataSource.setPassword("123456"); 
			dataSource.setUrl("jdbc:h2:d:/test_db/TEST_DATA;MVCC\\=true"); 
			dataSource.setInitialSize(5); 
			dataSource.setMinIdle(3); 
			dataSource.setMaxActive(20); // 启用监控统计功能 
			dataSource.setMaxWait(6000);

			//start TODO
			//设置开启后，从连接池获取一个连接，操作1800秒，就移除连接，终止sql执行处理
			//这里设定租期，是为了防止长时间占用连接，忘记归还连接，导致连接池爆了导致连接不够用而设置的，
			//如果有长时间处理的任务，要考虑设置这个时间，一般程序中不会考虑长时间任务，可以分批请求，或是使用其他程序处理
			//禁用对于长时间不使用的连接强制关闭的功能,以下配置会影响性能，如非真存在连接泄露，不要打开，https://github.com/alibaba/druid/wiki/%E8%BF%9E%E6%8E%A5%E6%B3%84%E6%BC%8F%E7%9B%91%E6%B5%8B
			//dataSource.setRemoveAbandoned(true);
			//超过30分钟开始关闭空闲连接，由于removeAbandoned为false，这个设置项不再起作用
			//dataSource.setRemoveAbandonedTimeout(1800);
			//将当前关闭动作记录到日志，由于removeAbandoned为false，这个设置项不再起作用
			//logAbandoned=true
			//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
			//timeBetweenEvictionRunsMillis=60000
			//end
			//配置一个连接在池中最小生存的时间，单位是毫秒
			//minEvictableIdleTimeMillis=300000
			//验证连接有效与否的SQL，不同的数据配置不同--validationQuery=SELECT 'x'
			dataSource.setValidationQuery("SELECT 1");
			dataSource.setTestOnBorrow(true);
			dataSource.setTestWhileIdle(true);
			//testOnReturn=false
			try {
				//配置监控统计拦截的filters==>>开启防火墙(wall选型是否影响性能，后续可以移除调试，甚至stat也移除)[https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter]和统计功能[filters=log4j,wall,stat
				dataSource.setFilters("wall,stat");
			} catch (SQLException e) {
				e.printStackTrace();
			}  
			//慢sql查询监控设置
			//connectionProperties=druid.stat.slowSqlMillis=1
			//打开PSCache，并且指定每个连接上PSCache的大小，如果用Oracle，则把poolPreparedStatements配置为true，mysql可以配置为false。分库分表较多的数据库，建议配置为false
			dataSource.setPoolPreparedStatements(false);
			//maxPoolPreparedStatementPerConnectionSize=20
			//配置timeBetweenLogStatsMillis>0之后，DruidDataSource会定期把监控数据输出到日志中 
			//timeBetweenLogStatsMillis=300000
			//这里配置提交方式，默认就是TRUE，可以不用配置 
			//dataSource.setDefaultAutoCommit(false);
			//读取配置文件信息
			DruidDataSourceFactory.config(dataSource, result.getProps());
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
		try {
			dataSource.init();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new StartupErrorException("sql数据库连接失败");
		}
		dataSourceStr = result.toString();
		return dataSource;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接,如果不存在，创建连接，并从连接池返回<br>
	 * 操作步骤: TODO<br>
	 */
	public static Connection getConnection()   {
		Connection connection = ConnectionFactory.getConnection(factory.dataSource);
		return connection;
	}
	
	/**
	 * 
	 * 方法用途: 获取当前线程上的连接并开启事务<br>
	 * 操作步骤: TODO<br>
	 */
	public static void startTransaction() {
		ConnectionFactory.startTransaction(factory.dataSource);
	}
	
	
	/**
	 * 
	 * 方法用途: 初始化数据源<br>
	 * 操作步骤:  init-method="init"<br>
	 */
	public static void initPool() {
		try {
			((DruidDataSource)factory.dataSource).init();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 方法用途: 关闭数据源<br>
	 * 操作步骤:  destroy-method="close"<br>
	 */
	public static void closePool() {
		((DruidDataSource)factory.dataSource).close();
	}
}

