package com.meizu.simplify.cache;

/**
  * <p><b>Title:</b><i>缓存管理接口</i></p>
 * <p>Desc: volite，读写锁，分布式锁实现，和使用利弊</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午3:36:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午3:36:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface ICacheManager {

	ICacheDao<?,?> getCache(String name);

}
