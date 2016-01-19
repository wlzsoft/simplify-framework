package com.meizu.aop;

import com.meizu.aop.cache.CacheInterceptor;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.redis.dao.impl.CommonRedisDao;

/**
 * <p><b>Title:</b><i>拦截器接口</i></p>
 * <p>Desc: 责任链方式处理，串联所有的拦截器实现</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:14:23</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:14:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IInterceptor {

	public static Object initBefore(String methodFullName,Object o,Object... args ) {
//		o.getClass().isAnnotationPresent(CacheDataAdd.class);
		new CacheInterceptor().before(args);
		return -1;
	}
	
	public static Object initAfter(String methodFullName,Object o,Object... args ) {
		new CacheInterceptor().after(args);
		return -1;
	}
	
	void before(Object... args);
	void after(Object... args);
}
