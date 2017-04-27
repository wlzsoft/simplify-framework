package vip.simplify.webcache.interceptor;

import java.lang.annotation.Annotation;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.aop.Context;
import vip.simplify.aop.Handler;
import vip.simplify.aop.IInterceptor;
import vip.simplify.aop.enums.ContextTypeEnum;
import vip.simplify.dto.AnnotationInfo;
import vip.simplify.encrypt.sign.md5.MD5Encrypt;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.enums.BeanTypeEnum;
import vip.simplify.utils.ClearCommentUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.webcache.annotation.WebCache;
import vip.simplify.webcache.resolver.WebCacheAnnotationResolver;
import vip.simplify.webcache.util.BrowserUtil;
import vip.simplify.webcache.web.BrowserCache;
import vip.simplify.webcache.web.Cache;
import vip.simplify.webcache.web.CacheBase;

/**
 * <p><b>Title:</b><i>web缓存拦截器</i></p>
 * <p>Desc: TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月19日 上午10:13:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月19日 上午10:13:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean(type=BeanTypeEnum.PROTOTYPE)
public class WebCacheInterceptor extends Handler implements  IInterceptor{

	private static final Logger LOGGER = LoggerFactory.getLogger(WebCacheInterceptor.class);
	
	@Resource
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
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
	public boolean before(Context context, Object... args) {
		String methodFullName = context.getMethodFullName();
//		Object o = context.getThiz();
		LOGGER.info("web缓存切面切入：["+methodFullName+"]方法之前 切入");
//		System.out.println("缓存切面切入：["+methodFullName+"]方法之前 切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
//		String key = methodFullName+"id";//需要想方法获取id的值TODO 废弃，不采用这种key的处理方式
		Map<String,AnnotationInfo<WebCache>> cacheAnnotationInfoMap = WebCacheAnnotationResolver.webCacheAnnotationInfoMap;
		AnnotationInfo<WebCache> cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		// 检查静态规则配置
		if(anno.annotationType().equals(WebCache.class)) {
			WebCache webCache = (WebCache)anno;
			
					
			Cache cache = CacheBase.getCache(webCache);
			if(cache != null){
				//这两行代码和下面重复，并且重复执行了一次
				String url = request.getServerName() + request.getRequestURI() + StringUtil.parseString(StringUtil.trim(request.getQueryString()),"");
				String staticName = MD5Encrypt.sign(url) + ".lv";
				//end
				String cacheContent =cache.readCache(webCache, staticName);
				if(cacheContent != null){
					if(response!=null&&webCache.enableBrowerCache()) {
						BrowserUtil.enableBrowerCache(response,webCache.timeToLiveSeconds());
					}
					//返回缓存的页面
					context.getCallback().setResult(cacheContent);
					LOGGER.debug("返回请求["+webCache+"]的缓存页面内容");
				}
			}
		} 
		return true;
	}
	
	@Override
	public boolean after(Context context,Object... args) {
		String methodFullName = context.getMethodFullName();
//		Object o = context.getThiz();
		LOGGER.info("web缓存切面切入：["+methodFullName+"]方法之后切入");
//		System.out.println("缓存切面切入：["+methodFullName+"]方法之后切入");
		//TODO 需要在存入redis之前对key进行优化精简，不要保存很长的一个字符串，把方法全名做一个16进制列表的对于关系，redis只保存最简短的16进制数据
//		String key = methodFullName+"id";//需要想方法获取id的值TODO 废弃，不采用这种key的处理方式
		Map<String,AnnotationInfo<WebCache>> cacheAnnotationInfoMap = WebCacheAnnotationResolver.webCacheAnnotationInfoMap;
		AnnotationInfo<WebCache> cacheAnnoInfo = cacheAnnotationInfoMap.get(methodFullName);
		Annotation anno = cacheAnnoInfo.getAnnotatoionType();
		// 检查静态规则配置
		if(anno.annotationType().equals(WebCache.class)) {
			WebCache webCache = (WebCache)anno;
			LOGGER.debug("webCache: timeToLiveSeconds="+webCache.timeToLiveSeconds()+
			",enableBrowerCache="+webCache.enableBrowerCache()+
			",mode="+webCache.mode()+
			",removeSpace="+webCache.removeSpace());
			
//1
			// 跳转前检查静态规则
			if (webCache != null && webCache.mode() != WebCache.CacheMode.nil) {
				//页面级别内容
				String content = (String) args[0];
//				String content = getPageContent(request, response, rd);//jvm内存缓存,JspTemplate生成，然后传递过来
//				String content = vw.toString();//文件缓存，Velocity中生成，然后传递过来
				// 是否去除空格
				if(webCache.removeSpace()) {
					content = ClearCommentUtil.clear(content);
					content = StringUtil.removeHtmlSpace(content);
				}
				BrowserCache.setCache(webCache, response);
				Cache cache = CacheBase.getCache(webCache);
				String url = request.getServerName() + request.getRequestURI() + StringUtil.parseString(StringUtil.trim(request.getQueryString()),"");
				String staticName = MD5Encrypt.sign(url) + ".lv";
				if(cache != null && cache.setCache(webCache, staticName, content)){
					// 缓存成功.
				}
			}
		
		}
		return false;
	}

	@Override
	public boolean handle(Context context,Object... obj) {
		if(context.getType().equals(ContextTypeEnum.BEFORE)) {
			before(context,obj);
		} else if(context.getType().equals(ContextTypeEnum.AFTER)) {
			if(context.getCallback().getResult() == null) {
				after(context,obj);
			}
		} else if(context.getType().equals(ContextTypeEnum.EXCEPTION)) {
			exception(context, obj);
		} else {
			finallyer(context, obj);
		}
		return true;
	}

	
	
}
