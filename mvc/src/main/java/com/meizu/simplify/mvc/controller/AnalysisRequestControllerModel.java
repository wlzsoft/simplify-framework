package com.meizu.simplify.mvc.controller;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.mvc.model.ModelCharsFilter;
import com.meizu.simplify.mvc.model.ModelScope;
import com.meizu.simplify.mvc.model.ModelSkip;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>解析http中的表单数据</i></p>
 * <p>Desc: TODO 待优化，使用了运行时反射</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月25日 下午6:01:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月25日 下午6:01:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class AnalysisRequestControllerModel {
	/**
	 * 
	 * 方法用途: 获取表单数据<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @return
	 */
	public static <T extends Model> T setRequestModel(HttpServletRequest request,Class<T>  entityClass) {
		try {
			T model = entityClass.newInstance();
			Method[] modelMethodArr = entityClass.getMethods();
			for ( int i=0; i < modelMethodArr.length;i++ ) {
				Method method = modelMethodArr[i];
				if (method != null && method.getName().indexOf("set") == 0) {
					Class<?>[] parameterTypes = method.getParameterTypes();
					if (parameterTypes.length != 1) {
						continue;
					}
					Class<?> type = parameterTypes[0];//pojo类的的set方法只有一个参数，所以这里写死读取第一个参数
					String paramName = method.getName().substring(3, method.getName().length());
					String paramValue = request.getParameter(Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1));

					paramValue = analysisModelScope(method, paramValue);
					
					paramValue = analysisModelCharsFilter(method, paramValue);

					// 是否滤过
					if (method.isAnnotationPresent(ModelSkip.class)) {
						continue;
					}

					if (paramValue != null) {
						// 将值进行格式化后注入
						if (type.isArray()) {
							String[] paramValueArr = request.getParameterValues(Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1));
							if (paramValueArr != null && paramValueArr.length == 1) {
								paramValueArr = paramValueArr[0].split(",");
							}
							method.invoke(model, new Object[] { DataUtil.convertType(type, paramValueArr) });
						} else {
							method.invoke(model, new Object[] { DataUtil.convertType(type, paramValue) });
						}
					}
				}
			}
			Method method = entityClass.getMethod("setParams", new Class[] { String[].class });
			Object paramObj = request.getAttribute("params");
			if(paramObj != null) {
				String[] params = (String[])paramObj;
				method.invoke(model, new Object[] { params });
			} else {
				method.invoke(model, new Object[] { null });
			}

			String cmd = (String) request.getAttribute("cmd");
			if (cmd != null) {
				method = entityClass.getMethod("setCmd", new Class[] { String.class });
				method.invoke(model, new Object[] { cmd });
			}
			return model;
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
	}

	/**
	 * 
	 * 方法用途: 解析表单属性值的非法字符过滤清除的设置<br>
	 * 操作步骤: TODO<br>
	 * @param method
	 * @param paramValue
	 * @return
	 */
	private static String analysisModelCharsFilter(Method method, String paramValue) {
		ModelCharsFilter stringFilter = null;
		if (method.isAnnotationPresent(ModelCharsFilter.class)) {
			stringFilter = (ModelCharsFilter) method.getAnnotation(ModelCharsFilter.class);
			if(stringFilter.filters() != null){
				for(ModelCharsFilter.Filter filter : stringFilter.filters()){
					if(ModelCharsFilter.Filter.Html == filter){
						paramValue = StringUtil.removeHtmlLabel(paramValue);
					} else if(ModelCharsFilter.Filter.Script == filter){
						paramValue = StringUtil.removeScript(paramValue);
					} else if(ModelCharsFilter.Filter.Style == filter){
						paramValue = StringUtil.removeStyle(paramValue);
					} else if(ModelCharsFilter.Filter.iframe == filter){
						paramValue = StringUtil.removeIframe(paramValue);
					} else if(ModelCharsFilter.Filter.trim == filter){
						paramValue = StringUtil.removeHtmlSpace(paramValue);
					}
				}
			}
		}
		return paramValue;
	}

	/**
	 * 
	 * 方法用途: 解析表单属性作用域的设置<br>
	 * 操作步骤: TODO<br>
	 * @param method
	 * @param paramValue
	 * @return
	 */
	private static String analysisModelScope(Method method, String paramValue) {
		ModelScope mset = null;
		if (method.isAnnotationPresent(ModelScope.class)) {
			mset = (ModelScope) method.getAnnotation(ModelScope.class);
			if(mset.scope() != null){
				if(ModelScope.Scope.session == mset.scope()){
					
				} else if(ModelScope.Scope.cookie == mset.scope()){
					
				} else if(ModelScope.Scope.application == mset.scope()){
					
				}
			}
			if(mset.charset() != null && mset.charset().length() > 0) {
				paramValue = StringUtil.coding(paramValue, mset.charset());
			}
		}
		return paramValue;
	}
}
