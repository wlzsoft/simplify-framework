package com.meizu.simplify.cache.redis.dao.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.exception.CacheException;
import com.meizu.simplify.cache.redis.RedisPool;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;
import com.meizu.simplify.cache.redis.exception.RedisException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.SerializeUtil;

import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: 超时检测</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月14日 上午11:52:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月14日 上午11:52:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <K>
 * @param <V>
 * @param <T>
 */
//@Bean(type=BeanTypeEnum.PROTOTYPE)
public class CommonRedisDao<K extends Serializable,V,T extends Serializable> extends BaseRedisDao<K> implements ICacheDao<K,V>{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonRedisDao.class);

	private String mod_name;

	public CommonRedisDao(String mod_name) {
		super(mod_name);
		this.mod_name = mod_name;
	}
  
  
  
  /**
	 * 方法用途: 返回值 TODO 
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
	public <KK,VV> VV execute(KK key,ICacheExecuteCallbak<KK,VV> callback) {
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
					pool.returnResourceObject(getJedis());
				}
			} catch (Exception ex) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
  

  /**
   * 方法用途:缓存里面是否存在该key
   * 操作步骤: <br>
   * @param key
   * @return
   */
  @Override
  public boolean exists(K key){
  	
       try {
           return getJedis().exists(key.toString());
       } catch (Exception e) {
      	   LOGGER.error("exists error  key["+key+"]", e);
           return false;
       }
  }
  
  /**
	 * 方法用途: 返回值 TODO 
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
  @Override
	public V get(K key) {

		return execute(key, new ICacheExecuteCallbak<K, V>() {
			@Override
			public V call(K key) {
				byte[] ret = getJedis().get(SerializeUtil.serialize(key));
				if (ret != null && ret.length > 0) {
					return (V) SerializeUtil.unserialize(ret);
				}
				return null;
			}
		});
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
		return execute(key, new ICacheExecuteCallbak<K, V>() {
			@Override
			public V call(K key) {
				byte[] ret = getJedis().get(SerializeUtil.serialize(key));
				if (ret != null && ret.length > 0) {
					V value = (V) SerializeUtil.unserialize(ret);
					if (type != null && !type.isInstance(value)) {
						throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
					}
					return value;
				}
				return null;
			}
		});
	  }
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	@Override
	public void add(K key, V value) throws UncheckedException {
		add(key, CacheExpireTimeEnum.CACHE_EXP_DAY, value);
	}
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	@Override
	public void add(K key, CacheExpireTimeEnum export,  V value) throws UncheckedException {
		
	}
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	@Override
	public boolean set(K key, V value) throws UncheckedException {
		return set(key, CacheExpireTimeEnum.CACHE_EXP_DAY, value);
//		return set(key,value,-1);
	}
	
  
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param export 超时时间 妙
	 * @param value 对象值
	 */
	@Override
	public boolean set(K key, CacheExpireTimeEnum export,  V value) throws UncheckedException {
		
		Boolean ret = execute(key, new ICacheExecuteCallbak<K, Boolean>() {
  			@Override
  			public Boolean call(K key) {
  				String ret = getJedis().set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
  	            if(export.timesanmp() > 0){
  	        	    getJedis().expire(SerializeUtil.serialize(key), export.timesanmp());
  			    }
			    return ret.equalsIgnoreCase("OK");
  			}
  		});
        return ret;
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

		return execute(key, new ICacheExecuteCallbak<K, V>() {
			@Override
			public V call(K key) {
				byte[] bytes = getJedis().getSet(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
				if (bytes != null && bytes.length > 0) {
					return SerializeUtil.unserialize(bytes);
				}
				return null;
			}
		});
		
	}
	
	
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	public boolean delete(K key) throws UncheckedException {
		
		Boolean ret = execute(key, new ICacheExecuteCallbak<K, Boolean>() {
  			@Override
  			public Boolean call(K key) {
  				 Long res = getJedis().del(SerializeUtil.serialize(key));
  		      	 if(res==0) {
  		      		 return true;
  		      	 } else {
  		      		 return false;
  		      	 }
  			}
  		});
		return ret;
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
		
		Boolean ret = execute(key, new ICacheExecuteCallbak<K, Boolean>() {
  			@Override
  			public Boolean call(K key) {
  				long ret = getJedis().setnx(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
  				return ret > 0;
  			}
  		});
		return ret;
		
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
		Boolean ret = execute(key, new ICacheExecuteCallbak<K, Boolean>() {
  			@Override
  			public Boolean call(K key) {
  				String ret = getJedis().setex(SerializeUtil.serialize(key), seconds, SerializeUtil.serialize(value));
  				return ret.equalsIgnoreCase("OK");
  			}
  		});
		return ret;
		
	}

	
}