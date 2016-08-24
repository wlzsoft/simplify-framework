package com.meizu.simplify.mvc.invoke;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;

/**
  * <p><b>Title:</b><i>动态表单对象选择器</i></p>
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
public class ModelSelector implements IModelSelector{
	
	@Override
	public <T> T setRequestModel(HttpServletRequest request, Class<T> modelClass)  {
		T model = null;
		try {
			model = modelClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
		Method[] modelMethodArr = modelClass.getMethods();
		for (int i = 0; i < modelMethodArr.length; i++) {
			Method method = modelMethodArr[i];
			String methodName = method.getName();
			Object value = setModelPropertie(request, method, methodName);
			if(value == null) {
				continue;
			}
			try {
				method.invoke(model, new Object[] { value });
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new UncheckedException("测试模式>>表单注入:参数无效[set方法->{"+modelClass.getName()+":"+methodName+"}的参数类型是{"+method.getParameterTypes()[0].getName()+"}]。但是注入的值是["+value+"],并且值类型是["+value.getClass()+"]");
			} catch (IllegalAccessException | InvocationTargetException e) {
//				e.printStackTrace();
				throw new UncheckedException(e);
			}
		}
		return model;
	}
}
