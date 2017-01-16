package com.meizu.simplify.plugin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;

/**
 * <p><b>Title:</b><i>插件自动加载解析处理器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年1月16日 下午2:34:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年1月16日 下午2:34:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(InitTypeEnum.PLUGIN)
public class PluginAnnotationResolver implements IAnnotationResolver<Class<?>> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginAnnotationResolver.class);
	
	@Override
	public void resolve(List<Class<?>> resolveList) {
		BeanContainer container = BeanFactory.getBeanContainer();
		Map<String, Object> mapContainer = container.getMapContainer();
		Collection<Object> containerCollection = mapContainer.values();
		LOGGER.info("test"+containerCollection.size());
	}
}
