package com.meizu.cache.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.Constants;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.ICacheManager;
import com.meizu.cache.annotation.CacheEvict;
import com.meizu.cache.annotation.CachePut;
import com.meizu.cache.annotation.Cacheable;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;

/**
 * 
 * <p><b>Title:</b><i>缓存dao,目前只针对 Session缓存</i></p>
 * <p>Desc: cache组件启用连接后的 cache集群日志信息格式
 	"ServerName:[{}] ACTION:{},PASSPORT:{},PARAMS:{}."</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午4:34:46</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午4:34:46</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Bean("cachedDao")
public class CachedDaoImpl implements ICacheDao {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7277091732315928217L;
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Resource
    private CacheTemplate<String,Object> redisTemplate;
	@Resource
	private ICacheManager cacheManager;
	
	
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	@Override
	public void add(String key, Object value) throws UncheckedException {
		add(key, Constants.CACHE_EXP_DAY, value);
	}
	
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	@Override
	public void add(String key, int export,  Object value) throws UncheckedException {
	}
	
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 替换
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	public void replace(String key, Object value) throws UncheckedException {
		replace(key, Constants.CACHE_EXP_DAY, value);
	}
	
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 添加值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	public void replace(String key, int export, Object value) throws UncheckedException {
	}
	
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
//	public <T> T  set(String key, T value) throws UncheckedException {
//		return set(key, 0, value);
//	}
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param export 超时时间 妙
	 * @param value 对象值
	 */
	@Override
	@CachePut(value="session",key="#key",condition="#result != null")
	public <T> T set(String key, int export,  T value) throws UncheckedException {
		if(export>0) {
			logger.info("会话超时设置(默认30分钟)："+redisTemplate.expire(key, export,TimeUnit.SECONDS));
			logger.info("会话过期时间倒计时(秒)："+redisTemplate.getExpire(key,TimeUnit.SECONDS));
		}
		return value;
	}
	
	/** 
	 * 方法用途: 返回值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
	@Override
	@Cacheable(value="session",key="#key")
	public Object get(String key) throws UncheckedException {
		Cache cache = cacheManager.getCache("session");
		if(cache != null) {
//			return cache.get(key);
		} 
		return null;
	} 
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	@Override
	@CacheEvict(value="session",key="#key")
	public boolean delete(String key) throws UncheckedException {
		Cache cache = cacheManager.getCache("session");
		if(cache != null) {
//			cache.evict(key);
		} 
		return true;
	}
	 
	/** 
	 * 注意：通过注解实现，该方法不可用
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 有冲突为TRUE无为FALSE
	 */
    public boolean isMutex(String key) throws UncheckedException {  
        return isMutex(key, Constants.MUTEX_EXP);  
    }  
  
    /** 
     * 注意：通过注解实现，该方法不可用
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @return 有冲突为TRUE无为FALSE
	 */
    public boolean isMutex(String key, int export) throws UncheckedException {
		return false;  
    }


	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void evict(Object key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	

}
