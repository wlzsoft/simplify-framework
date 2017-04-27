package vip.simplify.config;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.config.annotation.DymaicProperties;
import vip.simplify.config.annotation.ReloadableResource;
import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>配置实体注入解析器</i></p>
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
@Init(InitTypeEnum.RELOAD_RESOURCE)
public class ReloadResourceAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ReloadResourceAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Set<String> containerCollection = mapContainer.keySet();
		for (String beanName : containerCollection) {
			resolveBeanObj(beanName);
		}
	}

	@Override
	public void resolveBeanObj(String beanName) {
		Object beanObj = BeanFactory.getBean(beanName);
		Class<?> beanClass = beanObj.getClass();
		ReloadableResource reloadableResource = beanClass.getAnnotation(ReloadableResource.class);
		if(reloadableResource == null || StringUtil.isBlank(reloadableResource.value())) {
			return;
		}
		LOGGER.info("配置实体注入 初始化:{}",beanClass.getName());
		String reloadableResourceValue = reloadableResource.value();
		String prefix = reloadableResource.prefix();
		PropertieUtil propertieUtils = new PropertieUtil(reloadableResourceValue,false);
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
