package com.meizu.simplify.aop.cache;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.aop.Context;
import com.meizu.simplify.aop.Handler;
import com.meizu.simplify.aop.IInterceptor;
import com.meizu.simplify.aop.enums.ContextTypeEnum;
import com.meizu.simplify.cache.ICacheDao;
import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.annotation.CacheDataDel;
import com.meizu.simplify.cache.annotation.CacheDataSearch;
import com.meizu.simplify.cache.dto.CacheAnnotationInfo;
import com.meizu.simplify.cache.redis.dao.impl.CommonRedisDao;
import com.meizu.simplify.cache.resolver.CacheAnnotationResolver;

/**
 * <p><b>Title:</b><i>缓存拦截器-后期剥离成一个插件</i></p>
 * <p>Desc: 注意： 1.考虑使用观察者模式替代责任连模式
 *                2.目前的责任连实现方案，有个bug，before和after共用了一个对象，在并发情况下会导致链条关系互串，要改成两个对象
 *                责任连有以下问题：1.链条断裂，2.排队问题（性能问题），3.并发问题
 *                3.修改成两个对象后，考虑以后优化，减少冗余代码，暂不实现要做标记，考虑和ioc框架集成4，考虑修改成责任连对业务的影响，
 *                4.cache和log类的handler实现类，都调整成有直接后继的方式，都返回true
 *                </p>
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
	
	private static final CacheInterceptor CACHE_BEFORE_INTERCEPTOR = new CacheInterceptor();
	private static final CacheInterceptor CACHE_AFTER_INTERCEPTOR = new CacheInterceptor(); 
	private CacheInterceptor() {
		
	}
	public static CacheInterceptor getBeforeInstance() {
		return CACHE_BEFORE_INTERCEPTOR;
	}
	public static CacheInterceptor getAfterInstance() {
		return CACHE_AFTER_INTERCEPTOR;
	}
	
	ICacheDao<String, Object> data = CacheProxyDao.getCache();
	@Override
	public boolean before(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之前 切入");
//		System.out.println("缓存切面切入：["+methodFullName+"]方法之前 切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
//		String key = methodFullName+"id";//需要想方法获取id的值TODO 废弃，不采用这种key的处理方式
		Map<String,CacheAnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		CacheAnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataSearch.class)) {
			CacheDataSearch cacheDataSearch = (CacheDataSearch)anno;
			Object obj = data.get(cacheDataSearch.key());
			if(obj == null) {
				return false;
			}
			context.setResult(obj);
			LOGGER.debug("search key:"+cacheDataSearch.key()+"]"+obj);
//			System.out.println("search key:"+cacheDataSearch.key()+"]"+obj);
		} 
		return true;
	}
	
	@Override
	public boolean after(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之后切入");
//		System.out.println("缓存切面切入：["+methodFullName+"]方法之后切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
//		String key = methodFullName+"id";//需要想方法获取id的值TODO 废弃，不采用这种key的处理方式
		Map<String,CacheAnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		CacheAnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataAdd.class)) {
			CacheDataAdd cacheDataAdd = (CacheDataAdd)anno;
			//TODO　这块的操作要控制的2ms以内
			boolean isOk = data.set(cacheDataAdd.key(), args[0]);
			LOGGER.debug("add key:"+cacheDataAdd.key()+"]"+isOk);
//			System.out.println("add key:"+cacheDataAdd.key()+"]"+isOk);
		} else if(anno.annotationType().equals(CacheDataDel.class)) {
			CacheDataDel cacheDataDel = (CacheDataDel)anno;
			Object obj = data.delete(cacheDataDel.key());
			LOGGER.debug("del key:"+cacheDataDel.key()+"]"+obj);
//			System.out.println("del key:"+cacheDataDel.key()+"]"+obj);
		}
		return false;
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else {
			after(context,obj);
		}
		return true;
	}

	
	
}
