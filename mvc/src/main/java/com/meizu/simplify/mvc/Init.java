package com.meizu.simplify.mvc;


/**
 * <p><b>Title:</b><i>框架初始化</i></p>
 * <p>Desc: 框架初始化
 * 对urlcache的缓存记录方式做了先进先出模式
 * cache.switch //是否启用缓存
 * redis.switch //是否启用redis缓存
 * system.test_switch //是否启用测试模式，测试模式下，不会有缓存，也不需要输入验证码
 * 检查缓存是否开启，如果未开启，需要启动时有明显提示信息</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午5:11:35</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午5:11:35</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Init extends MvcInit {
	public static boolean cache = false;
	public static boolean jvmcache = false;
	
	static {
		if (cache) {
			if (jvmcache) {
//				LOGGER.log("Framework jvm cached -> ok.");
			} else {
//				LOGGER.log("Framework redis_servers -> " + redis_servers);
//				LOGGER.log("Framework redis_cacheTimeSeconds -> " + redis_cacheTimeSeconds);
			}
		} else {
//			LOGGER.log("Framework cached is closed.");
		}
		
	}
}
