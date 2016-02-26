package com.meizu.simplify.cache.redis.dao;
/**
  * <p><b>Title:</b><i>缓存回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月26日 下午2:52:00</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月26日 下午2:52:00</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface ICacheExecuteCallbak<K,V> {

	V call(K key);

}
