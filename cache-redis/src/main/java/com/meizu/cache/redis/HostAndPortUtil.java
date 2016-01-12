package com.meizu.cache.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.meizu.simplify.utils.StringUtil;

import redis.clients.jedis.Protocol;

public class HostAndPortUtil {
	private static Map<String, List<HostAndPort>> hostAndPortMap = new ConcurrentHashMap<String, List<HostAndPort>>();
	private static final String REDIS_CONFIG_FILE = "redis-host.properties";
	private static final PropertieUtils propertieUtils = new PropertieUtils(
			REDIS_CONFIG_FILE);
	static {
		Set<String> set = propertieUtils.stringPropertyNames();
		if (set.size() == 0) {
			throw new IllegalArgumentException("args is null !");
		}

		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			String value = propertieUtils.getProperty(key);
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
							" ip : {0} port {1} is error !",
							hostAndPort[0], hostAndPort[1]));
				}
				
				if(hostAndPort.length == 3){
					hnp.pwd = hostAndPort[2];
				}
				
				hostList.add(hnp);
			
			}
			hostAndPortMap.put(key, hostList);

		}

		final StringBuilder strb = new StringBuilder(
				"Redis hosts to be used : ");

		for (Iterator<String> it = hostAndPortMap.keySet().iterator(); it
				.hasNext();) {
			String key = it.next();
			strb.append(" key : " + key);
			strb.append("[");
			List<HostAndPort> hostList = hostAndPortMap.get(key);
			for (HostAndPort hnp : hostList) {
				if(hnp.pwd != null && hnp.pwd.length() > 0){
					strb.append(hnp.host + ":" +hnp.port+ ":" +hnp.pwd+",");
				}else{
					strb.append(hnp.host + ":" +hnp.port+",");
				}
			}
			strb.append("]");
		}
		// for (HostAndPort hnp : hostList) {
		// strb.append('[').append(hnp.host).append(':').append(hnp.port)
		// .append(']').append(' ');
		// }
		System.out.println(strb);
	}
 
	
	public static Map<String, List<HostAndPort>> getRedisServers() {
		return hostAndPortMap;
	}

	public static class HostAndPort {
		public String host;
		public int port;
		public String pwd;
	}

	public static void main(String[] args) {
		//HostAndPortUtil.getRedisServers();
		
		String str = "192.168.168.208:6379";
		System.out.println(str.split("\\|").length);
	}
}
