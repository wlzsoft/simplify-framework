package vip.simplify.mvc.controller;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

import vip.simplify.mvc.model.BaseModel;
import vip.simplify.mvc.model.Model;
import vip.simplify.mvc.model.ModelCharsFilter;
import vip.simplify.mvc.model.ModelScope;
import vip.simplify.utils.StringUtil;

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
	

	public static <T> T setBaseModel(Class<T> modelClass, String[] urlparams, T model) {
		Class<?> modelSuperClass = modelClass.getSuperclass();
		Class<?> modelSuperSuperClass = modelSuperClass.getSuperclass();
		if( modelClass!=Model.class&&modelSuperClass!= BaseModel.class&&modelSuperClass != Model.class&&modelSuperSuperClass!= BaseModel.class&&modelSuperSuperClass != Model.class) {
			return model;
		}
		//url参数：针对rest风格url参数的设置
		Model modelTemp = (Model) model;
		if(urlparams != null) {
			modelTemp.setParams(urlparams);
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
		if(clz==String.class||clz==Date.class || clz==LocalDate.class || clz==LocalTime.class || clz==LocalDateTime.class) {
			return true;
		}
        try { 

			boolean isPrimitive = clz.isPrimitive();//判断是否基本类型 int long double等
			if(isPrimitive) {
				return true;
			}
			boolean isWrapperPrimitive = ((Class<?>) clz.getField("TYPE").get(null)).isPrimitive();//判断是否基本类型包装类型 Integer ,Long, Double
        	return isWrapperPrimitive;
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
