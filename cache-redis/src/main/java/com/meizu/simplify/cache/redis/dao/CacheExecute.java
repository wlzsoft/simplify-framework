package com.meizu.simplify.cache.redis.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.exception.CacheException;
import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;
import com.meizu.simplify.cache.redis.exception.RedisException;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午5:54:49</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月26日 下午5:54:49</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class CacheExecute {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonRedisDao.class);
	
	public static ShardedJedis getJedis(String mod_name) {
		try {
			return RedisPool.getConnection(mod_name);
		} catch(RedisException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法用途: 返回值 TODO 
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
	public static <KK,VV> VV execute(KK key,ICacheExecuteCallbak<KK,VV> callback,String mod_name) {
		try {
			return callback.call(key);
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
				ShardedJedisPool pool = RedisPool.init(mod_name);
				if (!pool.isClosed()) {
//					pool.returnResourceObject(getJedis(mod_name));
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
}
