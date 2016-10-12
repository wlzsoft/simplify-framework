package com.meizu.simplify.dao.invoke;

import com.meizu.simplify.config.annotation.Config;
import com.meizu.simplify.dao.orm.IEnum;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.utils.MapperTypeUtil;
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
		Object obj = ReflectionUtil.invokeGetterMethod(t, columnName);
		if(obj == null) {
			return null;
		}
		//实体特殊类型处理start-1.该处理有微小的性能消耗，需要压测分析对框架的影响有多大。2.对类型推导的源码生成的影响多大，需要评估，并且修改类型推导功能 TODO
		//数组类型处理
		if(obj.getClass().isArray()) {
			Object[] objArr = ((Object[])obj);
			String v = "";
			for (Object object : objArr) {
				v += ","+String.valueOf(object);
			}
			if(v!=null&&!"".equals(v)) {
				v = v.substring(1);
			}
			return v;
		}
		//枚举类型处理
		Class<?>[] interfacesArr = obj.getClass().getInterfaces();
		if(interfacesArr != null && interfacesArr.length == 1 && interfacesArr[0] == IEnum.class) {
			return ((IEnum)obj).getValue();
		}
		//实体特殊类型处理end
		return obj;
	}

	/** 
	 * 1.增加了isSelfParamType参数后  对类型推导的源码生成的影响多大，需要评估，并且修改类型推导功能 TODO
	 * @see com.meizu.simplify.dao.invoke.ISqlMethodSelector#invokeSet(java.lang.Object, java.lang.String, java.lang.Object, boolean)
	 */
	@Override
	public void invokeSet(Object t, String columnName,Object val,boolean isSelfParamType) {
		Class<?> valClazz = MapperTypeUtil.mapperOrmType(val,useNewDate);
		val = MapperTypeUtil.convertOrmType(val, val.getClass(), useNewDate);
		if(isSelfParamType) {
			ReflectionUtil.invokeSetterMethod(t, columnName, val,true);
		} else {
			ReflectionUtil.invokeSetterMethod(t, columnName, val,valClazz);
		}
	}
	
}
