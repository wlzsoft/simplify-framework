package vip.simplify.mvc.resolver;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.ioc.resolver.IAnnotationResolver;
import vip.simplify.template.annotation.TemplateType;
import vip.simplify.util.CacheManager;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.view.IPageTemplate;

/**
  * <p><b>Title:</b><i>模版解析器配置解析</i></p>
 * <p>Desc: TODO </p>
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
@Init(InitTypeEnum.TEMPLATE)
public class TemplateAnnotationResolver implements IAnnotationResolver<Class<?>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TemplateAnnotationResolver.class);
	public static Map<String, IPageTemplate> templateMap = new ConcurrentHashMap<>();
	public TemplateAnnotationResolver() {
		CacheManager.addCache("templateMap", templateMap);
	}
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		for (Object beanObj : containerCollection) {
			Class<?> beanClass = beanObj.getClass();
			TemplateType templateType = beanClass.getAnnotation(TemplateType.class);
			if(templateType == null) {
				continue;
			}
			Class<?>[] interfaces = beanClass.getInterfaces();
			Class<?> itClazz = CollectionUtil.getItem(interfaces, IPageTemplate.class, (i,t) -> i == t);
			if(itClazz == null) {
				continue;
			}
			
			String templateTypeTag = templateType.value();
			templateMap.put(templateTypeTag , (IPageTemplate)beanObj);
			LOGGER.info("已启用模版引擎:["+templateTypeTag+"],类型：["+beanClass.getName()+"]");
		}
	}
}

