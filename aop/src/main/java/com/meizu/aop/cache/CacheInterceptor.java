package com.meizu.aop.cache;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.aop.IInterceptor;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.annotation.CacheDataAdd;
import com.meizu.cache.dto.CacheAnnotationInfo;
import com.meizu.cache.redis.dao.impl.CommonRedisDao;
import com.meizu.cache.resolver.CacheAnnotationResolver;

/**
 * <p><b>Title:</b><i>缓存拦截器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class CacheInterceptor implements IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheInterceptor.class);
	@Override
	public void after(String methodFullName,Object o,Object... args) {
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之后切入");
	}

	@Override
	public void before(String methodFullName,Object o,Object... args) {
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之前 切入");
		Map<String,CacheAnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		CacheAnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataAdd.class)) {
			CacheDataAdd cacheDataAdd = (CacheDataAdd)anno;
			ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
			boolean isOk = commonRedisDao.set("age", cacheDataAdd.key());
			LOGGER.debug("key:"+cacheDataAdd.key()+"]"+isOk);
		}
	}
	
}
