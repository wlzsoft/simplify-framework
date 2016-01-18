package com.meizu.aop;

import com.meizu.aop.cache.CacheInterceptor;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.redis.dao.impl.CommonRedisDao;

/**
  * <p><b>Title:</b><i>拦截器接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月18日 下午5:30:30</p>
 * <p>Modified By:Administrator-</p>
 * <p>Modified Date:2016年1月18日 下午5:30:30</p>
 * @author <a href="mailto:Administrator@meizu.com" title="邮箱地址">Administrator</a>
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
