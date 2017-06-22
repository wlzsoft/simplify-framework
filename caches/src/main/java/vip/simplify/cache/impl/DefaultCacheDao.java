package vip.simplify.cache.impl;

import vip.simplify.cache.ICacheDao;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.enums.TimeEnum;
import vip.simplify.ioc.annotation.Bean;

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
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@Bean("cachedDao")
public class DefaultCacheDao<K,V> implements ICacheDao<K,V> {
	
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
	public void add(K key, int expireTime, V value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean set(K key, V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean set(K key, int expireTime, V value) {
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
	public boolean isMutex(K key, CacheExpireTimeEnum expireTime) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long expire(String key, int expireTime, TimeEnum seconds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long expire(byte[] key, int expireTime, TimeEnum seconds) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getExpire(K key, TimeEnum seconds) {
		// TODO Auto-generated method stub
		return 0;
	}



}
