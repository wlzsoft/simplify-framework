package vip.simplify.rpc.hook;

import vip.simplify.Constants;
import vip.simplify.ioc.BeanEntity;
import vip.simplify.ioc.annotation.BeanConfig;
import vip.simplify.ioc.annotation.BeanHook;
import vip.simplify.ioc.hook.IBeanHook;
import vip.simplify.ioc.resolver.BeanConfigAnnotationResolver;
import vip.simplify.rpc.annotations.ClientBeanConfig;
import vip.simplify.rpc.resolver.ClientBeanAnnotationResolver;
import vip.simplify.utils.ClassUtil;
import vip.simplify.utils.CollectionUtil;
import vip.simplify.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * <p>clientBean之BeanConfig注解解析</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@BeanHook(ClientBeanConfig.class)
public class ClientBeanConfigPrototypeHook implements IBeanHook{
	
	@Override
	public BeanEntity<?> hook(Class<?> clazz) {
		Map<Class<?>,BeanConfig> map = BeanConfigAnnotationResolver.beanConfigClassMap;
		if (CollectionUtil.isEmpty(map)) {
			return null;
		}
		BeanConfig beanConfig = map.get(clazz);
		if (beanConfig == null) {
			return null;
		}
		List<Class<?>> allIpmlClass=ClassUtil.findClassesByParentClass(clazz, Constants.packagePrefix);
		if (CollectionUtil.isNotEmpty(allIpmlClass)) {
			return null;
		}
		//TODO 下面固定的参数值，需要重新处理成可配置，并和原来的ClientBean集成
		return ClientBeanAnnotationResolver.addRemoteBean(clazz,"1.0.0",false,"");
	}
	
}
