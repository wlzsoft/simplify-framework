package com.meizu.simplify.ioc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.StartupTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtils;
import com.meizu.simplify.utils.CollectionUtils;
import com.meizu.simplify.utils.ReflectionUtils;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午6:20:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午6:20:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class Startup {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);
	public static StartupTypeEnum start() {
		
		List<Class<?>> resolveList = ClassUtils.findClassesByParentClass(IAnnotationResolver.class, "com.meizu");
		Map<Integer,Class<?>> mapResolve = new ConcurrentHashMap<Integer, Class<?>>();
		for (Class<?> clazz : resolveList) {
			Init init = clazz.getAnnotation(Init.class);
			mapResolve.put(init.value(), clazz);
		}
		mapResolve = CollectionUtils.sortMapByKey(mapResolve, true);
		for (Class<?> clazz : mapResolve.values()) {
			LOGGER.info("resolver invoke:{}",clazz.getName());
			try {
				Object obj = clazz.newInstance();
				ReflectionUtils.invokeMethod(obj, "resolve", null, new Class[]{List.class}, new Object[]{new ArrayList()});
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return StartupTypeEnum.SUCCESS;
	}

}
