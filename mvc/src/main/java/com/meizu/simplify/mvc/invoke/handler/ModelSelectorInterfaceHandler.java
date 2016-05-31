package com.meizu.simplify.mvc.invoke.handler;

import java.util.List;

import com.meizu.simplify.Constants;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.HandleInterface;
import com.meizu.simplify.mvc.invoke.IModelSelector;
import com.meizu.simplify.utils.ClassUtil;

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
@HandleInterface(IModelSelector.class)
public class ModelSelectorInterfaceHandler implements IInterfaceHandler{
	
	@Override
	public Class<?> handle() {
		List<Class<?>> methodSelectorList = ClassUtil.findClassesByInterfaces(IModelSelector.class, Constants.packagePrefix);
		for (Class<?> methodSelectorClazz : methodSelectorList) {
			if(IModelSelector.class != methodSelectorClazz) {
				return methodSelectorClazz;
			}
		}
		throw new StartupErrorException("请检查IMethodSelector是否有非默认实现类");
	}
}
