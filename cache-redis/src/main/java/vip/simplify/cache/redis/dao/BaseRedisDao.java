package vip.simplify.cache.redis.dao;

import vip.simplify.cache.enums.TimeEnum;
import vip.simplify.cache.redis.CacheExecute;
import vip.simplify.cache.redis.properties.RedisPoolProperties;
import vip.simplify.cache.redis.util.RedisPoolUtil;

import java.io.Serializable;

/**
 * <p><b>Title:</b><i>缓存操作基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月15日 上午10:05:06</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月15日 上午10:05:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <K>
 */
public abstract class BaseRedisDao<K extends Serializable>  {
	RedisPoolProperties redisPoolProperties = RedisPoolUtil.getRedisPoolProperties();
	public String modName;
	public BaseRedisDao(String modName) {
		this.modName = modName;
	}
	
	/**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param expireTime 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int expireTime, TimeEnum seconds) {
		Long ret = null;
		if (redisPoolProperties.getOfficialCluster()) {
			ret = CacheExecute.executeCluster(key,(k,jedis) -> jedis.expire(k,expireTime),modName);
		} else {
			ret = CacheExecute.execute(key,(k,jedis) -> jedis.expire(k, expireTime),modName);
		}
		return ret;
	}
	/**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param expireTime 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param seconds
	 * @return
	 */
	public long expire(byte[] key, int expireTime, TimeEnum seconds) {
		Long ret = null;
		if (redisPoolProperties.getOfficialCluster()) {
			ret = CacheExecute.executeCluster(key,(k,jedis) ->  jedis.expire(k, expireTime),modName);
		} else {
			ret = CacheExecute.execute(key,(k,jedis) ->  jedis.expire(k, expireTime),modName);
		}
		return ret;
	}
	
	/**
	 * 
	 * 方法用途: 获取指定key的剩余过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long getExpire(K key, TimeEnum seconds) {
		return 0L;
	}
	
}