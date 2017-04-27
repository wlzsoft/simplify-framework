package vip.simplify.cache;

import vip.simplify.cache.dao.IJsonCacheDao;
import vip.simplify.cache.dao.IListCacheDao;
import vip.simplify.cache.redis.dao.impl.CommonRedisDao;
import vip.simplify.cache.redis.dao.impl.JsonRedisDao;
import vip.simplify.cache.redis.dao.impl.ListRedisDao;

/**
 * <p><b>Title:</b><i>缓存代理工具</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午6:48:07</p>
 * <p>Modified By:wanghb-</p>
 * <p>Modified Date:2016年2月26日 下午6:48:07</p>
 * @author <a href="mailto:wanghb@meizu.com" >wanghb</a>
 * @version Version 0.1
 *
 */
public class CacheProxyDao {
	private static final ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	private static final IJsonCacheDao<Object> jsonRedisDao = new JsonRedisDao<>("redis_ref_hosts");
	private static final IListCacheDao listRedisDao = new ListRedisDao("redis_ref_hosts");
	
	public static ICacheDao<String, Object> getCache() {
		return commonRedisDao;
	}
	
	public static IJsonCacheDao<Object> getJsonCache() {
		return jsonRedisDao;
	}
	
	public static IListCacheDao getListCache() {
		return listRedisDao;
	}
}