package vip.simplify.cache.redis.dao.impl;

import com.alibaba.fastjson.TypeReference;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import vip.simplify.cache.dao.IJsonCacheDao;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.CacheExecute;
import vip.simplify.cache.redis.dao.BaseRedisDao;
import vip.simplify.cache.redis.properties.RedisPoolProperties;
import vip.simplify.cache.redis.util.RedisPoolUtil;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.util.JsonResolver;
import vip.simplify.utils.JsonUtil;

import java.util.*;


/**
 * <p><b>Title:</b><i>以JSON字符缓存操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:53:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:53:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonRedisDao<VV> extends BaseRedisDao<String> implements IJsonCacheDao<VV> {

	RedisPoolProperties redisPoolProperties = RedisPoolUtil.getRedisPoolProperties();
	private Class<VV> valueClazz;

	public JsonRedisDao(String modName,Class<VV> valueClazz) {
		super(modName);
		this.valueClazz = valueClazz;
	}
	
	public JsonRedisDao(String modName) {
		super(modName);
	}
	
	/**
	 * 写死默认固定值 TODO 暂不启用
	 */
	public JsonRedisDao() {
		super("redis_ref_hosts");
	}

	/**
	 * 
	 * 方法用途: <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p><br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public VV getAndSet(String key, VV value) {
		return getAndSet(key,value,valueClazz);
	}
	
	/**
	 * 
	 * 方法用途: <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p><br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @param clazz
	 * @return
	 */
	@Override
	public <V> V getAndSet(String key, V value,Class<V> clazz) {
		if (redisPoolProperties.getOfficialCluster()) {
			@SuppressWarnings("unchecked")
			V result = CacheExecute.executeCluster(key, (k, jedis) ->  {
				String valueStr = jedis.getSet(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				if(valueStr != null && valueStr.length() > 0){
					if(clazz != null) {
						return JsonUtil.jsonToObject(valueStr,clazz);
					} else {
						return (V)JsonUtil.jsonToObject(valueStr);
					}
				}
				return null;
			}, modName);
			return result;
		} else {
			@SuppressWarnings("unchecked")
			V result = CacheExecute.execute(key, (k, jedis) ->  {
				String valueStr = jedis.getSet(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				if(valueStr != null && valueStr.length() > 0){
					if(clazz != null) {
						return JsonUtil.jsonToObject(valueStr,clazz);
					} else {
						return (V)JsonUtil.jsonToObject(valueStr);
					}
				}
				return null;
			}, modName);
			return result;
		}

	}

	/**
	 * 
	 * 方法用途: 注意返回<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	@Override
	public VV get(String key) {
		return get(key,valueClazz);
	}
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param typeReference
	 * @return
	 */
	@Override
	public <V> V get(String key,TypeReference<V> typeReference) {
		if (redisPoolProperties.getOfficialCluster()) {
			V result =  CacheExecute.executeCluster(key, (k,jedis) ->  {
				String valueStr =  jedis.get(k);
				if(valueStr != null && valueStr.length() > 0){
					if(typeReference != null) {
						return JsonUtil.jsonToObject(valueStr,typeReference);
					}
				}
				return null;
			}, modName);
			return result;
		} else {
			V result =  CacheExecute.execute(key, (k,jedis) ->  {
				String valueStr =  jedis.get(k);
				if(valueStr != null && valueStr.length() > 0){
					if(typeReference != null) {
						return JsonUtil.jsonToObject(valueStr,typeReference);
					}
				}
				return null;
			}, modName);
			return result;
		}

	}
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param clazz
	 * @return
	 */
	@Override
	public <V> V get(String key,Class<V> clazz) {
		if (redisPoolProperties.getOfficialCluster()) {
			@SuppressWarnings("unchecked")
			V result =  CacheExecute.executeCluster(key, (k,jedis) ->  {
				String valueStr =  jedis.get(k);
				if(valueStr != null && valueStr.length() > 0){
					if(clazz != null) {
						return JsonUtil.jsonToObject(valueStr,clazz);
					} else {
						return (V)JsonUtil.jsonToObject(valueStr);
					}
				}
				return null;
			}, modName);
			return result;
		} else {
			@SuppressWarnings("unchecked")
			V result =  CacheExecute.execute(key, (k,jedis) ->  {
				String valueStr =  jedis.get(k);
				if(valueStr != null && valueStr.length() > 0){
					if(clazz != null) {
						return JsonUtil.jsonToObject(valueStr,clazz);
					} else {
						return (V)JsonUtil.jsonToObject(valueStr);
					}
				}
				return null;
			}, modName);
			return result;
		}

	}
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(String key, VV value) {
		return set(key, CacheExpireTimeEnum.CACHE_EXP_FOREVER.timesanmp(), value);
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: BeanFactory.getBean(JsonResolver.class).ObjectToString(value) 后续使用 JsonUtil.objectToString 方法来处理，当前类使用BeanConfig注入方式 TODO<br>
	 * @param key
	 * @param expireTime 超时事件 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(String key,int expireTime, VV value) {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean isSuccess = CacheExecute.executeCluster(key, (k,jedis) -> {
				String result = jedis.set(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				if(expireTime > 0){
					jedis.expire(k, expireTime);
				}
				return result.equalsIgnoreCase("OK");
			}, modName);
			return isSuccess;
		} else {
			Boolean isSuccess = CacheExecute.execute(key, (k,jedis) -> {
				String result = jedis.set(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				if(expireTime > 0){
					jedis.expire(k, expireTime);
				}
				return result.equalsIgnoreCase("OK");
			}, modName);
			return isSuccess;
		}

	}
	  
	
    /**
     * 
     * 方法用途: <p>将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @return
     */
	@Override
    public boolean setnx(String key, VV value) {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean isSuccess = CacheExecute.executeCluster(key, (k,jedis) ->  {
				long result = jedis.setnx(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				return result > 0;
			}, modName);
			return isSuccess;
		} else {
			Boolean isSuccess = CacheExecute.execute(key, (k,jedis) ->  {
				long result = jedis.setnx(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				return result > 0;
			}, modName);
			return isSuccess;
		}

    }

    /**
     * 
     * 方法用途: <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param seconds
     * @param value
     * @return
     */
	@Override
    public boolean setex(String key, int seconds, VV value) {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean isSuccess = CacheExecute.executeCluster(key, (k,jedis) ->  {
				String result = jedis.setex(k, seconds, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				return result.equalsIgnoreCase("OK");
			}, modName);
			return isSuccess;
		} else {
			Boolean isSuccess = CacheExecute.execute(key, (k,jedis) ->  {
				String result = jedis.setex(k, seconds, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				return result.equalsIgnoreCase("OK");
			}, modName);
			return isSuccess;
		}

    }
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: 注意：这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO<br>
	 * @param key 指定要删除的键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	public boolean delete(String key) throws UncheckedException {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean result = CacheExecute.executeCluster(key, (k,jedis) -> {
				Long res = jedis.del(k);
				if(res==0) {
					return true;
				} else {
					return false;
				}
			},modName);
			return result;
		} else {
			Boolean result = CacheExecute.execute(key, (k,jedis) -> {
				Long res = jedis.del(k);
				if(res==0) {
					return true;
				} else {
					return false;
				}
			},modName);
			return result;
		}

	}
	
	/** 
	 * 方法用途: 删除多个值
	 * 操作步骤: 注意：这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片这里会查询所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现<br>
	 *            这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO
	 * <br>
	 * @param keys 指定要删除的多个键
	 * @return 删除成功,返回删除的记录数
	 */
	@Override
	public Long delete(String[] keys) {
		if (redisPoolProperties.getOfficialCluster()) {
			Long result = CacheExecute.executeCluster(keys, (k,jedis) -> {
				Long delCount = jedis.del(keys);
				return delCount;
			},modName);
			return result;
		} else {
			Long result = CacheExecute.execute(keys, (k,jedis) -> {
				Long delCount = 0L;
				Collection<Jedis> jedisSet = jedis.getAllShards();
				for (Jedis singleJedis : jedisSet) {
					delCount += singleJedis.del(keys);
				}
				return delCount;
			},modName);
			return result;
		}

	}
	
	/** 
	 * 方法用途: 删除多个值
	 * 操作步骤: 注意：这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现
	 *                这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO<br>
	 * @param keys 指定要删除的多个键
	 * @return 删除成功,返回删除的记录数
	 */
	@Override
	public Long delete(Set<String> keys)  {
		if (keys.size() == 0) {
			return 0L;
		}
		return delete(keys.toArray(new String[keys.size()]));
	}
	
	/** 
	 * 方法用途: 删除查询到的所有结果
	 * 操作步骤: 注意：1.这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现
	 *                2.这里查询和删除分两步操作redis
	 *                3.这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO<br>
	 * @param key 待查询key前缀
	 * @return 删除成功,返回删除的记录数
	 */
	@Override
	public Long searchAndDelete(String key)  {
		return delete(keys(key));
	}
	
	/**
	 * 
	 * 方法用途: 左边前缀模糊匹配key<br>
	 * 操作步骤: 注意：这里会查询所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现<br>
	 * @param key 待查询key前缀
	 * @return
	 */
	public Set<String> keys(String key) {
		if (key.startsWith("*")) {
			throw new UncheckedException("只允许左边前缀匹配key");
		}
		if (redisPoolProperties.getOfficialCluster()) {
			Set<String> hitKeySet = CacheExecute.executeCluster(key,(k,jedis) ->  {
				Set<String> keySet = new HashSet<>();
				Map<String, JedisPool> clusterNodes = jedis.getClusterNodes();
				for(Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()){
					JedisPool jedisPool = entry.getValue();
					Jedis connection = jedisPool.getResource();
					keySet.addAll(connection.keys(key));
				}
				return keySet;

			},modName);
			return hitKeySet;
		} else {
			Set<String> hitKeySet = CacheExecute.execute(key,(k,jedis) ->  {
				Set<String> keySet = new HashSet<>();
				Collection<Jedis> jedisCollecion = jedis.getAllShards();
				for (Jedis singleRedis : jedisCollecion) {
					keySet.addAll(singleRedis.keys(key));
				}
				return keySet;
			},modName);
			return hitKeySet;
		}

	}
}
