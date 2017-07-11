package vip.simplify.cache.redis.dao.impl;

import vip.simplify.cache.ICacheDao;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.CacheExecute;
import vip.simplify.cache.redis.dao.BaseRedisDao;
import vip.simplify.cache.redis.properties.RedisPoolProperties;
import vip.simplify.cache.redis.util.RedisPoolUtil;
import vip.simplify.exception.UncheckedException;
import vip.simplify.utils.SerializeUtil;

import java.io.Serializable;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: 超时检测</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 上午11:52:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 上午11:52:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <K>
 * @param <V>
 * @param <T>
 */
//@Bean(type=BeanTypeEnum.PROTOTYPE)
public class CommonRedisDao<K extends Serializable,V,T extends Serializable> extends BaseRedisDao<K> implements ICacheDao<K,V>{

	RedisPoolProperties redisPoolProperties = RedisPoolUtil.getRedisPoolProperties();
	public CommonRedisDao(String modName) {
		super(modName);
	}

	/**
	 * 方法用途:缓存里面是否存在该key<br>
	 * 操作步骤: TODO<br>
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(K key) {
		if (redisPoolProperties.getOfficialCluster()) {
			return CacheExecute.executeCluster(key, (k, jedis) -> jedis.exists(k.toString()),modName);
		} else {
			return CacheExecute.execute(key, (k, jedis) -> jedis.exists(k.toString()),modName);
		}
	}
  
	/**
	 * 方法用途: 返回值 <br>
	 * 操作步骤: <br>
	 * @param key  保存键
	 * @return 缓存保存的对象
	 */
	@Override
	public V get(K key) {
		if (redisPoolProperties.getOfficialCluster()) {
			return CacheExecute.executeCluster(key, (k,jedis) ->  {
				byte[] ret = jedis.get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					return SerializeUtil.unserialize(ret);
				}
				return null;
			},modName);
		} else {
			return CacheExecute.execute(key, (k,jedis) ->  {
				byte[] ret = jedis.get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					return SerializeUtil.unserialize(ret);
				}
				return null;
			},modName);
		}
	}

	/**
	 * 方法用途: 返回值 <br>
	 * 操作步骤: <br>
	 * @param key  保存键
	 * @return 缓存保存的对象
	 */
	public byte[] getBytes(byte[] key) {
		if (redisPoolProperties.getOfficialCluster()) {
			return CacheExecute.executeCluster(key, (k,jedis) ->  jedis.get(k) ,modName);
		} else {
			return CacheExecute.execute(key, (k,jedis) ->  jedis.get(k) ,modName);
		}
	}
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param type
	 * @return
	 */
	public  V get(K key, Class<V> type) {
		if (redisPoolProperties.getOfficialCluster()) {
			return CacheExecute.executeCluster(key, (k,jedis) -> {
				byte[] ret = jedis.get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					V value = SerializeUtil.unserialize(ret);
					if (type != null && !type.isInstance(value)) {
						throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
					}
					return value;
				}
				return null;
			},modName);
		} else {
			return CacheExecute.execute(key, (k,jedis) -> {
				byte[] ret = jedis.get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					V value = SerializeUtil.unserialize(ret);
					if (type != null && !type.isInstance(value)) {
						throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
					}
					return value;
				}
				return null;
			},modName);
		}
	  }
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	@Override
	public void add(K key, V value) throws UncheckedException {
		add(key, CacheExpireTimeEnum.CACHE_EXP_DAY.timesanmp(), value);
	}
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	@Override
	public void add(K key, int export,  V value) throws UncheckedException {
		set(key, export,value);
	}
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	@Override
	public boolean set(K key, V value) throws UncheckedException {
		return set(key, CacheExpireTimeEnum.CACHE_EXP_DAY.timesanmp(), value);
	}
	
  
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param exportTime 超时时间 妙
	 * @param value 对象值
	 */
	@Override
	public boolean set(K key, int exportTime,  V value) throws UncheckedException {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean ret = CacheExecute.executeCluster(key, (k,jedis) ->  {
				String result = jedis.set(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				if(exportTime > 0){
					jedis.expire(SerializeUtil.serialize(k), exportTime);
				}
				return result.equalsIgnoreCase("OK");
			},modName);
			return ret;
		} else {
			Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
				String result = jedis.set(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				if(exportTime > 0){
					jedis.expire(SerializeUtil.serialize(k), exportTime);
				}
				return result.equalsIgnoreCase("OK");
			},modName);
			return ret;
		}
	}
	
   /**
	 * 
	 * 方法用途: 将给定key的值设为value，并返回key的旧值<br>
	 * 操作步骤: 当key存在但不是字符串类型时，返回一个错误<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getAndSet(K key, Object value) {
		if (redisPoolProperties.getOfficialCluster()) {
			return CacheExecute.executeCluster(key, (k, jedis) -> {
				byte[] bytes = jedis.getSet(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				if (bytes != null && bytes.length > 0) {
					return SerializeUtil.unserialize(bytes);
				}
				return null;
			}, modName);
		} else {
			return CacheExecute.execute(key, (k, jedis) -> {
				byte[] bytes = jedis.getSet(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				if (bytes != null && bytes.length > 0) {
					return SerializeUtil.unserialize(bytes);
				}
				return null;
			}, modName);
		}
		
	}
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	public boolean delete(K key) throws UncheckedException {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean result = CacheExecute.executeCluster(key, (k,jedis) -> {
				Long res = jedis.del(SerializeUtil.serialize(k));
				if(res==0) {
					return true;
				} else {
					return false;
				}
			},modName);
			return result;
		} else {
			Boolean result = CacheExecute.execute(key, (k,jedis) -> {
				Long res = jedis.del(SerializeUtil.serialize(k));
				if(res==0) {
					return true;
				} else {
					return false;
				}
			},modName);
			return result;
		}
	}
	
  @Override  
  public void clear() {  
//      redisClient.flush();  
//		for (String key : keySet) {
//			redisClient.deleteWithNoReply(this.getKey(key));
//		}
	}
	 
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 有冲突为TRUE无为FALSE
	 */
	public boolean isMutex(K key) throws UncheckedException {
		return isMutex(key, CacheExpireTimeEnum.MUTEX_EXP);
	} 

	/**
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 冲突判定<br>
	 * 操作步骤: <br>
	 * @param key  保存键
	 * @param export  超时时间
	 * @return 有冲突为TRUE无为FALSE
	 */
	public boolean isMutex(K key, CacheExpireTimeEnum export) throws UncheckedException {
		return false;
	}
  
	/**
	 * 
	 * 方法用途: 将key的值设为value，当且仅当key不存在<br>
	 * 操作步骤: 若给定的key已经存在，则SETNX不做任何动作
	 * SETNX是”SET if Not eXists”(如果不存在，则SET)的简写<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean setnx(K key, V value) {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean ret = CacheExecute.executeCluster(key, (k,jedis) -> {
				long result = jedis.setnx(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				return result > 0;
			},modName);
			return ret;
		} else {
			Boolean ret = CacheExecute.execute(key, (k,jedis) -> {
				long result = jedis.setnx(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				return result > 0;
			},modName);
			return ret;
		}

		
	}

	/**
	 * 
	 * 方法用途: 将值value关联到key，并将key的生存时间设为seconds(以秒为单位)<br>
	 * 操作步骤: 如果key 已经存在，SETEX命令将覆写旧值。 原子性(atomic)操作<br>
	 * 
	 * @param key
	 * @param seconds
	 * @param value
	 * @return
	 */
	public boolean setex(K key, int seconds, V value) {
		if (redisPoolProperties.getOfficialCluster()) {
			Boolean ret = CacheExecute.executeCluster(key, (k,jedis) -> {
				String result = jedis.setex(SerializeUtil.serialize(k), seconds, SerializeUtil.serialize(value));
				return result.equalsIgnoreCase("OK");
			},modName);
			return ret;
		} else {
			Boolean ret = CacheExecute.execute(key, (k,jedis) -> {
				String result = jedis.setex(SerializeUtil.serialize(k), seconds, SerializeUtil.serialize(value));
				return result.equalsIgnoreCase("OK");
			},modName);
			return ret;
		}

		
	}
	
}