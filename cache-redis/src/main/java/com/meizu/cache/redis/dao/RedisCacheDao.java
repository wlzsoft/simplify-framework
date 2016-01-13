package com.meizu.cache.redis.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.Constants;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.exception.CacheException;
import com.meizu.cache.redis.RedisClientPool;
import com.meizu.cache.util.DefaultCodec;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.ByteUtil;
import com.meizu.simplify.utils.ReflectionUtil;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 缓存操作基类
 *
 */
//@Component
public abstract class RedisCacheDao<T extends Serializable> implements ICacheDao{
	private static final Logger log = LoggerFactory.getLogger(RedisCacheDao.class);
    //序列化
    protected DefaultCodec codec = new DefaultCodec();
    private String mod_name;
    
    
    private ShardedJedisPool shardedJedisPool;
    protected Class<? extends RedisCacheDao> entityClass;

    public RedisCacheDao(){
        this.entityClass = ReflectionUtil.getSuperClassGenricType(getClass());
    }
//    public BaseCacheClient(String name, int expire, RedisClient redisClient) {
//	      this.name = name;
//	      this.expire = expire;
//			this.redisClient = redisClient;
//	    }
    
    public String getName() {
    	return mod_name;	
    }
    
    public RedisCacheDao(String mod_name){
    	this.mod_name = mod_name;
    }
    
    protected ShardedJedis getClient(){
		return RedisClientPool.getJedisClient(mod_name);
    }
    
    protected void returnClient(ShardedJedis shardedJedis){
    	RedisClientPool.returnJedisClient(mod_name, shardedJedis);
    }
    
    protected void returnBrokenResource(ShardedJedis shardedJedis){
    	RedisClientPool.returnBrokenResource(mod_name, shardedJedis);
    }
    
    protected byte[] getByteKey(String key){
    	if(key == null){
    		return null;
    	}
    	return key.getBytes();
    }
    
    /**
     * del
     * @param key
     * @return
     */
    public Long del(String key){
    	 ShardedJedis jedis = this.getClient();
         try {
             return jedis.del(key);
         } catch (Exception e) {
             log.error("del error  key["+key+"]", e);
             this.returnBrokenResource(jedis);
             return 0L;
         } finally {
             this.returnClient(jedis);
         }
    }
  
