package com.meizu.simplify.mvc.controller;

import java.lang.reflect.InvocationTargetException;
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
	

	public static <T> T setBaseModel(Class<T> modelClass, String cmd, String[] urlparams, T model) {
		if(modelClass.getSuperclass() != BaseModel.class&&modelClass.getSuperclass() != Model.class) {
			return model;
		}
		//url参数：针对rest风格url参数的设置
		Model modelTemp = (Model) model;
		if(urlparams != null) {
			modelTemp.setParams(urlparams);
		}
		//指令：对调用的controller方法的名称的做了指令，用于区分并处理方法见的差异逻辑
		if(cmd != null) {
			modelTemp.setCmd(cmd);
		}
		return model;
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
	public static String analysisModelCharsFilter(Method method, String paramValue) {
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
	public static String analysisModelScope(Method method, String paramValue) {
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
