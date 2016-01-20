package com.meizu.aop.cache;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.aop.Context;
import com.meizu.aop.Handler;
import com.meizu.aop.IInterceptor;
import com.meizu.aop.enums.ContextTypeEnum;
import com.meizu.aop.log.LogInterceptor;
import com.meizu.cache.ICacheDao;
import com.meizu.cache.annotation.CacheDataAdd;
import com.meizu.cache.annotation.CacheDataDel;
import com.meizu.cache.annotation.CacheDataSearch;
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
public class CacheInterceptor extends Handler implements  IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(CacheInterceptor.class);
	
	private static final CacheInterceptor CACHE_INTERCEPTOR = new CacheInterceptor(); 
	private CacheInterceptor() {
		
	}
	public static CacheInterceptor getInstance() {
		return CACHE_INTERCEPTOR;
	}
	
	//TODO 需要优化，不应该每次都去获取连接，要设置初始化一个可用连接池，并初始化部分连接，这块的连接每次都去获取连接，很消耗性能
	ICacheDao<String, Object> commonRedisDao = new CommonRedisDao<>("redis_ref_hosts");
	
	@Override
	public void before(String methodFullName,Object o,Object... args) {
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之前 切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
		String key = methodFullName+"id";//需要想方法获取id的值
		Map<String,CacheAnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		CacheAnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataSearch.class)) {
			CacheDataSearch cacheDataSearch = (CacheDataSearch)anno;
			Object obj = commonRedisDao.get(key);
			LOGGER.debug("search key:"+cacheDataSearch.key()+"]"+obj);
		} 
	}
	
	@Override
	public void after(String methodFullName,Object o,Object... args) {
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之后切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
		String key = methodFullName+"id";//需要想方法获取id的值
		Map<String,CacheAnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		CacheAnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataAdd.class)) {
			CacheDataAdd cacheDataAdd = (CacheDataAdd)anno;
			//TODO　这块的操作要控制的2ms以内
			boolean isOk = commonRedisDao.set(key, args[0]);
			LOGGER.debug("add key:"+cacheDataAdd.key()+"]"+isOk);
		} else if(anno.annotationType().equals(CacheDataDel.class)) {
			CacheDataDel cacheDataDel = (CacheDataDel)anno;
			Object obj = commonRedisDao.delete(key);
			LOGGER.debug("del key:"+cacheDataDel.key()+"]"+obj);
		}
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context.getMethodFullName(),context.getThiz(),obj);
		} else {
			after(context.getMethodFullName(),context.getThiz(),obj);
		}
		return false;
	}

	
	
}
