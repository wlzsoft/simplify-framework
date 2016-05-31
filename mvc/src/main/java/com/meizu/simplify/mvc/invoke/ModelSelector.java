package com.meizu.simplify.mvc.invoke;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.mvc.controller.AnalysisRequestControllerModel;
import com.meizu.simplify.mvc.model.ModelSkip;
import com.meizu.simplify.utils.DataUtil;

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
		try {
			T model = modelClass.newInstance();
			Method[] modelMethodArr = modelClass.getMethods();
			for (int i = 0; i < modelMethodArr.length; i++) {
				Method method = modelMethodArr[i];
				String methodName = method.getName();
				if (methodName.indexOf("set") < 0) {
					continue;
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) {
					continue;
				}
				Class<?> type = parameterTypes[0];// pojo类的的set方法只有一个参数，所以这里写死读取第一个参数
				Object value = null;
				// 将值进行格式化后注入
				if (type.isArray()) {// 目前只为 params 而使用，内嵌的一个属性
					continue;
				} else if (!AnalysisRequestControllerModel.isBaseType(type)) {
					value = setRequestModel(request, type);
				} else {
					String paramName = methodName.substring(3, methodName.length());
					paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
					String paramValue = request.getParameter(paramName);
					if (paramValue == null) {
						continue;
					}
					paramValue = AnalysisRequestControllerModel.analysisModelScope(method, paramValue);
					paramValue = AnalysisRequestControllerModel.analysisModelCharsFilter(method, paramValue);
					// 是否滤过
					if (method.isAnnotationPresent(ModelSkip.class)) {
						continue;
					}
					value = paramValue;
				}
				method.invoke(model, new Object[] { DataUtil.convertType(type, value) });
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
	}
}
