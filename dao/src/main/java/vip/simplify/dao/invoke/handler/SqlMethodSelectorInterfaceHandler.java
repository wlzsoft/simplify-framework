package vip.simplify.dao.invoke.handler;

import java.util.List;

import vip.simplify.dao.invoke.ISqlMethodSelector;
import vip.simplify.dao.invoke.SqlMethodSelector;
import vip.simplify.exception.StartupErrorException;
import vip.simplify.ioc.IInterfaceHandler;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.HandleInterface;
import vip.simplify.ioc.resolver.BeanAnnotationResolver;
import vip.simplify.utils.ClassUtil;

/**
  * <p><b>Title:</b><i>模版bean多实现类选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 下午3:02:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 下午3:02:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@HandleInterface(ISqlMethodSelector.class)
public class SqlMethodSelectorInterfaceHandler implements IInterfaceHandler {
	
	@Override
	public Class<?> handle() {
		List<Class<?>> methodSelectorList = ClassUtil.findClassesByInterfaces(ISqlMethodSelector.class, BeanAnnotationResolver.getClasspaths());
		for (Class<?> methodSelectorClazz : methodSelectorList) {
			if(SqlMethodSelector.class != methodSelectorClazz) {
				return methodSelectorClazz;
			}
		}
		throw new StartupErrorException("请检查ISqlMethodSelector是否有非默认实现类");
	}
}
