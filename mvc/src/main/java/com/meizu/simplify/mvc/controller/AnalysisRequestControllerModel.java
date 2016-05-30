package com.meizu.simplify.mvc.controller;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.mvc.model.BaseModel;
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
	 * 操作步骤: 目前不限制只能注入Model类型的实体<br>
	 * @param request
	 * @param modelClass
	 * @return
	 */
	public static <T> T setRequestModel(HttpServletRequest request,Class<T>  modelClass,String cmd,String[] urlparams) {
		try {
			T model = modelClass.newInstance();
			Method[] modelMethodArr = modelClass.getMethods();
			for ( int i=0; i < modelMethodArr.length;i++ ) {
				Method method = modelMethodArr[i];
				String methodName = method.getName();
				if (methodName.indexOf("set") < 0) {
					continue;
				}
				Class<?>[] parameterTypes = method.getParameterTypes();
				if (parameterTypes.length != 1) {
					continue;
				}
				Class<?> type = parameterTypes[0];//pojo类的的set方法只有一个参数，所以这里写死读取第一个参数
				String paramName = methodName.substring(3, methodName.length());
				paramName = Character.toLowerCase(paramName.charAt(0)) + paramName.substring(1);

				// 将值进行格式化后注入
				if (type.isArray()) {//TODO 针对set属性值为数组类型的处理，需要精细测试，并确认是否有必要，是否目前只为 params 而使用，内嵌的一个属性,通过setAttribute设置，是否也可以从页面传递过来
					String[] paramValueArr = request.getParameterValues(paramName);
					if (paramValueArr != null && paramValueArr.length == 1) {
						paramValueArr = paramValueArr[0].split(",");
					}
					method.invoke(model, new Object[] { DataUtil.convertType(type, paramValueArr) });
				} else if(!isBaseType(type)){
//					System.out.println(type);
					Object pojo = setRequestModel(request,type,cmd,urlparams);
					method.invoke(model, new Object[] { DataUtil.convertType(type, pojo) });
				} else {
					String paramValue = request.getParameter(paramName);
					if (paramValue == null) {
						continue;
					}
					paramValue = analysisModelScope(method, paramValue);
					paramValue = analysisModelCharsFilter(method, paramValue);
					// 是否滤过
					if (method.isAnnotationPresent(ModelSkip.class)) {
						continue;
					}
					method.invoke(model, new Object[] { DataUtil.convertType(type, paramValue) });
				}
			}
			if(modelClass.getSuperclass() != BaseModel.class&&modelClass.getSuperclass() != Model.class) {
				return model;
			}
			//url参数：针对rest风格url参数的设置
			Method method = modelClass.getMethod("setParams", new Class[] { String[].class });
			if(method != null) {
				method.invoke(model, new Object[] { urlparams });
			}
			//指令：对调用的controller方法的名称的做了指令，用于区分并处理方法见的差异逻辑
			method = modelClass.getMethod("setCmd", new Class[] { String.class });
			if (method != null) {
				if(cmd != null) {
					method.invoke(model, new Object[] { cmd });
				}
			}
			return model;
		} catch ( Exception e ) {
			e.printStackTrace();
			throw new UncheckedException(e);
		}
	}
	
	/**
	 * 方法用途: 判断是否基本数据类型，包含基本数据类型的包装类型<br>
	 * 操作步骤: TODO<br>
	 * @param clz
	 * @return
	 */
	public static boolean isBaseType(Class<?> clz) {
		if(clz==String.class||clz==Date.class) {
			return true;
		}
        try { 
        	boolean isPrimitive = ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();
        	return isPrimitive;
        } catch (Exception e) {
        	return false;
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
