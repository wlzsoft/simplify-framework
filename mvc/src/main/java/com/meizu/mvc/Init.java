package com.meizu.mvc;

/**
 * 
 * <p><b>Title:</b><i>框架初始化</i></p>
 * <p>Desc: 框架初始化</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:45:49</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:45:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 * @version 2014-12 v1.0 版本生成
 * @version 2014-12 v1.1 扩展表单获取方式, 增加对URL做Cached
 * @version 2014-12 v1.2 扩展标签，增加对数组迭代、增加对全区取对象，对print标签增加format功能
 * @version 2015-01 v1.2.2 扩展Ehcached配置支持
 * @version 2015-02 v1.2.3 扩展Client，json远程对象调用
 * @version 2015-02 v1.2.4 扩展表单获取，对浮点类型支持
 * @version 2015-02 v1.3 扩展filter方式,并支持requestset路径
 * @version 2015-02 v1.3.5 扩展filter方式对参数支持
 * @version 2015-02 v1.4 对urlcache的缓存记录方式做了先进先出模式
 * @version 2015-02 v1.4.5 对hibernate扩展，支持动态设置表名, 对标签功能扩展
 * @version 2015-02 v1.4.8 对hibernateSearch建立索引缓存开放配置
 * @version 2015-02 v1.4.9 对lucene接口提供过滤器功能, 解决lucene由于第一次取大小会被执行2次的问题.
 * @version 2015-02 v1.5 增加对batch支持, 增加优化批量添加删除接口, 以及修复一些其它bug.
 * @version 2015-02 v1.5.5 进行了一些Search相关优化操作
 * @version 2015-02 v1.5.6 优化MVC表单取值的处理过程
 * @version 2015-02 v1.5.7 对获取表单参数值增加了对ec参数支持，能指定参数编码
 * @version 2015-02 v1.5.8 修正CreateIndex的pushbug.
 * @version 2015-02 v1.5.9 调整切片策略支持和一些兼容性和稳定性优化
 * @version 2015-02 v1.6 支持多SessionFactory管理</p>
 */

public class Init extends MvcInit {
	
	public static boolean cache = false;
	public static boolean ehcache = false;
	public static String memcached_servers = "";
	public static String memcached_cacheTimeSeconds = "300"; // 缓存失效时间
	public static boolean lucene_switch = false;
	public static String lucene_indexBase = ""; // 位置
	public static String directory_provider = ""; // 加载器
	public static String mergeFactor = "1000"; // lucene配置
	public static String minMergeDocs = "1000";// lucene配置
	public static String maxMergeDocs = String.valueOf(Integer.MAX_VALUE);// lucene配置
	
	public static String batchSize = "100000"; // lucene配置
	public static String operationLimit = "1000000";// lucene配置
	public static String transactionLimit = "1000000";// lucene配置
	
	// public static Integer limitExecutionTime = 0; // 默认关闭
	
	public static String authorization_user = "";
	public static String authorization_password = "";
	
	public static String hibernate_shards;
	public static String hibernate_selectionstrategy;
	public static String hibernate_resolutionstrategy;
	public static String hibernate_accessstrategy;
	
	public static String hibernate_configfiles;
	
	/*
	 * 测试开关，true表示以测试方式启动项目 测试方式启动项目则缓存方式无效
	 */
	public static boolean test_switch = false;
	
	public static int limitExecutionTime = 0;
	
	public Init() {
	}
	
	static {
		hibernate = config.getBoolean("system.hibernate", false);
		
		cache = config.getBoolean("memcached.switch", false);
		if (!cache) cache = (ehcache = config.getBoolean("ehcached.switch", false));
		
		memcached_servers = config.getString("memcached.servers");
		memcached_cacheTimeSeconds = config.getString("memcached.cacheTimeSeconds", "300");
		
		lucene_switch = config.getBoolean("lucene.switch", false);
		lucene_indexBase = config.getString("lucene.indexBase");
		directory_provider = config.getString("lucene.directory_provider", "org.hibernate.search.store.FSDirectoryProvider");
		
		mergeFactor = config.getString("lucene.mergeFactor", mergeFactor);
		minMergeDocs = config.getString("lucene.minMergeDocs", minMergeDocs);
		maxMergeDocs = config.getString("lucene.maxMergeDocs", maxMergeDocs);
		limitExecutionTime = config.getInteger("lucene.limitExecutionTime", limitExecutionTime);
		
		batchSize = config.getString("lucene.batchSize", batchSize);
		operationLimit = config.getString("lucene.operationLimit", operationLimit);
		transactionLimit = config.getString("lucene.transactionLimit", transactionLimit);
		
		authorization_user = config.getString("authorization.user");
		authorization_password = config.getString("authorization.password");
		
		hibernate_shards = config.getString("hibernate.shards", "");
		hibernate_selectionstrategy = config.getString("hibernate.selectionstrategy", "");
		hibernate_resolutionstrategy = config.getString("hibernate.resolutionstrategy", "");
		hibernate_accessstrategy = config.getString("hibernate.accessstrategy", "");
		
		hibernate_configfiles = config.getString("hibernate.configfiles", "/hibernate.cfg.xml");
		
		test_switch = config.getBoolean("system.test_switch", false);
		if (cache) {
			if (ehcache) {
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework ehcached -> ok.");
			} else {
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework memcached_servers -> " + memcached_servers);
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework memcached_cacheTimeSeconds -> " + memcached_cacheTimeSeconds);
			}
		} else {
//			PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework cached is closed.");
		}
		if (lucene_switch) {
//			PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework lucene_indexBase -> " + lucene_indexBase);
			// PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework lucene_directory_provider -> " + directory_provider);
		}
		
	}
	
}
