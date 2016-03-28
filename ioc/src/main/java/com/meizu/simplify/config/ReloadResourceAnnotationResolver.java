package com.meizu.simplify.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>配置实体注入解析器</i></p>
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
public class ReloadResourceAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadResourceAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		resolveList = ClassUtil.findClassesByAnnotationClass(Bean.class, "com.meizu");
		for (Class<?> clazz : resolveList) {
			ReloadableResource reloadableResource = clazz.getAnnotation(ReloadableResource.class);
			if(reloadableResource == null || StringUtil.isBlank(reloadableResource.value())) {
				continue;
			}
			LOGGER.info("配置实体注入 初始化:{}",clazz.getName());
			String reloadableResourceValue = reloadableResource.value();
			String prefix = reloadableResource.prefix();
			PropertieUtil propertieUtils = new PropertieUtil(reloadableResourceValue);
			Object beanObj = propertieUtils.toClass(clazz, prefix);
   			BeanFactory.addBean(beanObj);
		}
	}
}
