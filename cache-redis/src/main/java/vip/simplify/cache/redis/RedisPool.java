package vip.simplify.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.Hashing;
import vip.simplify.cache.redis.exception.RedisException;
import vip.simplify.cache.redis.properties.RedisPoolProperties;
import vip.simplify.cache.redis.util.RedisHostAndPortUtil;
import vip.simplify.cache.redis.util.RedisHostAndPortUtil.HostAndPort;
import vip.simplify.cache.redis.util.RedisPoolUtil;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * <p><b>Title:</b><i>reids连接池工具类</i></p>
 * <p>Desc: jedis库使用注意事项
 * 1.cluster方式的slave不接受任何读写操作
 * 2.cluster方式不支持keys操作，可以使用scan，但是没办法完全替代，其实keys很低效，也的确尽量不用，同时也不支持select dbNum操作，只有db:select 0 可以用
 * 3.不支持批量处理，比如批量删除，会报错误：No way to dispatch this command to Redis Cluster because keys have different slots. jedisCluster
 * 4.info等之前非cluster下可以用的函数，都会报错：No way to dispatch this command to Redis Cluster
 * </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午7:24:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午7:24:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedisPool {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisPool.class);
	private static Map<String, List<HostAndPort>> hostAndPortMap;
	private static Map<String, ShardedJedisPool> redisPools = new ConcurrentHashMap<>();
	private static Map<String, JedisCluster> redisClusterPools = new ConcurrentHashMap<>();
	public static String redisInfo;
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
		if (hostAndPortMap.isEmpty()) {
			throw new RuntimeException(" redis config error !!! ");
		}

		//1.支持主从集群设置 TODO
		//2.以下是分布式集群切片设置

		final StringBuilder strb = new StringBuilder("redis集群节点信息 被启用列表 : ");
		// 添加的切片顺序有意义
		for (Entry<String, List<HostAndPort>> entry : hostAndPortMap.entrySet()) {
			String key = entry.getKey();
			List<HostAndPort> hostList = entry.getValue();

			//A.本地客户端切片集群设置
			//这里的集合创建可使用google的guava集合库
			//List<JedisShardInfo> shards = com.google.common.collect.Lists.newArrayList();
			List<JedisShardInfo> shards = new ArrayList<>();

			//B.官方redis cluster设置
			Set<redis.clients.jedis.HostAndPort> hostAndPortSet = new HashSet<>();
			String password = "";

			strb.append("\n切片集群组[").append(key).append("]==>>");
			for (int i = 0; i < hostList.size(); i++) {
				HostAndPort hnp = hostList.get(i);
				strb.append("{节点地址").append(i+1).append(":[").append(hnp.host).append(":").append(hnp.port).append("],");
				if(hnp.pwd != null && hnp.pwd.length() > 0){
					strb.append("密码:").append("******");
				}
				if (redisPoolProperties.getOfficialCluster()) {
					//b.
					redis.clients.jedis.HostAndPort hostAndPort = new redis.clients.jedis.HostAndPort(hnp.host,hnp.port);
					//TODO 暂时没有设置密码权限
					hostAndPortSet.add(hostAndPort);
				} else {
					//a.
					JedisShardInfo jedisShardInfo = new JedisShardInfo(hnp.host,hnp.port);
					if(hnp.pwd != null && hnp.pwd.length() > 0){
						jedisShardInfo.setPassword(hnp.pwd);
						password = hnp.pwd;//最后一个密码为准，后续优化
					}
					shards.add(jedisShardInfo);
				}

				strb.append("}");
			}

			if (redisPoolProperties.getOfficialCluster()) {
				//b.
				JedisCluster jedisCluster = null;
				Integer connectionTimeout = redisPoolProperties.getConnectionTimeout();
				Integer soTimeout = redisPoolProperties.getSoTimeout();
				Integer maxAttempts = redisPoolProperties.getMaxAttempts();
				if ("".equals(password)) {
					jedisCluster = new JedisCluster(hostAndPortSet,connectionTimeout,soTimeout,maxAttempts,config);
				} else {
					jedisCluster = new JedisCluster(hostAndPortSet,connectionTimeout,soTimeout,maxAttempts,password,config);
				}
				redisClusterPools.put(key, jedisCluster);
			} else {
				//a.
				/**
				 * Jedis 驱动库实现的分布式切片,其中List<JedisShardInfo>中JedisShardInfo添加的顺序和key，来算出具体执行哪个切片（哪台redis切片）
				 * 在线上准备好新切片时，可支持Pre-Sharding（在线扩容），前提是需要配合配置中心，做到配置在线自动更新
				 */
				ShardedJedisPool pool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH);
				//ShardedJedisPool pool = new ShardedJedisPool(config, shards, Hashing.MURMUR_HASH,Sharded.DEFAULT_KEY_TAG_PATTERN);// TODO DEFAULT_KEY_TAG_PATTERN 的作用
				redisPools.put(key, pool);
			}
		}


		redisInfo = strb.toString();

	}

	/**
	 * 
	 * 方法用途: 初始化连接池  <br>
	 * 操作步骤: TODO<br>
	 */
	public static void initCachePool() {
		RedisPoolProperties redisPoolProperties= RedisPoolUtil.getRedisPoolProperties();
		if (redisPoolProperties.getOfficialCluster()) {
			JedisCluster jedisCluster = RedisPool.getClusterConnection("redis_ref_hosts");
			LOGGER.info("当前redis连接池状态：启用redis cluster模式，无需关心连接池");
		} else {
			ShardedJedisPool pool = RedisPool.init("redis_ref_hosts");
			for(int i=0; i<10; i++) {
//			RedisPool.getConnection("redis_ref_hosts");//请求后已经返回连接池中，这时候逻辑连接，应该为0，物理连接为10
				ShardedJedis jedis = pool.getResource();
				jedis.close();//redis3.0后建议使用
				//pool.returnResourceObject(jedis);//redis 3.0后废弃
			}
			LOGGER.info("当前redis连接池状态：NumActive(当前激活数):"+pool.getNumActive()+"-NumIdle(当前空闲数):"+pool.getNumIdle()+"-NumWaiters(当前等待数):"+pool.getNumWaiters());
		}
	}
	
	private RedisPool() {

	}

	/**
	 * 
	 * 方法用途: 启动时初始化连接池的大小<br>
	 * 操作步骤: TODO<br>
	 * @param modName
	 */
	public static ShardedJedisPool init(String modName) {
		return redisPools.get(modName);
	}
	
	/**
	 * 
	 * 方法用途: Connection reset by peer: socket write error 问题分析和定位解决
	 *          1.并发量过大，连接数不够导致服务器主动关闭部分连接<br>
	 * 操作步骤: 警告==》(谨慎调用，会有连接泄漏问题)这个方法应该是私有的或是default无修饰符的 TODO<br>
	 * @param modName
	 * @return
	 */
	static ShardedJedis getConnection(String modName) {
		ShardedJedisPool  pool = init(modName);
		ShardedJedis jedis = null;
		try {
			jedis = pool.getResource();
		} catch(JedisConnectionException ex) {
			if(jedis != null) {
				jedis.close();
				pool.destroy();
			}
			throw new RedisException("无法从连接池中获取连接，请确认是否redis服务是否正常",ex);
		}
		return jedis;
	}

	static JedisCluster getClusterConnection(String modName) {
		JedisCluster  cluster = redisClusterPools.get(modName);
		return cluster;
	}

}
