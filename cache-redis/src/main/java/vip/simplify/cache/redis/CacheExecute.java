package vip.simplify.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;
import vip.simplify.cache.exception.CacheException;
import vip.simplify.cache.redis.dao.ICacheExecuteCallbak;
import vip.simplify.cache.redis.dao.IClusterCacheExecuteCallbak;
import vip.simplify.cache.redis.exception.RedisException;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午5:54:49</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月26日 下午5:54:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class CacheExecute {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheExecute.class);
	
	
	/**
	 * 方法用途: 返回值 <br>
	 * 操作步骤: 用法 <code><br>
	 * CacheExecute.execute(key, (k,jedis) -> {
	 *<br>&nbsp;&nbsp;&nbsp;&nbsp;V value = null;	
	 *<br>&nbsp;&nbsp;&nbsp;&nbsp;return value;
	 *<br>},modName);</code>
	 * <br>
	 * @param key 缓存键
	 * @return 缓存值
	 */
	public static <KK,VV> VV execute(KK key, ICacheExecuteCallbak<KK,VV> callback, String modName) {
		ShardedJedis jedis = RedisPool.getConnection(modName);
		try {
			return callback.call(key,jedis);
//		} catch (TimeoutException e) {
//			LOGGER.error("获取 redis 缓存超时", e);
		} catch (JedisConnectionException e) {
			LOGGER.error("并发导致连接异常被服务端丢弃和重置!", e);
			throw new RedisException(e);
		} catch (JedisDataException e) {
			LOGGER.warn("获取 redis 缓存被中断", e);
			throw new RedisException(e);
		} catch (JedisException e) {
			LOGGER.warn("获取 redis 缓存错误", e);
			throw new RedisException(e);
		} catch (Exception e) {
	          LOGGER.error("error!", e);
	          throw new CacheException(e.getMessage());
		} finally {
			try {
				ShardedJedisPool pool = RedisPool.init(modName);
				if (!pool.isClosed()&&jedis!=null) {
					jedis.close();
					//pool.returnResourceObject(jedis);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 方法用途: 返回值 <br>
	 * 操作步骤: 用法 <code><br>
	 * CacheExecute.execute(key, (k,jedis) -> {
	 *<br>&nbsp;&nbsp;&nbsp;&nbsp;V value = null;
	 *<br>&nbsp;&nbsp;&nbsp;&nbsp;return value;
	 *<br>},modName);</code>
	 * <br>
	 * @param key 缓存键
	 * @return 缓存值
	 */
	public static <KK,VV> VV executeCluster(KK key, IClusterCacheExecuteCallbak<KK,VV> callback, String modName) {

		JedisCluster jedis = RedisPool.getClusterConnection(modName);
		try {
			return callback.call(key,jedis);
//		} catch (TimeoutException e) {
//			LOGGER.error("获取 redis 缓存超时", e);
		} catch (JedisConnectionException e) {
			LOGGER.error("并发导致连接异常被服务端丢弃和重置!", e);
			throw new RedisException(e);
		} catch (JedisDataException e) {
			LOGGER.warn("获取 redis 缓存被中断", e);
			throw new RedisException(e);
		} catch (JedisException e) {
			LOGGER.warn("获取 redis 缓存错误", e);
			throw new RedisException(e);
		} catch (Exception e) {
			LOGGER.error("error!", e);
			throw new CacheException(e.getMessage());
		} finally {
			try {
				if (jedis!=null) {
					//jedis.close(); //jedisCluster不应该关闭，会关闭整个连接池
					//pool.returnResourceObject(jedis);
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
