package com.meizu.cache.redis.util;

import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.utils.PropertieUtil;

public class RedisPoolProperties {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisPoolProperties.class);
	private static final String REDIS_CONFIG_FILE = "redis-host.properties";
	private static final PropertieUtil propertieUtils = new PropertieUtil(REDIS_CONFIG_FILE);
	static {
		Set<Entry<Object, Object>> set = propertieUtils.propertys();
		if (set.size() == 0) {
			throw new IllegalArgumentException("redis集群节点信息：["+REDIS_CONFIG_FILE+"]配置文件为空 !");
		}
	}

}
