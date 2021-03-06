package vip.simplify.ioc.resolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.simplify.ioc.BeanContainer;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.annotation.Init;
import vip.simplify.ioc.annotation.InitBean;
import vip.simplify.ioc.enums.InitTypeEnum;
import vip.simplify.utils.ClassUtil;

/**
  * <p><b>Title:</b><i>依赖注入解析器</i></p>
 * <p>Desc: 注意：可启用容器托管，只获取部分ioc注入功能</p>
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
@Init(InitTypeEnum.INIT_BEAN)
public final class InitBeanAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(InitBeanAnnotationResolver.class);
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
		List<Method> initMethodList = ClassUtil.findMethodByAnnotation(InitBean.class,beanClass);
		for (Method method : initMethodList) {
			String message = "bean["+beanClass.getName()+"]的初始化方法["+method.getName()+"]";
			try {
				method.invoke(beanObj);
				LOGGER.info("已调用"+message);
				break;
			} catch (IllegalAccessException  | InvocationTargetException e) {
				LOGGER.error(message + "异常.",e);
			} catch (IllegalArgumentException e) {
				LOGGER.error(message + "异常 ==>> "+method.getName()+"方法必须是无参的,方法签名是:\n@InitBean\npublic void "+method.getName()+"(){\n//init code\n}",e);
			}
		}
	}
	
}
