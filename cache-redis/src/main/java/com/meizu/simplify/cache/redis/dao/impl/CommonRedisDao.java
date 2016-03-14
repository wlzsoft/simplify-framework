package com.meizu.simplify.cache.redis.dao.impl;

import java.io.Serializable;

import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.SerializeUtil;


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
	
	public CommonRedisDao(String mod_name) {
		super(mod_name);
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
		Boolean ret = CacheExecute.execute(key, k -> {
  				return CacheExecute.getJedis(mod_name).exists(k.toString());
  		},mod_name);
		return ret;
			
	}
  
	/**
	 * 方法用途: 返回值 <br>
	 * 操作步骤: <br>
	 * @param key  保存键
	 * @return 缓存保存的对象
	 */
	@Override
	public V get(K key) {

		return CacheExecute.execute(key, k ->  {
				byte[] ret = CacheExecute.getJedis(mod_name).get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					return (V) SerializeUtil.unserialize(ret);
				}
				return null;
		},mod_name);
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
		return CacheExecute.execute(key, k -> {
				byte[] ret = CacheExecute.getJedis(mod_name).get(SerializeUtil.serialize(k));
				if (ret != null && ret.length > 0) {
					V value = (V) SerializeUtil.unserialize(ret);
					if (type != null && !type.isInstance(value)) {
						throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
					}
					return value;
				}
				return null;
		},mod_name);
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
		
		Boolean ret = CacheExecute.execute(key, k ->  {
  				String result = CacheExecute.getJedis(mod_name).set(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
  	            if(export.timesanmp() > 0){
  	            	CacheExecute.getJedis(mod_name).expire(SerializeUtil.serialize(k), export.timesanmp());
  			    }
			    return result.equalsIgnoreCase("OK");
  		},mod_name);
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

		return CacheExecute.execute(key, k -> {
				byte[] bytes = CacheExecute.getJedis(mod_name).getSet(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
				if (bytes != null && bytes.length > 0) {
					return SerializeUtil.unserialize(bytes);
				}
				return null;
		},mod_name);
		
	}
	
	
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	public boolean delete(K key) throws UncheckedException {
		
		Boolean ret = CacheExecute.execute(key, k -> {
  				 Long res = CacheExecute.getJedis(mod_name).del(SerializeUtil.serialize(k));
  		      	 if(res==0) {
  		      		 return true;
  		      	 } else {
  		      		 return false;
  		      	 }
  		},mod_name);
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
		
		Boolean ret = CacheExecute.execute(key, k -> {
  				long result = CacheExecute.getJedis(mod_name).setnx(SerializeUtil.serialize(k), SerializeUtil.serialize(value));
  				return result > 0;
  		},mod_name);
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
		Boolean ret = CacheExecute.execute(key, k -> {
  				String result = CacheExecute.getJedis(mod_name).setex(SerializeUtil.serialize(k), seconds, SerializeUtil.serialize(value));
  				return result.equalsIgnoreCase("OK");
  		},mod_name);
		return ret;
		
	}

	
}