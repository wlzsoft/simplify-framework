package com.meizu.simplify.dao.orm;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.ReflectionUtil;

/**
  * <p><b>Title:</b><i>动态方法选择器</i></p>
 * <p>Desc: 基于反射机制实现，性能校差,用于debug模式，方便开发阶段测试</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:35:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:35:50</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class SqlMethodSelector implements ISqlMethodSelector{
	
	@Override
	public  Object invoke(Object t,String columnName) {
		return ReflectionUtil.invokeGetterMethod(t, columnName);
	}
}
