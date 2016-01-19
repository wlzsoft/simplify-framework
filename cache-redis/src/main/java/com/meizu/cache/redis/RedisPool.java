package com.meizu.cache.redis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.properties.RedisPoolProperties;
import com.meizu.cache.redis.util.RedisHostAndPortUtil;
import com.meizu.cache.redis.util.RedisHostAndPortUtil.HostAndPort;
import com.meizu.cache.redis.util.RedisPoolUtil;

import redis.clients.jedis.JedisPoolConfig;
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

		RedisPoolProperties redisPoolProperties= RedisPoolUtil.getRedisPoolProperties();
//		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		JedisPoolConfig config = new JedisPoolConfig();
		
		//表示当borrow(获取)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException
		config.setMaxWaitMillis(redisPoolProperties.getMaxWaitMillis());
		
		//连接池的最大连接数，使用完，就会排队等待获取连接，连接用尽
		config.setMaxTotal(redisPoolProperties.getMaxTotal());
		
		//连接池的最大空闲连接数
		config.setMaxIdle(redisPoolProperties.getMaxIdle());
		
		config.setMinIdle(redisPoolProperties.getMinIdle());
		
		//在borrow(获取)一个jedis实例时，是否提前进行validate操作,如果为true，则得到的jedis实例均是可用的
		config.setTestOnBorrow(redisPoolProperties.getTestOnBorrow());
		
//		在return给pool时，是否提前进行validate操作
//		config.setTestOnReturn(true);
		
//		如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
		config.setTestWhileIdle(redisPoolProperties.getTestWhileIdle());//defaul true
		
//		表示当pool中的jedis实例都被allocated完时，pool要采取的操作；默认有三种:
//			WHEN_EXHAUSTED_FAIL --> 表示无jedis实例时，直接抛出NoSuchElementException；
//			WHEN_EXHAUSTED_BLOCK --> 则表示阻塞住，或者达到maxWait时抛出JedisConnectionException；
//			WHEN_EXHAUSTED_GROW --> 则表示新建一个jedis实例，也就说设置的maxActive无用
//		config.setBlockWhenExhausted(false); //TODO
		
//		config.setNumTestsPerEvictionRun(-1);
		
//		表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
//		config.setMinEvictableIdleTimeMillis(60000);
		
		//表示idle object evitor两次扫描之间要sleep的毫秒数
//		config.setTimeBetweenEvictionRunsMillis(30000);
		
//		表示idle object evitor每次扫描的最多的对象数
//		config.setNumTestsPerEvictionRun(10000);
		
//		在minEvictableIdleTimeMillis基础上，加入了至少minIdle个对象已经在pool里面了。如果为-1，evicted不会根据idle time驱逐任何对象。如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在timeBetweenEvictionRunsMillis大于0时才有意义
//		config.setSoftMinEvictableIdleTimeMillis(1000);
//
//		borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列
//		config.setLifo(true);
          
		hostAndPortMap = RedisHostAndPortUtil.getRedisServers();
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
	 * 方法用途: 启动时初始化连接池的大小<br>
	 * 操作步骤: TODO<br>
	 * @param mod_name
	 */
	public static ShardedJedisPool init(String mod_name) {
		return redisPools.get(mod_name);
	}
	
	/**
	 * 
	 * 方法用途: Connection reset by peer: socket write error 问题分析和定位解决
	 *          1.并发量过大，连接数不够导致服务器主动关闭部分连接<br>
	 * 操作步骤: TODO<br>
	 * @param mod_name
	 * @return
	 */
	public static ShardedJedis getConnection(String mod_name) {
		return redisPools.get(mod_name).getResource();
	}

}
