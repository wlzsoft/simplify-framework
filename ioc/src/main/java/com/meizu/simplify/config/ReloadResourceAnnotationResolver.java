package com.meizu.simplify.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.DymaicProperties;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
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
@Init(InitTypeEnum.RELOADRESOURCE)
public class ReloadResourceAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadResourceAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			ReloadableResource reloadableResource = beanClass.getAnnotation(ReloadableResource.class);
			if(reloadableResource == null || StringUtil.isBlank(reloadableResource.value())) {
				continue;
			}
			LOGGER.info("配置实体注入 初始化:{}",beanClass.getName());
			String reloadableResourceValue = reloadableResource.value();
			String prefix = reloadableResource.prefix();
			PropertieUtil propertieUtils = new PropertieUtil(reloadableResourceValue);
			propertieUtils.setConfigValue(beanObj, prefix);
			
			//配置文件动态读取设置
			Field[] fieldArr = beanClass.getDeclaredFields();
			for (Field field : fieldArr) {
				DymaicProperties dymaicProperties = field.getAnnotation(DymaicProperties.class);
				if(dymaicProperties != null) {
					ReflectionUtil.invokeSetterMethod(beanObj, field.getName(), propertieUtils);
				}
			}
		}
	}
}
