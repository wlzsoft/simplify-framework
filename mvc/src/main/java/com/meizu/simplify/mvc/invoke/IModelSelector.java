package com.meizu.simplify.mvc.invoke;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.mvc.controller.AnalysisRequestControllerModel;
import com.meizu.simplify.mvc.model.ModelSkip;
import com.meizu.simplify.utils.DataUtil;

/**
  * <p><b>Title:</b><i>controller表单对象选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:37:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:37:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface IModelSelector {

	/**
	 * 方法用途: controller表单对象选择处理-设置表单数据<br>
	 * 操作步骤: 目前限制只能注入Model类型的实体<br>
	 * @param request
	 * @param modelClass
	 * @return
	 */
	public <T> T setRequestModel(HttpServletRequest request, Class<T> modelClass);
	default Object setModelPropertie(HttpServletRequest request, Method method, String methodName) {
		if (methodName.indexOf("set") < 0) {
			return null;
		}
		Class<?>[] parameterTypes = method.getParameterTypes();
		if (parameterTypes.length != 1) {
			return null;
		}
		Class<?> type = parameterTypes[0];// pojo类的的set方法只有一个参数，所以这里写死读取第一个参数
		Object value = null;
		// 将值进行格式化后注入
		if (type.isArray()) {// 目前只为 params 而使用，内嵌的一个属性
			return null;
		} else if (!AnalysisRequestControllerModel.isBaseType(type)) {
			value = setRequestModel(request, type);
		} else {
			String paramName = methodName.substring(3, methodName.length());
			paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
			String paramValue = request.getParameter(paramName);
			if (paramValue == null) {
				return null;
			}
			paramValue = AnalysisRequestControllerModel.analysisModelScope(method, paramValue);
			paramValue = AnalysisRequestControllerModel.analysisModelCharsFilter(method, paramValue);
			// 是否滤过
			if (method.isAnnotationPresent(ModelSkip.class)) {
				return null;
			}
			value = paramValue;
		}
		value = DataUtil.convertType(type, value,true);//是否需要删除,不保留这个逻辑 TODO
		return value;
	}
}