    /**
     * 缓存里面是否存在该key
     * @param key
     * @return
     */
    public boolean exists(String key){
    	 ShardedJedis jedis = this.getClient();
         try {
             return jedis.exists(key);
         } catch (Exception e) {
             log.error("exists error  key["+key+"]", e);
             this.returnBrokenResource(jedis);
             return false;
         } finally {
             this.returnClient(jedis);
         }
    }
    
	
	 

	    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
	        this.shardedJedisPool = shardedJedisPool;
	    }

	    public ShardedJedis getConnection() {
	        ShardedJedis jedis=null;
	        try {
	            jedis=shardedJedisPool.getResource();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return jedis;
	    }

	    public void closeConnection(ShardedJedis jedis) {
	        if (null != jedis) {
	            try {
	                shardedJedisPool.returnResource(jedis);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }

	    public void put(String key,T entity){
	    	 ShardedJedis jedis = null;
	        try {
	            jedis=shardedJedisPool.getResource();
	            jedis.set(key.getBytes(), ByteUtil.ObjectToByte(entity));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	 shardedJedisPool.returnResource(jedis);
	        }
	    }

	    /**
		 * 方法用途: 返回值 TODO 操作步骤: <br>
		 * 
		 * @param key
		 *            保存键
		 * @return 缓存保存的对象
		 */
		@Override
		public Object get(String key) {

	        byte[] entityByte = null;
	        ShardedJedis jedis = null;
	        try {
	        	jedis =shardedJedisPool.getResource();
	            entityByte =jedis.get(key.getBytes());
	            return (T) ByteUtil.ByteToObject(entityByte);
	        } catch (Exception e) {
	            e.printStackTrace();
//	    	} catch (TimeoutException e) {
//				log.warn("获取 redis 缓存超时", e);
//				throw new UncheckedException(e);
//			} catch (InterruptedException e) {
//				log.warn("获取 redis 缓存被中断", e);
//				throw new UncheckedException(e);
//			} catch (RedisException e) {
//				log.warn("获取 redis 缓存错误", e);
//				throw new UncheckedException(e);
//			}
	        }finally{
	        	shardedJedisPool.returnResource(jedis);
	        }
	        return null;
	    }

	    public void remove(String key){
	    	 ShardedJedis jedis = null;
	        try {
	            jedis=shardedJedisPool.getResource();
	            jedis.del(key);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally{
	        	shardedJedisPool.returnResource(jedis);
	        }
	    }

		

			private Set<String> keySet = new HashSet<String>();
			private int expire;

		   
			

			public void put(String key, Object value) {
			}

			

			private String getKey(String key) {
				return "name" + "_" + key;
			}

			/**
			 * 方法用途: 添加值 操作步骤: 通过ADD添加数据时不允许相同键值<br>
			 * @param key
			 *            保存键
			 * @param value
			 *            对象值
			 */
			@Override
			public void add(String key, Object value) throws UncheckedException {
				add(key, Constants.CACHE_EXP_DAY, value);
			}

			/**
			 * 方法用途: 添加值 操作步骤: 通过ADD添加数据时不允许相同键值<br>
			 * 
			 * @param key
			 *            保存键
			 * @param export
			 *            超时时间
			 * @param value
			 *            对象值
			 */
			@Override
			public void add(String key, int export, Object value) {
			}

			/**
			 * 方法用途: 替换 操作步骤: <br>
			 * 
			 * @param key
			 *            保存键
			 * @param value
			 *            对象值
			 */
			public void replace(String key, Object value) throws UncheckedException {
				replace(key, Constants.CACHE_EXP_DAY, value);
			}

			/**
			 * 方法用途: 添加值 操作步骤: <br>
			 * 
			 * @param key
			 *            保存键
			 * @param export
			 *            超时时间
			 * @param value
			 *            对象值
			 */
			public void replace(String key, int export, Object value)
					throws UncheckedException {
			}

			/**
			 * 方法用途: 添加值 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
			 * 
			 * @param key
			 *            保存键
			 * @param value
			 *            对象值
			 */
//			@Override
//			public <T> T set(String key, T value) throws UncheckedException {
//				return set(key, 0, value);
//			}
			/**
			 * 方法用途: 添加值 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
			 * 
			 * @param key
			 *            保存键
			 * @param export
			 *            超时时间
			 * @param value
			 *            对象值
			 */
			public <T> T set(String key, int export, T value) throws UncheckedException {
				return value;
			}

			

			/**
			 * 方法用途: 删除值 TODO 操作步骤: <br>
			 * 
			 * @param key
			 *            保存键
			 * @return 删除成功为TRUE失败为FALSE
			 */
			@Override
			public boolean delete(String key) throws UncheckedException {
//				try {
//					key = this.getKey(key);
//					redisClient.deleteWithNoReply(key);
//					return true;
//				} catch (InterruptedException e) {
//					log.warn("删除 redis 缓存被中断", e);
//				} catch (RedisException e) {
//					log.warn("删除 redis 缓存错误", e);
//				}
				return false;
			}

			/**
			 * 方法用途: 冲突判定 操作步骤: <br>
			 * 
			 * @param key
			 *            保存键
			 * @return 有冲突为TRUE无为FALSE
			 */
			public boolean isMutex(String key) throws UncheckedException {
				return isMutex(key, Constants.MUTEX_EXP);
			}

			/**
			 * 方法用途: 冲突判定 操作步骤: <br>
			 * 
			 * @param key
			 *            保存键
			 * @param export
			 *            超时时间
			 * @return 有冲突为TRUE无为FALSE
			 */
			public boolean isMutex(String key, int export) throws UncheckedException {
				return false;
			}

			/**
			 * 
			 * 
			 * 方法用途: Object转byte[]<br>
			 * 操作步骤: TODO<br>
			 * @param obj
			 * @return
			 */
			private byte[] toByteArray(Object obj) {
				byte[] bytes = null;
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					ObjectOutputStream oos = new ObjectOutputStream(bos);
					oos.writeObject(obj);
					oos.flush();
					bytes = bos.toByteArray();
					oos.close();
					bos.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				return bytes;
			}
			/**
			 * 方法用途: byte[]转Object<br>
			 * 操作步骤: TODO<br>
			 * @param bytes
			 * @return
			 */
			private Object toObject(byte[] bytes) {
				Object obj = null;
				try {
					ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
					ObjectInputStream ois = new ObjectInputStream(bis);
					obj = ois.readObject();
					ois.close();
					bis.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
				return obj;
			}

		  
		    @Override  
		    public Object get(Object key) {  
		    	
		        Object object = null;  
		        try {  
		        	ShardedJedis jedis = null;
		            jedis=shardedJedisPool.getResource();
		        	object = jedis.get((String)key);
		        	
		        } catch (CacheException e) {  
		            e.printStackTrace();  
		        }  
		        return object;
		    }  
		  
		    
		    @Override  
		    public void put(Object key, Object value) {  
		        try {  
		        	ShardedJedis jedis = null;
		            jedis=shardedJedisPool.getResource();
		        	jedis.lpush((String)key, (String)value);  
		        } catch (CacheException e) {  
		            e.printStackTrace();  
		        }  
		    }  
		  
		    @Override  
		    public void evict(Object key) {  
		        try {  
		        	ShardedJedis jedis = null;
		            jedis=shardedJedisPool.getResource();
		        	jedis.del((String)key);  
		        } catch (CacheException e) {  
		            e.printStackTrace();  
		        }  
		    }  
		  
		    @Override  
		    public void clear() {  
//		        try {  
//		        	redisClient.flush();  
//		        } catch (CacheException e) {  
//		            e.printStackTrace();  
//		        }  
		    }
		    /*public void clear() {
				for (String key : keySet) {
					try {
						redisClient.deleteWithNoReply(this.getKey(key));
					} catch (InterruptedException e) {
						log.warn("删除 redis 缓存被中断", e);
					} catch (redisException e) {
						log.warn("删除 redis 缓存错误", e);
					}
				}
			}*/
		  
			@Override
			  public <T> T get(Object key, Class<T> type) {
			    Object cacheValue;
				try {
					ShardedJedis jedis = null;
		            jedis=shardedJedisPool.getResource();
					cacheValue = jedis.get(key.toString());
					Object value = (cacheValue != null ? cacheValue : null);
					if (type != null && !type.isInstance(value)) {
						throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
					}
					return (T) value;
				} catch ( CacheException e) {
					e.printStackTrace();
				}
				return null;
			  }
    
}