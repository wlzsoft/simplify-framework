package com.meizu.cache.impl;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.ICacheDao;
import com.meizu.cache.ICacheManager;
import com.meizu.cache.annotation.CacheEvict;
import com.meizu.cache.annotation.CachePut;
import com.meizu.cache.annotation.Cacheable;
import com.meizu.cache.enums.CacheExpireTimeEnum;
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
public class DefaultCacheDao<K,V> implements ICacheDao<K,V> {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public boolean exists(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(K key, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void add(K key, CacheExpireTimeEnum export, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean set(K key, V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean set(K key, CacheExpireTimeEnum export, V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isMutex(K key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMutex(K key, CacheExpireTimeEnum export) {
		// TODO Auto-generated method stub
		return false;
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
