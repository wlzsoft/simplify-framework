package com.meizu.simplify.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanEntity;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.BeanHook;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;
import com.meizu.simplify.ioc.prototype.IBeanPrototypeHook;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;

/**
  * <p><b>Title:</b><i>配置属性单个注入解析器</i></p>
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
@Init(0)
public class ConfigAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		resolveList = ClassUtil.findClassesByAnnotationClass(Bean.class, "com.meizu");
		for (Class<?> clazz : resolveList) {
			LOGGER.info("配置信息注入 初始化:{}",clazz.getName());
			try {
				Bean beanAnnotation = clazz.getAnnotation(Bean.class);
        		if(beanAnnotation.type().equals(BeanTypeEnum.PROTOTYPE)) {
        			List<Class<?>> hookList = ClassUtil.findClassesByAnnotationClass(BeanHook.class, "com.meizu");
        			for (Class<?> hookClazz : hookList) {
						BeanHook hookBeanAnno = hookClazz.getAnnotation(BeanHook.class);
						Class<?> serviceClass = hookBeanAnno.value();
						if(serviceClass.equals(clazz)) {
							Object hookObj = hookClazz.newInstance();
							List<BeanEntity<?>> listObj = ((IBeanPrototypeHook)hookObj).hook(clazz);
							BeanFactory.addBeanList(listObj);
						}
					}
        		} else {
        			Object beanObj = clazz.newInstance();
        			BeanFactory.addBean(beanObj);
        		}
				
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				LOGGER.debug("配置信息:"+clazz.getName()+"初始化失败");
			}
			
		}
	}
}
