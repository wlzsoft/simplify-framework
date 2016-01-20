package com.meizu.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.dto.CacheAnnotationInfo;
import com.meizu.cache.exception.CacheException;
import com.meizu.mvc.annotation.RequestMap;
import com.meizu.mvc.annotation.RequestParam;
import com.meizu.mvc.dto.MappingAnnotationInfo;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>mvc请求地址解析器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(4)
public class ControllerAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAnnotationResolver.class);
	
	public static final Map<String,MappingAnnotationInfo> mappingAnnotationInfo = new ConcurrentHashMap<>();
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
				throw new CacheException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
			}
			
			for (Method method : methodArr) {
                if (method.isAnnotationPresent(RequestMap.class)) {
                	resolveAnno(beanClass, method,RequestMap.class);
                }
                if (method.isAnnotationPresent(RequestParam.class)) {
                	resolveAnno(beanClass, method,RequestParam.class);
                }
			}
		}
	}
	private <T extends Annotation> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno) {
		T requestInfo = method.getDeclaredAnnotation(clazzAnno);
		LOGGER.debug("请求映射注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
		MappingAnnotationInfo cai = new MappingAnnotationInfo();
		cai.setAnnotatoionType(requestInfo);
		cai.setReturnType(method.getReturnType());
		mappingAnnotationInfo.put(beanClass.getName()+":"+method.getName(), cai);
	}
}
