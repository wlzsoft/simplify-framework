package vip.simplify.cache.redis.dao.impl;

import com.alibaba.fastjson.TypeReference;

import vip.simplify.cache.dao.IJsonCacheDao;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.CacheExecute;
import vip.simplify.cache.redis.dao.BaseRedisDao;
import vip.simplify.exception.UncheckedException;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.util.JsonResolver;
import vip.simplify.utils.JsonUtil;


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
		
		Boolean isSuccess = CacheExecute.execute(key, (k,jedis) -> {
				String result = jedis.set(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
				if(expireTime > 0){
					jedis.expire(k, expireTime);
				}
				return result.equalsIgnoreCase("OK");
		}, modName);
		return isSuccess;
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
    	Boolean isSuccess = CacheExecute.execute(key, (k,jedis) ->  {
				 long result = jedis.setnx(k, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
		         return result > 0;
		}, modName);
        return isSuccess;
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
    	Boolean isSuccess = CacheExecute.execute(key, (k,jedis) ->  {
				String result = jedis.setex(k, seconds, BeanFactory.getBean(JsonResolver.class).ObjectToString(value));
	            return result.equalsIgnoreCase("OK");
		}, modName);
        return isSuccess;
    }
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: 注意：这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO<br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	public boolean delete(String key) throws UncheckedException {
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
