package com.meizu.cache.redis;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.CacheException;
import com.meizu.cache.Constants;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.impl.Cache;
import com.meizu.cache.impl.ValueWrapper;
import com.meizu.simplify.exception.UncheckedException;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月13日 下午5:31:27</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月13日 下午5:31:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class RedisCacheDao implements ICacheDao {
	

    private final String name;
    
    private static final long serialVersionUID = 4133511643649980250L;
	private static Logger log = LoggerFactory.getLogger(RedisCacheDao.class);


	/**
	 * 缓存客户端
	 */
	@Resource
	private RedisClient redisClient;

	/**
	 * 缓存客户端
	 */
	@Resource
	private RedisClient slaveredisClient;

	private Set<String> keySet = new HashSet<String>();
	private final int expire;

    public RedisCacheDao(String name, int expire, RedisClient redisClient) {
      this.name = name;
      this.expire = expire;
		this.redisClient = redisClient;
    }
	

	public void put(String key, Object value) {
	}

	

	private String getKey(String key) {
		return name + "_" + key;
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
//	@Override
//	public <T> T set(String key, T value) throws UncheckedException {
//		return set(key, 0, value);
//	}
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
	 * 方法用途: 返回值 TODO 操作步骤: <br>
	 * 
	 * @param key
	 *            保存键
	 * @return 缓存保存的对象
	 */
	@Override
	public Object get(String key) throws UncheckedException {
		Object value = null;
//		try {
			key = this.getKey(key);
			value = redisClient.get(key);
//		} catch (TimeoutException e) {
//			log.warn("获取 redis 缓存超时", e);
//			throw new UncheckedException(e);
//		} catch (InterruptedException e) {
//			log.warn("获取 redis 缓存被中断", e);
//			throw new UncheckedException(e);
//		} catch (RedisException e) {
//			log.warn("获取 redis 缓存错误", e);
//			throw new UncheckedException(e);
//		}
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
//		try {
//			key = this.getKey(key);
//			redisClient.deleteWithNoReply(key);
//			return true;
//		} catch (InterruptedException e) {
//			log.warn("删除 redis 缓存被中断", e);
//		} catch (RedisException e) {
//			log.warn("删除 redis 缓存错误", e);
//		}
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
    public ValueWrapper get(Object key) {  
    	
        Object object = null;  
        try {  
        	object = redisClient.get((String)key);
        } catch (CacheException e) {  
            e.printStackTrace();  
        }  
        return (ValueWrapper) object;
    }  
  
   /* @Override
	public void put(Object key, Object value) {
		final String keyf = (String) key;
		final Object valuef = value;
		final long liveTime = 86400;

		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyb = keyf.getBytes();
				byte[] valueb = toByteArray(valuef);
				connection.set(keyb, valueb);
				if (liveTime > 0) {
					connection.expire(keyb, liveTime);
				}
				return 1L;
			}
		});
	}*/
    
    @Override  
    public void put(Object key, Object value) {  
        try {  
            redisClient.put(key, value);  
        } catch (CacheException e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void evict(Object key) {  
        try {  
        	redisClient.remove((String)key);  
        } catch (CacheException e) {  
            e.printStackTrace();  
        }  
    }  
  
    @Override  
    public void clear() {  
//        try {  
//        	redisClient.flush();  
//        } catch (CacheException e) {  
//            e.printStackTrace();  
//        }  
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
			cacheValue = redisClient.get(key.toString());
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

