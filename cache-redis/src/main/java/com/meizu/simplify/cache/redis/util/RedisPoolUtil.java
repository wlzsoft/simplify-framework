package com.meizu.simplify.cache.redis.util;

import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.redis.properties.RedisPoolProperties;
import com.meizu.simplify.utils.PropertieUtil;

/**
 * <p><b>Title:</b><i>redis连接池工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 上午11:11:56</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 上午11:11:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisPoolUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisPoolUtil.class);
	private static final String REDIS_CONFIG_FILE = "redis-pool.properties";
	private static final PropertieUtil propertieUtils = new PropertieUtil(REDIS_CONFIG_FILE);
	private static RedisPoolProperties redisPoolProperties = null;
	static {
		Set<Entry<Object, Object>> set = propertieUtils.propertys();
		if (set.size() == 0) {
			throw new IllegalArgumentException("redis连接池信息：["+REDIS_CONFIG_FILE+"]配置文件为空 !");
		}
		//TODO 可以通过通过ReloadableResource注解的方式来处理，需求自己处理转换-后续考虑是否统一处理
		redisPoolProperties = propertieUtils.toClass(RedisPoolProperties.class);
		LOGGER.info("初始redis连接池配置信息:"+propertieUtils.toString());
	}
	public static RedisPoolProperties getRedisPoolProperties() {
		return redisPoolProperties;
	}

}
