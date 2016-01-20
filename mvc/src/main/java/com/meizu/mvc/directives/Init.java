package com.meizu.mvc.directives;


/**
 * 框架初始化
 * 
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
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework ehcached -> ok.");
			} else {
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework memcached_servers -> " + memcached_servers);
//				PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework memcached_cacheTimeSeconds -> " + memcached_cacheTimeSeconds);
			}
		} else {
//			PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework cached is closed.");
		}
		
	}
	
}
