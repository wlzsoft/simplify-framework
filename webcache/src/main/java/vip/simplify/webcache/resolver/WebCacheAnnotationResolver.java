package vip.simplify.webcache.resolver;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.dto.AnnotationInfo;
import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.util.CacheManager;
import vip.simplify.webcache.annotation.WebCache;
import vip.simplify.webcache.exception.WebCacheException;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.WEB_CACHE)
public class WebCacheAnnotationResolver implements IAnnotationResolver<Class<?>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(WebCacheAnnotationResolver.class);
	
	public static final Map<String,AnnotationInfo<WebCache>> webCacheAnnotationInfoMap = new ConcurrentHashMap<>();
	public WebCacheAnnotationResolver() {
		CacheManager.addCache("webCacheAnnotationInfoMap", webCacheAnnotationInfoMap);
	}
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			Method[] methodArr = null;
			try {
				methodArr = beanClass.getDeclaredMethods();
			} catch(NoClassDefFoundError e) {
				e.printStackTrace();
				throw new WebCacheException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
			}
			
			for (Method method : methodArr) {
                if (method.isAnnotationPresent(WebCache.class)) {
                	resolveAnno(beanClass, method,WebCache.class);
                }
			}
		}
	}
	private <T extends WebCache> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno) {
		T cacheDataAdd = method.getDeclaredAnnotation(clazzAnno);
		LOGGER.debug("web缓存注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
		AnnotationInfo<WebCache> cai = new AnnotationInfo<>();
		cai.setAnnotatoionType(cacheDataAdd);
		cai.setReturnType(method.getReturnType());
		webCacheAnnotationInfoMap.put(beanClass.getName()+":"+method.getName(), cai);
	}
}
