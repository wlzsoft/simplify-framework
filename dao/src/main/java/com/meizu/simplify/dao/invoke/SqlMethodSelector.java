package com.meizu.simplify.dao.invoke;

import java.time.LocalDate;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.dao.orm.MapperTypeUtil;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.DateUtil;
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
	
	@Config("system.useNewDate")
	private boolean useNewDate;
	
	@Override
	public  Object invoke(Object t,String columnName) {
		return ReflectionUtil.invokeGetterMethod(t, columnName);
	}

	@Override
	public void invokeSet(Object t, String columnName,Object val) {
		Class<?> valClazz = MapperTypeUtil.mapperOrmType(val,useNewDate);
	    if(val.getClass() == java.sql.Date.class) {
	    	if(useNewDate) {
				val = LocalDate.parse(val.toString());
			} else {
				val = DateUtil.parse(val.toString());
			}
	    }
		ReflectionUtil.invokeSetterMethod(t, columnName, val,valClazz);
	}
	
}
