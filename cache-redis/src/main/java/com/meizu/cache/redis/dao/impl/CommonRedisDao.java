package com.meizu.cache.redis.dao.impl;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.ICacheDao;
import com.meizu.cache.enums.CacheExpireTimeEnum;
import com.meizu.cache.exception.CacheException;
import com.meizu.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.exception.UncheckedException;

import redis.clients.jedis.ShardedJedis;


//@Component
public class CommonRedisDao<K extends Serializable,V,T extends Serializable> extends BaseRedisDao implements ICacheDao<K,V>{
	
  private static final Logger LOGGER = LoggerFactory.getLogger(CommonRedisDao.class);
  public CommonRedisDao(String mod_name) {
		super(mod_name);
  }

  /**
   * 缓存里面是否存在该key
   * @param key
   * @return
   */
  @Override
  public boolean exists(K key){
  	
  	 ShardedJedis jedis = client.getClient();
       try {
           return jedis.exists(key.toString());
       } catch (Exception e) {
      	   LOGGER.error("exists error  key["+key+"]", e);
           return false;
       } finally {
      	 client.returnClient(jedis);
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
      ShardedJedis jedis = client.getClient();
      try {
          byte[] ret = jedis.get(getByteKey(key));
          if (ret != null && ret.length > 0) {
              return (V) codec.decode(ret);
          }
          return null;
//        byte[] entityByte = null;
//        ShardedJedis jedis = null;
//        	jedis =shardedJedisPool.getResource();
//            entityByte =jedis.get(key.getBytes());
//            return (T) ByteUtil.ByteToObject(entityByte);
//    } catch (TimeoutException e) {
//			log.warn("获取 redis 缓存超时", e);
//			throw new UncheckedException(e);
//		} catch (InterruptedException e) {
//			log.warn("获取 redis 缓存被中断", e);
//			throw new UncheckedException(e);
//		} catch (RedisException e) {
//			log.warn("获取 redis 缓存错误", e);
//			throw new UncheckedException(e);
//		}
      } catch (Exception e) {
          LOGGER.error("get error!", e);
          return null;
      } finally {
      	client.returnClient(jedis);
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
  //@Override
	public  V get(K key, Class<T> type) {
	    Object cacheValue;
		try {
			ShardedJedis jedis =getConnection();
			cacheValue = jedis.get(key.toString());
			Object value = (cacheValue != null ? cacheValue : null);
			if (type != null && !type.isInstance(value)) {
				throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
			}
			return  (V) value;
		} catch ( CacheException e) {
			e.printStackTrace();
		}
		return null;
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
		
		
		ShardedJedis jedis = client.getClient();
      try {
          String ret = jedis.set(getByteKey(key), codec.encode(value));
          if(export.timesanmp() > 0){
				jedis.expire((String) key, export.timesanmp());
			}
          return ret.equalsIgnoreCase("OK");
      } catch (Exception e) {
          LOGGER.error("set error!", e);
          return false;
      } finally {
      	client.returnClient(jedis);
      }
		
		
		
//		if(export.timesanmp()>0) {
//			LOGGER.info("会话超时设置(默认30分钟)：");
//			LOGGER.info("会话过期时间倒计时(秒)：");
//			ShardedJedis jedis= null;
//			try {
//				jedis=shardedJedisPool.getResource();
//				jedis.set((byte[]) key, ByteUtil.ObjectToByte(value));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}finally{
//				this.returnClient(jedis);
//			}
//		}
//		return true;
		
		
	}
	
	
	/**
   * 方法用途: <p>将给定key的值设为value，并返回key的旧值。   </p>
   * <p>当key存在但不是字符串类型时，返回一个错误。    </p>
   *
   * @param key
   * @param value
   * @return
   */
  public Object getAndSet(K key, Object value) {
      ShardedJedis jedis = client.getClient();
      try {
          byte[] bytes = jedis.getSet(getByteKey(key),codec.encode(value));
          if (bytes != null && bytes.length > 0) {
              return codec.decode(bytes);
          }
          return null;
      } catch (Exception e) {
          LOGGER.error("getAndSet error!", e);
          return null;
      } finally {
          client.returnClient(jedis);
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
		ShardedJedis jedis = client.getClient();
		Long res = null;
       try {
      	 res = jedis.del(key.toString());
      	 if(res==0) {
      		 return true;
      	 }
//      	 catch (InterruptedException e) {
//   			log.warn("删除 redis 缓存被中断", e);
//   		} catch (RedisException e) {
//   			log.warn("删除 redis 缓存错误", e);
//   		}
       } catch (Exception e) {
      	 LOGGER.error("del error  key["+key+"]", e);
       } finally {
      	 client.returnClient(jedis);
       }
		return false;
	}
	
  @Override  
  public void clear() {  
//      try {  
//      	redisClient.flush();  
//      } catch (CacheException e) {  
//          e.printStackTrace();  
//      }  
//		for (String key : keySet) {
//			try {
//				redisClient.deleteWithNoReply(this.getKey(key));
//			} catch (InterruptedException e) {
//				log.warn("删除 redis 缓存被中断", e);
//			} catch (redisException e) {
//				log.warn("删除 redis 缓存错误", e);
//			}
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
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @return 有冲突为TRUE无为FALSE
	 */
  public boolean isMutex(K key, CacheExpireTimeEnum export) throws UncheckedException {
		return false;  
  }
  
  /**
   * 方法用途: <p> 将key的值设为value，当且仅当key不存在。   </p>
   * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
   * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p>
   *
   * @param key
   * @param value
   * @return
   */
  public boolean setnx(K key, Object value) {
      ShardedJedis jedis = client.getClient();
      try {
          long ret = jedis.setnx(getByteKey(key), codec.encode(value));
          return ret > 0;
      } catch (Exception e) {
          LOGGER.error("setnx error!", e);
          return false;
      } finally {
          client.returnClient(jedis);
      }
  }

  /**
   * 方法用途: <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。</p>
   * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作<p/>
   *
   * @param key
   * @param seconds
   * @param value
   * @return
   */
  public boolean setex(K key, int seconds, Object value) {
      ShardedJedis jedis = client.getClient();
      try {
          String ret = jedis.setex(getByteKey(key), seconds, codec.encode(value));
          return ret.equalsIgnoreCase("OK");
      } catch (Exception e) {
          LOGGER.error("setex error!", e);
          return false;
      } finally {
          client.returnClient(jedis);
      }
  }

	@Override
	public String expire(K key, CacheExpireTimeEnum export, TimeUnit seconds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExpire(K key, TimeUnit seconds) {
		// TODO Auto-generated method stub
		return null;
	}	
	
}