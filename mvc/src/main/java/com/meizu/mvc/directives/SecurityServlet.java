package com.meizu.mvc.directives;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.meizu.mvc.MvcInit;
import com.meizu.mvc.annotation.AjaxAccess;
import com.meizu.mvc.annotation.AjaxAccess.Methods;
import com.meizu.mvc.annotation.RequestParam;
import com.meizu.mvc.controller.IForward;
import com.meizu.mvc.directives.Model.ModelSet;
import com.meizu.mvc.directives.Model.Passme;
import com.meizu.mvc.directives.Model.StringFilter;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.Md5Util;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.webcache.annotation.CacheSet;
import com.meizu.webcache.web.Cache;
import com.meizu.webcache.web.CacheBase;


/**
 * 验证Servlet
 * 
 */
public class SecurityServlet<T extends Model> extends HttpServlet {
	private static final long serialVersionUID = 8160874454429513848L;
	protected CacheSet cacheSet = null; // 静态规则设置
	protected String staticName; // 静态标识名字

	public void destroy() {
		super.destroy();
	}

	public SecurityServlet() {}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * 递交业务处理逻辑
	 */
	public void doPost(HttpServletRequest request2, HttpServletResponse response) throws ServletException, IOException {
		HttpServletRequest request = new HttpServletRequestWrapper(request2){
			
			private String encode(String param){
				try {
					/*System.out.println(param);
					
					System.out.println(new String(param.getBytes("UTF-8"), "UTF-8"));
					System.out.println(new String(param.getBytes("GBK"), "UTF-8"));
					System.out.println(new String(param.getBytes("ISO-8859-1"), "UTF-8"));
					
					System.out.println(new String(param.getBytes("UTF-8"), "GBK"));
					System.out.println(new String(param.getBytes("GBK"), "GBK"));
					System.out.println(new String(param.getBytes("ISO-8859-1"), "GBK"));
					
					System.out.println(new String(param.getBytes("UTF-8"), "ISO-8859-1"));
					System.out.println(new String(param.getBytes("GBK"), "ISO-8859-1"));
					System.out.println(new String(param.getBytes("ISO-8859-1"), "ISO-8859-1"));*/
					
					String enc = StringUtil.parseString(super.getParameter("ec"), MvcInit.charSet); 
					if (enc.equalsIgnoreCase(super.getCharacterEncoding())) {
						return param;
					} else {
						return new String(param.getBytes(MvcInit.webcharSet), enc.toUpperCase());
					}
				} catch ( Exception e ) {}
				return param;
			}
			
			@Override
			public String getParameter(String name) {
				return encode(super.getParameter(name)); 
			}
			
			@Override
			public String[] getParameterValues(String parameter){
				String[] results = super.getParameterValues(parameter);
				if (results == null)return null;
				int count = results.length;
				String[] trimResults = new String[count];
				for (int i = 0; i < count; i++) {
				  trimResults[i] = encode(results[i]);
				}
				return trimResults; 
			}
		};
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
		actionDestroy(request, response, model);
		
	}

	/**
	 * Action释放
	 * 
	 * @param request
	 * @param response
	 * @param t
	 */
	public void actionDestroy(HttpServletRequest request, HttpServletResponse response, T t){
		if (MvcInit.hibernate) {    
//			MultiHibernateDAO.closeSession(0);
//			PrintHelper.getPrint().debug("Hibernate Session[0]  closed.");
		}
	}
	
	/**
	 * 安全权限过程检查
	 * 
	 * @param request
	 * @param response
	 * @param t
	 * @return boolean
	 * @throws ServletException
	 * @throws IOException
	 */
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T t) throws ServletException, IOException {
		return true;
	}

	/**
	 * 执行逻辑
	 * 
	 * @param request
	 * @param response
	 * @param t
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public IForward execute(HttpServletRequest request, HttpServletResponse response, T t) throws ServletException, IOException {
		if (t.getCmd() != null && t.getCmd().length() > 0) {
			String doCmd = StringUtil.format("do{0}", Character.toUpperCase(t.getCmd().charAt(0)) + t.getCmd().substring(1));
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
				if (doMethod == null) { throw new IllegalArgumentException("The method named, " + doCmd + ", is not specified by " + this.getClass()); }
				if (doMethod.getParameterTypes().length < 3) { throw new IllegalArgumentException("The method named, " + doCmd + ", parameter length is not enough by " + this.getClass()); }

				// 设置参数值
				Object[] parameValue = new Object[doMethod.getParameterTypes().length];
				parameValue[0] = request;
				parameValue[1] = response;
				parameValue[2] = t;
				for ( int i = 3; i < doMethod.getParameterTypes().length; i++ ) {
					for ( int j = 0; j < doMethod.getParameterAnnotations()[i].length; j++ ) {
						if (doMethod.getParameterAnnotations()[i][j].annotationType() == RequestParam.class) {
							parameValue[i] = null;
							String param = ((RequestParam) doMethod.getParameterAnnotations()[i][j]).param();
							String defaultValue = ((RequestParam) doMethod.getParameterAnnotations()[i][j]).defaultValue();
							defaultValue = "null".equals(defaultValue) ? null : defaultValue;
							Object value = null;
							if (!StringUtil.isEmpty(request.getParameter(param))) {
								value = request.getParameter(param);
							} else if (ObjectUtil.isInt(param)) {
								int index = Integer.valueOf(param);
								value = index < t.getPrarms().length ? t.getPrarms()[index] : null;
							} else value = defaultValue;

							if (value == null) break;

							// 将值进行格式化后注入
							/*parameValue[i] = ClassHelper.formatof(doMethod.getParameterTypes()[i], value.toString());*/
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
							if (sb_methods.length() > 0) sb_methods.delete(sb_methods.length() - 1, sb_methods.length());
							response.addHeader("Access-Control-Allow-Methods", sb_methods.toString());
						}
						response.addHeader("Access-Control-Max-Age", String.valueOf(ajaxAccess.maxAge()));
					}
				}

				// 检查静态规则配置
				if (doMethod.isAnnotationPresent(CacheSet.class)) {
					this.cacheSet = (CacheSet) doMethod.getAnnotation(CacheSet.class);
					Cache cache = CacheBase.getCache(cacheSet);
					if(cache != null){
						String cacheContent = cache.readCache(cacheSet, staticName,response);
						if(cacheContent != null){
							response.setCharacterEncoding(MvcInit.charSet);
							response.setContentType("text/html; charset=" + MvcInit.charSet);
							response.getWriter().print(cacheContent);
//							PrintHelper.getPrint().debug("UrlCache -> read Cache.");
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

	public void init() throws ServletException {}

	@SuppressWarnings("unchecked")
	private Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/**
	 * 获取表单数据
	 * 
	 * @param request
	 * @return
	 */
	private T setRequestModel(HttpServletRequest request) {
		try {
			Class<T> entityClass = getEntityClass();
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
							/*method2.invoke(model, new Object[] { ClassHelper.formatof(type, pars) });*/
						} else {
							/*method2.invoke(model, new Object[] { ClassHelper.formatof(type, par) });*/
						}
					}
				}
			}
			String[] params = (String[]) request.getAttribute("params");
			String cmd = (String) request.getAttribute("cmd");
			if (params != null && cmd != null) {
				Method method = entityClass.getMethod("setPrarms", new Class[] { String[].class });
				method.invoke(model, new Object[] { params });

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
