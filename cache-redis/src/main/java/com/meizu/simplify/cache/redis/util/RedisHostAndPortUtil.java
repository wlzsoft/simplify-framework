package com.meizu.simplify.cache.redis.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.utils.AssertUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;

import redis.clients.jedis.Protocol;

/**
 * <p><b>Title:</b><i>redis分片服务信息配置工具</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 上午11:11:51</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 上午11:11:51</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisHostAndPortUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisHostAndPortUtil.class);
	private static Map<String, List<HostAndPort>> hostAndPortMap = new ConcurrentHashMap<String, List<HostAndPort>>();
	private static final String REDIS_CONFIG_FILE = "redis-host.properties";
	private static final PropertieUtil propertieUtils = new PropertieUtil(REDIS_CONFIG_FILE);
	static {
		Set<Entry<Object, Object>> set = propertieUtils.propertys();
		if (set.size() == 0) {
			throw new IllegalArgumentException("redis集群节点信息：["+REDIS_CONFIG_FILE+"]配置文件为空 !");
		}
		for (Entry<Object, Object> property : set) {
			String value = StringUtil.parseString(property.getValue(),"");
			String[] envHosts = value.split(",|，");
			if (envHosts.length == 0) {
				continue;
			}
			List<HostAndPort> hostList = new ArrayList<HostAndPort>();
			for (String hostDef : envHosts) {
				final String[] hostAndPort = hostDef.split(":");
				if (null == hostAndPort) {
					continue;
				}

				final HostAndPort hnp = new HostAndPort();
				hnp.host = hostAndPort[0];
				try {
					hnp.port = Integer.parseInt(hostAndPort[1]);
				} catch (final NumberFormatException nfe) {
					hnp.port = Protocol.DEFAULT_PORT;
					throw new IllegalArgumentException(StringUtil.format(
							" ip : {0} 端口 {1} 错误 !",
							hostAndPort[0], hostAndPort[1]));
				}
				
				if(hostAndPort.length == 3){
					hnp.pwd = hostAndPort[2];
				}
				
				hostList.add(hnp);
			
			}
			String key = StringUtil.parseString(property.getKey(),"");
			AssertUtil.notBlank("key不能为空");
			hostAndPortMap.put(key, hostList);

		}

		final StringBuilder strb = new StringBuilder("redis集群节点信息 被启用列表 : ");

		for (String key : hostAndPortMap.keySet()) {
			strb.append(" key : " + key);
			strb.append("[");
			List<HostAndPort> hostList = hostAndPortMap.get(key);
			for (HostAndPort hnp : hostList) {
				if(hnp.pwd != null && hnp.pwd.length() > 0){
					strb.append(hnp.host).append(":").append(hnp.port).append(":").append(hnp.pwd+",");
				}else{
					strb.append(hnp.host).append(":").append(hnp.port).append(",");
				}
			}
			strb.append("]");
		}
		LOGGER.info(strb.toString());
		
	}
	
	public static Map<String, List<HostAndPort>> getRedisServers() {
		return hostAndPortMap;
	}

	public static class HostAndPort {
		public String host;
		public int port;
		public String pwd;
	}

}
