package com.meizu.cache.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.util.HostAndPortUtil;
import com.meizu.cache.redis.util.HostAndPortUtil.HostAndPort;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午7:24:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午7:24:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisPool {
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisPool.class);

	private static Map<String, List<HostAndPort>> hostAndPortMap;

	private static Map<String, ShardedJedisPool> redisPools = new ConcurrentHashMap<String, ShardedJedisPool>();

	static {

		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxWaitMillis(10000);
		config.setMaxIdle(100);
		config.setMaxTotal(5000);
		config.setTestOnBorrow(true);
		config.setTestWhileIdle(false);
		
		//config.timeBetweenEvictionRunsMillis = 30000;
		//config.numTestsPerEvictionRun= 10000;

		hostAndPortMap = HostAndPortUtil.getRedisServers();
		if (hostAndPortMap.isEmpty())
			throw new RuntimeException(" redis config error !!! ");
		
		for (Iterator<String> it = hostAndPortMap.keySet().iterator(); it
				.hasNext();) {
			String mod_name = it.next();
			List<HostAndPort> hostList = hostAndPortMap.get(mod_name);
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			for (HostAndPort hnp : hostList) {
				LOGGER.info("[redis - list],host:{},port:{}", new Object[] {
						hnp.host, hnp.port });
				JedisShardInfo jedisShardInfo = new JedisShardInfo(hnp.host,
						hnp.port);
				
				if(hnp.pwd != null && hnp.pwd.length() > 0){
					jedisShardInfo.setPassword(hnp.pwd);
				}
				shards.add(jedisShardInfo);
			}
			ShardedJedisPool pool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH);
			redisPools.put(mod_name, pool);
		}

	}

	private RedisPool() {

	}

	/**
	 * 
	 * 方法用途: <br>
	 * 操作步骤: TODO<br>
	 * @param mod_name
	 * @return
	 */
	public static ShardedJedis getConnection(String mod_name) {
		return redisPools.get(mod_name).getResource();
	}

}
