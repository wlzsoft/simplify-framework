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
					Class<?> type = parameterTypes[0];
					String parName = method.getName().substring(3, method.getName().length());
					String par = request.getParameter(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));

					par = analysisModelScope(method, par);
					
					par = analysisModelCharsFilter(method, par);

					// 是否滤过
					if (method.isAnnotationPresent(ModelSkip.class)) {
						continue;
					}

					// 取消兼容方式
					// par = par == null ? request.getParameter(parName) : par;
					if (par != null) {
						Method method2 = entityClass.getMethod(method.getName(), new Class[] { type });
						// 将值进行格式化后注入
						if (type.isArray()) {
							String[] pars = request.getParameterValues(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
							if (pars != null && pars.length == 1) {
								pars = pars[0].split(",");
							}
							method2.invoke(model, new Object[] { DataUtil.convertType(type, pars) });
						} else {
							method2.invoke(model, new Object[] { DataUtil.convertType(type, par) });
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
	 * @param par
	 * @return
	 */
	private static String analysisModelCharsFilter(Method method, String par) {
		ModelCharsFilter stringFilter = null;
		if (method.isAnnotationPresent(ModelCharsFilter.class)) {
			stringFilter = (ModelCharsFilter) method.getAnnotation(ModelCharsFilter.class);
			if(stringFilter.filters() != null){
				for(ModelCharsFilter.Filter filter : stringFilter.filters()){
					if(ModelCharsFilter.Filter.Html == filter){
						par = StringUtil.removeHtmlLabel(par);
					} else if(ModelCharsFilter.Filter.Script == filter){
						par = StringUtil.removeScript(par);
					} else if(ModelCharsFilter.Filter.Style == filter){
						par = StringUtil.removeStyle(par);
					} else if(ModelCharsFilter.Filter.iframe == filter){
						par = StringUtil.removeIframe(par);
					} else if(ModelCharsFilter.Filter.trim == filter){
						par = StringUtil.removeHtmlSpace(par);
					}
				}
			}
		}
		return par;
	}

	/**
	 * 
	 * 方法用途: 解析表单属性作用域的设置<br>
	 * 操作步骤: TODO<br>
	 * @param method
	 * @param par
	 * @return
	 */
	private static String analysisModelScope(Method method, String par) {
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
				par = StringUtil.coding(par, mset.charset());
			}
		}
		return par;
	}
}
