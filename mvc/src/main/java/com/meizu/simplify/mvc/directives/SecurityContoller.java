package com.meizu.simplify.mvc.directives;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.encrypt.Md5Util;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.mvc.annotation.AjaxAccess;
import com.meizu.simplify.mvc.annotation.AjaxAccess.Methods;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.controller.IForward;
import com.meizu.simplify.mvc.directives.Model.ModelSet;
import com.meizu.simplify.mvc.directives.Model.Passme;
import com.meizu.simplify.mvc.directives.Model.StringFilter;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.ReflectionGenericUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>安全控制器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:24:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:24:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class SecurityContoller<T extends Model> {
	protected WebCache cacheSet = null; // 静态规则设置
	protected String staticName; // 静态标识名字
//	protected static final String X_REQUESTED_WITH = "x-requested-with";
	
	public void init() {}
	
	/**
	 * 
	 * 方法用途: 拦截处理所有请求<br>
	 * 操作步骤: TODO<br>
	 * @param req
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void process(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		HttpServletRequest request = req;
//		HttpServletRequest request = new HttpServletRequestWrapper(req){
//			
//			private String encode(String param){
//				try {
//					
//					String enc = StringUtil.parseString(super.getParameter("ec"), MvcInit.charSet); 
//					if (enc.equalsIgnoreCase(super.getCharacterEncoding())) {
//						return param;
//					} else {
//						return new String(param.getBytes(MvcInit.webcharSet), enc.toUpperCase());
//					}
//				} catch ( Exception e ) {
//					e.printStackTrace();
//				}
//				return param;
//			}
//			
//			@Override
//			public String getParameter(String name) {
//				return encode(super.getParameter(name)); 
//			}
//			
//			@Override
//			public String[] getParameterValues(String parameter){
//				String[] results = super.getParameterValues(parameter);
//				if (results == null)return null;
//				int count = results.length;
//				String[] trimResults = new String[count];
//				for (int i = 0; i < count; i++) {
//				  trimResults[i] = encode(results[i]);
//				}
//				return trimResults; 
//			}
//		};
		T model = setRequestModel(request);
		this.staticName = Md5Util.md5(request.getServerName() + request.getRequestURI() + StringUtil.trim(request.getQueryString())) + ".lv";
		
		if (checkPermission(request, response, model)) {
			IForward AF = execute(request, response, model);
			if (AF != null) {
				request.setAttribute("formData", model);
				AF.doAction(request, response, cacheSet, staticName);
			}
		}
		// 逻辑递交执行完成, 关闭session
		destroy(request, response, model);
		
	}

	/**
	 * controller 注销相关处理
	 * 
	 * @param request
	 * @param response
	 * @param t
	 */
	public void destroy(HttpServletRequest request, HttpServletResponse response, T t){
//			DAO.closeSession(0);
//			LOGGER.debug("dao Session[0]  closed.");
	}
	
	/**
	 * 
	 * 方法用途: 安全权限过程检查<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param t
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T t) throws ServletException, IOException {
		return true;
	}

	/**
	 * 
	 * 方法用途: 执行逻辑<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @param response
	 * @param t
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public IForward execute(HttpServletRequest request, HttpServletResponse response, T t) throws ServletException, IOException {
		if (t.getCmd() != null && t.getCmd().length() > 0) {
			String doCmd = t.getCmd();
			Method doMethod = null;
			try {
				Method[] methods = this.getClass().getMethods();
				for ( Method m : methods ) {
					if (doCmd.equals(m.getName())) {
						if (doMethod == null) {
							doMethod = m;
							break;
						} else {
							throw new IllegalArgumentException();
						}
					}
				}
				if (doMethod == null) { 
					throw new IllegalArgumentException("The method named, " + doCmd + ", is not specified by " + this.getClass()); 
				}
				if (doMethod.getParameterTypes().length < 3) { //考虑model问题，后续可以做更灵活调整
					throw new IllegalArgumentException("类:["+this.getClass()+"] 的方法 :[" + doCmd + "]的参数的长度不能小于3" ); 
				}

				// 设置参数值
				Object[] parameValue = new Object[doMethod.getParameterTypes().length];
				parameValue[0] = request;
				parameValue[1] = response;
				parameValue[2] = t;
				for ( int i = 3; i < doMethod.getParameterTypes().length; i++ ) {
					for ( int j = 0; j < doMethod.getParameterAnnotations()[i].length; j++ ) {
						if (doMethod.getParameterAnnotations()[i][j].annotationType() == RequestParam.class) {
							parameValue[i] = null;
							RequestParam requestParam = ((RequestParam) doMethod.getParameterAnnotations()[i][j]);
							String param = requestParam.param();
							String name = requestParam.name();
							String defaultValue = ((RequestParam) doMethod.getParameterAnnotations()[i][j]).defaultValue();
							defaultValue = "null".equals(defaultValue) ? null : defaultValue;
							Object value = null;
							
							//RequestParam的param有两个作用，一个是参数索引值，一个是参数名称，后续要拆分，不要一个参数两个含义，增加一个name属性就可以
							if (!StringUtil.isEmpty(request.getParameter(name))) {
								value = request.getParameter(name);
							} else if (ObjectUtil.isInt(param)) {
								int index = Integer.valueOf(param);
								if(t.getParams() != null && t.getParams().length > 0 && index < t.getParams().length) {
									value = t.getParams()[index];
								}
							} else {
								value = defaultValue;
							}

							if (value == null) {
								break;
							}

							// 将值进行格式化后注入
							parameValue[i] = DataUtil.convertType(doMethod.getParameterTypes()[i], value.toString());
						}
					}
				}
				
				// 检查Ajax跨域配置
				if (doMethod.isAnnotationPresent(AjaxAccess.class)) {
					AjaxAccess ajaxAccess = doMethod.getAnnotation(AjaxAccess.class);
					if (ajaxAccess != null) {
						if (!StringUtil.isEmpty(ajaxAccess.allowOrigin())) {
							response.addHeader("Access-Control-Allow-Origin", ajaxAccess.allowOrigin());
						}
						if (!StringUtil.isEmpty(ajaxAccess.allowHeaders())) {
							response.addHeader("Access-Control-Allow-Headers", ajaxAccess.allowHeaders());
						}
						if (ajaxAccess.allowMethods() != null) {
							StringBuffer sb_methods = new StringBuffer();
							for (Methods method : ajaxAccess.allowMethods()) {
								sb_methods.append(method.name()).append(",");
							}
							if (sb_methods.length() > 0) {
								sb_methods.delete(sb_methods.length() - 1, sb_methods.length());
							}
							response.addHeader("Access-Control-Allow-Methods", sb_methods.toString());
						}
						response.addHeader("Access-Control-Max-Age", String.valueOf(ajaxAccess.maxAge()));
					}
				}

				// 检查静态规则配置
				if (doMethod.isAnnotationPresent(WebCache.class)) {
					this.cacheSet = (WebCache) doMethod.getAnnotation(WebCache.class);
					Cache cache = CacheBase.getCache(cacheSet);
					if(cache != null){
						String cacheContent = cache.readCache(cacheSet, staticName,response);
						if(cacheContent != null){
							response.setCharacterEncoding(MvcInit.charSet);
							response.setContentType("text/html; charset=" + MvcInit.charSet);
							response.getWriter().print(cacheContent);
							System.out.println("UrlCache -> read Cache.");
							return null;
						}
					}
				}
				return (IForward) doMethod.invoke(this, parameValue);
			} catch ( Exception e ) {
				// response.sendError(404); // 错误的request请求.
				e.printStackTrace();
			}
		}
		return null;
	}

	


	/**
	 * 
	 * 方法用途: 获取表单数据<br>
	 * 操作步骤: TODO<br>
	 * @param request
	 * @return
	 */
	private T setRequestModel(HttpServletRequest request) {
		try {
			Class<T> entityClass = ReflectionGenericUtil.getSuperClassGenricType(getClass(),0);
			T model = entityClass.newInstance();
			for ( Method method : entityClass.getMethods() ) {
				if (method != null && method.getName().indexOf("set") == 0) {
					int parLength = method.getParameterTypes().length;
					if (parLength != 1) continue;
					Class<?> type = method.getParameterTypes()[0];
					String parName = method.getName().substring(3, method.getName().length());
					String par = request.getParameter(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));

					// 检查annotation 设置
					ModelSet mset = null;
					if (method.isAnnotationPresent(ModelSet.class)) {
						mset = (ModelSet) method.getAnnotation(ModelSet.class);
						if(mset.scope() != null){
							if(ModelSet.Scope.session == mset.scope()){
								
							} else if(ModelSet.Scope.cookie == mset.scope()){
								
							} else if(ModelSet.Scope.application == mset.scope()){
								
							}
						}
						if(mset.charset() != null && mset.charset().length() > 0) par = StringUtil.coding(par, mset.charset());
					}
					
					StringFilter stringFilter = null;
					if (method.isAnnotationPresent(StringFilter.class)) {
						stringFilter = (StringFilter) method.getAnnotation(StringFilter.class);
						if(stringFilter.filters() != null){
							for(StringFilter.Filter filter : stringFilter.filters()){
								if(StringFilter.Filter.Html == filter){
									par = StringUtil.removeHtmlLabel(par);
								} else if(StringFilter.Filter.Script == filter){
									par = StringUtil.removeScript(par);
								} else if(StringFilter.Filter.Style == filter){
									par = StringUtil.removeStyle(par);
								} else if(StringFilter.Filter.iframe == filter){
									par = StringUtil.removeIframe(par);
								} else if(StringFilter.Filter.trim == filter){
									par = StringUtil.removeHtmlSpace(par);
								}
							}
						}
					}

					// 是否滤过
					if (method.isAnnotationPresent(Passme.class)) continue;

					// 取消兼容方式
					// par = par == null ? request.getParameter(parName) : par;
					if (par != null) {
						Method method2 = entityClass.getMethod(method.getName(), new Class[] { type });
						// 将值进行格式化后注入
						if (type.isArray()) {
							String[] pars = request.getParameterValues(Character.toLowerCase(parName.charAt(0)) + parName.substring(1));
							if (pars != null && pars.length == 1) pars = pars[0].split(",");
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
}
