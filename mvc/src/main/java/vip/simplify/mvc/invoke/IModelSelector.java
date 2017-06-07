package vip.simplify.mvc.invoke;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import vip.simplify.ioc.annotation.DefaultBean;
import vip.simplify.mvc.controller.AnalysisRequestControllerModel;
import vip.simplify.mvc.model.ModelSkip;
import vip.simplify.utils.DataUtil;
import vip.simplify.utils.ReflectionGenericUtil;
import vip.simplify.utils.StringUtil;

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
	public <T> T setRequestModel(HttpServletRequest request, Class<T> modelClass, String preParamName);
	default Object setModelPropertie(HttpServletRequest request, Method method, String methodName, String preParamName) {
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
		} else if (type.equals(List.class)) {
			//1.获取set方法的泛型参数类型
			Class<?> clazz = ReflectionGenericUtil.getGenricTypeParam(method, 0, 0);
			//2.获取待调用的set方法名称，这里是的set方法的参数是List类型
			String paramName = methodName.substring(3, methodName.length());
			paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
			List<Object> list = null;
			//3.提供两种方式集合注入处理
			//4.第一种：没有提供集合size值的表单项，格式为 {paramName}.size
			String sizeParam = request.getParameter(paramName+".size");
			if (StringUtil.isBlank(sizeParam)) {
				Enumeration<String> paramEnumeration = request.getParameterNames();
				int size = 0;
				while (paramEnumeration.hasMoreElements()) {
					String paramKey = paramEnumeration.nextElement();
					if(paramKey.startsWith(paramName)) {
						String c = paramKey.substring(paramName.length()+1,paramKey.indexOf("]"));
						Integer index = Integer.valueOf(c);
						if (index > size) {
							size = index;
						}
					}
				}
				size ++;
				list = new ArrayList<Object>(size);
				for (int i = 0; i < size; i++) {
					list.add(setRequestModel(request, clazz,paramName+"["+i+"]."));
				}
			} else {//5.第二种：提供了size值的处理方式,这种方式可以指定List的容量大小
				Integer size = DataUtil.parseInt(sizeParam,0);
				list = new ArrayList<Object>(size);
				for (int i = 0; i < size; i++) {
					list.add(setRequestModel(request, clazz,paramName+"["+i+"]."));
				}
			}
			value = list;
		} else if (!AnalysisRequestControllerModel.isBaseType(type)) {
			value = setRequestModel(request, type, null);
		} else {
			String paramName = methodName.substring(3, methodName.length());
			paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);
			String paramValue = request.getParameter((preParamName == null?"":preParamName)+paramName);
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
