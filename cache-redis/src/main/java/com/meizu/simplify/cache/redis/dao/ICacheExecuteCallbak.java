package com.meizu.simplify.cache.redis.dao;

import redis.clients.jedis.ShardedJedis;

/**
  * <p><b>Title:</b><i>缓存回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午2:52:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月26日 下午2:52:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@FunctionalInterface//可以省略掉，默认升级为function接口
public interface ICacheExecuteCallbak<K,V> {

	V call(K key,ShardedJedis jedis);

}
