package com.meizu.cache.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.HostAndPortUtil.HostAndPort;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

/**
 * 
 * 
 */
public class RedisClientPool {
	private static final Logger log = LoggerFactory
			.getLogger(RedisClientPool.class);

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
				log.info("[redis - list],host:{},port:{}", new Object[] {
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

	private RedisClientPool() {

	}

	public static ShardedJedis getJedisClient(String mod_name) {
		return redisPools.get(mod_name).getResource();
	}

	public static void returnJedisClient(String mod_name,ShardedJedis shardedJedis) {
		redisPools.get(mod_name).returnResource(shardedJedis);
	}

	public static void returnBrokenResource(String mod_name,ShardedJedis shardedJedis){
		redisPools.get(mod_name).returnBrokenResource(shardedJedis);
	}
	
	public static void main(String[] args) {
		
		
	}
}
