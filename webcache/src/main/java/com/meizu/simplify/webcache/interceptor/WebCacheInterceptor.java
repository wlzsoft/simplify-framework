package com.meizu.simplify.webcache.interceptor;

import java.lang.annotation.Annotation;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.aop.Context;
import com.meizu.simplify.aop.Handler;
import com.meizu.simplify.aop.IInterceptor;
import com.meizu.simplify.aop.enums.ContextTypeEnum;
import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.cache.annotation.CacheDataDel;
import com.meizu.simplify.cache.annotation.CacheDataSearch;
import com.meizu.simplify.cache.resolver.CacheAnnotationResolver;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;

/**
 * <p><b>Title:</b><i>web缓存拦截器</i></p>
 * <p>Desc: TODO </p>
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
@Bean(type=BeanTypeEnum.PROTOTYPE)
public class WebCacheInterceptor extends Handler implements  IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(WebCacheInterceptor.class);
	
	private static final WebCacheInterceptor CACHE_BEFORE_INTERCEPTOR = new WebCacheInterceptor();
	private static final WebCacheInterceptor CACHE_AFTER_INTERCEPTOR = new WebCacheInterceptor(); 
	private WebCacheInterceptor() {
		
	}
	public static WebCacheInterceptor getBeforeInstance() {
		return CACHE_BEFORE_INTERCEPTOR;
	}
	public static WebCacheInterceptor getAfterInstance() {
		return CACHE_AFTER_INTERCEPTOR;
	}
	
	@Override
	public boolean before(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
		Object o = context.getThiz();
		LOGGER.info("缓存切面切入：["+methodFullName+"]方法之前 切入");
//		System.out.println("缓存切面切入：["+methodFullName+"]方法之前 切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
//		String key = methodFullName+"id";//需要想方法获取id的值TODO 废弃，不采用这种key的处理方式
		Map<String,AnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		AnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataSearch.class)) {
			CacheDataSearch cacheDataSearch = (CacheDataSearch)anno;
			Object obj = null;//data.get(cacheDataSearch.key());
			if(obj == null) {
				return false;
			}
			context.getCallback().setResult(obj);
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
		Map<String,AnnotationInfo> cacheAnnotationInfoMap = CacheAnnotationResolver.cacheAnnotationInfoMap;
		AnnotationInfo cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		if(anno.annotationType().equals(CacheDataAdd.class)) {
			CacheDataAdd cacheDataAdd = (CacheDataAdd)anno;
			//TODO　这块的操作要控制的2ms以内
			boolean isOk = false;//data.set(cacheDataAdd.key(), args[0]);
			LOGGER.debug("add key:"+cacheDataAdd.key()+"]"+isOk);
//			System.out.println("add key:"+cacheDataAdd.key()+"]"+isOk);
		} else if(anno.annotationType().equals(CacheDataDel.class)) {
			CacheDataDel cacheDataDel = (CacheDataDel)anno;
			Object obj = null;//data.delete(cacheDataDel.key());
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
			if(context.getCallback().getResult() == null) {
				after(context,obj);
			}
		}
		return true;
	}

	
	
}
