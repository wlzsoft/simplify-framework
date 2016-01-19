package com.meizu.cache.resolver;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.annotation.CacheDataAdd;
import com.meizu.cache.exception.CacheException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
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
@Init(3)
public class CacheAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAnnotationResolver.class);
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
                if (method.isAnnotationPresent(CacheDataAdd.class)) {
                	CacheDataAdd cacheDataAdd = method.getDeclaredAnnotation(CacheDataAdd.class);
                	LOGGER.debug(cacheDataAdd.key()+":key");
/*                	String message = "缓存初始化: "+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]";
                	Object iocBean = null;
                	if(iocType.isInterface()) {
                		List<Class<?>> clazzList = ClassUtil.findClassesByInterfaces(iocType,"com.meizu");
                		int clazzSize = clazzList.size();
                		if(clazzSize>1) {
                			throw new UncheckedException("接口："+iocType.getName()+"不允许有多个实现类");
                		}
                		if(clazzSize<1) {
                			throw new UncheckedException("接口："+iocType.getName()+"无实现类，无法注入bean");
                		}
                		iocType = clazzList.get(0);
                	}
                	iocBean = BeanFactory.getBean(iocType);
                	try {
                		field.setAccessible(true);
						field.set(beanObj, iocBean);
					} catch (IllegalArgumentException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	LOGGER.debug(message);
*/                }
			}
		}
	}
}
