package com.meizu.simplify.config;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.config.annotation.ReloadableResource;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;

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
@Init(4)
public class ConfigAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigAnnotationResolver.class);
	
	private Map<String,PropertieUtil> propertiesMap = new ConcurrentHashMap<>();
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
			LOGGER.info("配置属性单个注入 初始化:{}",beanClass.getName());
			String reloadableResourceValue = reloadableResource.value();
			String prefix = reloadableResource.prefix();
			PropertieUtil propertieUtil = new PropertieUtil(reloadableResourceValue);
			propertiesMap.put(reloadableResourceValue, propertieUtil);
		}
		
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			Field[] fieldArr = beanClass.getDeclaredFields();
			for (Field field : fieldArr) {
                if (field.isAnnotationPresent(Config.class)) {
                	Config config = field.getAnnotation(Config.class);
                	String configName = config.value();
                	if(ObjectUtil.isNull(configName)) {
                		configName = "";
                	}
                	Class<?> iocType = field.getType();
                	String message = "依赖注入配置文件属性初始化: "+field.getDeclaringClass().getTypeName()+"["+iocType.getTypeName()+":"+field.getName()+"]";
                	if(StringUtil.isNotBlank(configName)) {
                		Set<Entry<String, PropertieUtil>> propertiesSet = propertiesMap.entrySet();
                		for (Entry<String, PropertieUtil> entry : propertiesSet) {
                			String configPath = entry.getKey();
                			message+="==>>注入的配置文件"+configPath+"中的["+configName+"]配置项";
                			PropertieUtil propertiesUtil = entry.getValue();
                			Object configPropertieValue = propertiesUtil.get(configName);
                			if(configPropertieValue == null) {
                				continue;
                			}
                			try {
                				field.setAccessible(true);
                				field.set(beanObj, configPropertieValue);
                			} catch (IllegalArgumentException | IllegalAccessException e) {
                				// TODO Auto-generated catch block
                				e.printStackTrace();
                			}
                			break;
                		}
                	}
                	LOGGER.debug(message);
                }
			}
		}
		
	}
}
