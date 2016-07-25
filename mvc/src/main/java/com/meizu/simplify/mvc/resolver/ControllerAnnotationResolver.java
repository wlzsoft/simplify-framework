package com.meizu.simplify.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.Constants;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.mvc.AnnotationResolverCallback;
import com.meizu.simplify.mvc.annotation.AjaxAccess;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.controller.IBaseController;
import com.meizu.simplify.mvc.dto.AnnotationListInfo;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.util.CacheManager;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.CacheBase;

/**
  * <p><b>Title:</b><i>mvc模块初始化解析</i></p>
 * <p>Desc: 1.初始化mvc数据参数
 *          2.mvc请求地址解析器</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@Init(InitTypeEnum.CONTROL)
public class ControllerAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAnnotationResolver.class);
	@Resource
	private PropertiesConfig config;
	/**
	 * <requestMap地址,controller实例>
	 */
	public static Map<String, ControllerAnnotationInfo<IBaseController<?>>> controllerMap = new ConcurrentHashMap<>();
	/**
	 * <requestMap地址,controller实例>
	 */
	public static Map<String, ControllerAnnotationInfo<IBaseController<?>>> controllerRegularExpressionsList = new ConcurrentSkipListMap<>();
	/**
	 * <包名.类名.方法名,注解对象>
	 */
	public static Map<String, AnnotationListInfo<AnnotationInfo<RequestParam>>> requestParamMap = new ConcurrentHashMap<>();
	/**
	 * <包名.类名.方法名,Class元对象>
	 */
	public static Map<String, Class<?>> pojoParamMap = new ConcurrentHashMap<>();
	/**
	 * <包名.类名.方法名,注解对象>
	 */
	public static Map<String, AnnotationInfo<AjaxAccess>> ajaxAccessMap = new ConcurrentHashMap<>();
	/**
	 * <包名.类名.方法名,注解对象>
	 */
	public static Map<String, AnnotationInfo<WebCache>> webCacheMap = new ConcurrentHashMap<>();
	
	private String classPath; 
	
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		CacheBase.init();
		init();
	}
	
	public void init() {
//		String webcharSet = config.getWebcharSet();
//		String directives = config.getDirectives(); 
		classPath = config.getControllerClasspath(); 
		// 查找指定class路径
		if (classPath != null) {
			String[] classPathArr = classPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> controllerClassList = ClassUtil.findClasses(cpath);
				if (controllerClassList == null || controllerClassList.size()<=0) {
					throw new UncheckedException("没有扫描到配置的路径["+cpath+"]有任何Controller被注册，请检查config.properties文件system.controllerClasspath的配置");
				}
				for (Class<?> controllerClass : controllerClassList) {
					BeanContainer container = BeanFactory.getBeanContainer();
					Map<String, Object> mapContainer = container.getMapContainer();
					Collection<Object> containerCollection = mapContainer.values();
					for (Object beanObj : containerCollection) {
						Class<?> beanClass = beanObj.getClass();
						if(controllerClass == beanClass) {
							Method[] methodArr = null;
							try {
								methodArr = beanClass.getDeclaredMethods();
							} catch(NoClassDefFoundError e) {
								e.printStackTrace();
								throw new BaseException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
							}
							
							for (Method method : methodArr) {
								if (method.isAnnotationPresent(RequestMap.class)) {
									resolverRequestMap(beanClass,method,RequestMap.class,cpath);
									resolveRequestParam(beanClass, method);
									resolveAnno(beanClass, method,AjaxAccess.class,annoInfo -> ajaxAccessMap.put(beanClass.getName()+":"+method.getName(), annoInfo));
									resolveAnno(beanClass, method,WebCache.class,annoInfo -> webCacheMap.put(beanClass.getName()+":"+method.getName(), annoInfo));
								}
							}
						}
					}
				}
			}
			CacheManager.addCache("controllerMap",controllerMap);
			CacheManager.addCache("controllerRegularExpressionsList",controllerRegularExpressionsList);
			CacheManager.addCache("requestParamMap",requestParamMap);
			CacheManager.addCache("pojoParamMap",pojoParamMap);
			CacheManager.addCache("ajaxAccessMap",ajaxAccessMap);
			CacheManager.addCache("webCacheMap",webCacheMap);
			LOGGER.info("Framework 测试模式是否开启 -> " + config.getDebug());
			LOGGER.info("Framework 页面缓存总数[url请求] -> " + config.getPageCacheCount());
			LOGGER.info("Framework 字符编码格式 -> " + config.getCharset());
			LOGGER.info("Framework "+Constants.version+" Init");
		}
	}
	
	/**
	 * 方法用途: 解析请求元数据信息<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass 具体controll实例对应的class
	 * @param method
	 * @param clazzAnno
	 * @param cpath
	 */
	public <T extends Annotation>  void resolverRequestMap(Class<?> beanClass,Method method ,Class<T> clazzAnno,String cpath) {
		LOGGER.debug("请求映射注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
//		TypeVariable<?>[] tv = method.getTypeParameters();//泛型方法才会使用到
		Object obj = BeanFactory.getBean(beanClass);//如果mvc需要脱离ioc框架，那么这个直接创建实例，而不是从容器获取实例
		if(obj == null) {
			return;
		}
		if (method != null) {
			if (method.isAnnotationPresent(RequestMap.class)) {
//				T requestMap = method.getDeclaredAnnotation(clazzAnno);
				T requestMap = method.getAnnotation(clazzAnno);
				RequestMap rm = ((RequestMap) requestMap);
				boolean isStatic = rm.isStatic();
				for (String path : rm.path()) {
					if (StringUtil.isBlank(path)) {//没有设置RequestMap的path属性，那么以方法名为准
						path = "/"+method.getName();
					}
					path = getPrePath(beanClass, path);
					path = addRequestInfo(method.getName(),isStatic, cpath, obj, path);
				}
			}
		}
	}

	/**
	 * 方法用途: 获取类的RequestMap信息<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
	 * @param path
	 * @return
	 */
	private String getPrePath(Class<?> beanClass, String path) {
		RequestMap preControlMap = beanClass.getAnnotation(RequestMap.class);
		if(preControlMap == null) {
			return path;
		}
		String[] prePathArr = preControlMap.path();
		if(prePathArr.length<=0) {
			return path;
		}
		String prePath = prePathArr[0];
		if(StringUtil.isBlank(prePath)) {
			String controllerBeanName = beanClass.getSimpleName();
			controllerBeanName = StringUtil.lowerCaseByFirst(controllerBeanName).replace("Controller", "");
			prePath = "/"+controllerBeanName;
		}
		path = prePath + path;
		return path;
	}

	/**
	 * 方法用途: 映射绑定请求信息<br>
	 * 操作步骤: TODO<br>
	 * @param requestMethodName 请求处理方法名称
	 * @param isStatic 是否是静态的页面渲染
	 * @param cpath 配置文件中controller的classpath的路径
	 * @param obj
	 * @param path
	 * @return
	 */
	public static String addRequestInfo(String requestMethodName,boolean isStatic, String cpath, Object obj, String path) {
		List<String> endFixArr = new ArrayList<>();
		endFixArr.add(".json");
		endFixArr.add(".jsonp");
		endFixArr.add(".html");
		if(path.endsWith("$")) {//正则表达式的处理
			if(path.endsWith("/$")) {
				path = path.substring(0, path.length()-2)+"$";
			}
			boolean isExist = isContainsEndFix(path, endFixArr);
			if(!isExist) {
				for (String endFix : endFixArr) {
					controllerRegularExpressionsList.put(path.substring(0,path.length()-1)+endFix+"$", new ControllerAnnotationInfo<IBaseController<?>>((IBaseController<?>)obj, requestMethodName,isStatic));
				}
			}
			controllerRegularExpressionsList.put(path, new ControllerAnnotationInfo<IBaseController<?>>((IBaseController<?>)obj, requestMethodName,isStatic));
		} else {//非正则表达式处理
			if(path.endsWith("/")) {
				path = path.substring(0, path.length()-1);
			}
			boolean isExist = isContainsEndFix(path, endFixArr);
			if(!isExist) {
				for (String endFix : endFixArr) {
					controllerMap.put(path+endFix, new ControllerAnnotationInfo<IBaseController<?>>((IBaseController<?>)obj, requestMethodName,isStatic));
				}
			}
			controllerMap.put(path, new ControllerAnnotationInfo<IBaseController<?>>((IBaseController<?>)obj, requestMethodName,isStatic));
		}
		LOGGER.info("成功添加请求映射 [" + cpath + "."+obj.getClass().getName()+":"+requestMethodName+"] -> " + path);
		return path;
	}

	private static boolean isContainsEndFix(String path, List<String> endFixArr) {
		boolean isExist = false;
		for (String endFix : endFixArr) {
			if(path.contains(endFix)) {//多视图的解析
				isExist = true;
				break;
			}
		}
		return isExist;
	}
	
	private <T extends Annotation> void resolveRequestParam(Class<?> beanClass, Method method) {
		
		//这个参数注解的getParameterAnnotations的长度和getParameterTypes的长度相等,顺序一致一一对应
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		List<AnnotationInfo<RequestParam>> requestParamAnnoList = new ArrayList<>();
		for (int i=0; i< parameterTypes.length;i++) {
			Class<?> paramType = parameterTypes[i];
			Annotation[] annotationsParamType = parameterAnnotations[i];
			for (Annotation annotation : annotationsParamType) {//这里的循环次数是0到1次，包含@RequestParam的会循环一次，因为目前参数上只会有RequestParam注解
				if(annotation.annotationType() == RequestParam.class) {
					RequestParam requestParam = (RequestParam)annotation;
					String paramStr = requestParam.name();
					String exceptionMessage = beanClass.getName()+":"+method.getName()+"方法的第"+(i+1)+"个参数，参数类型为["+paramType.getName()+"]的参数名：只能是字符串，不可以是["+paramStr+"]";
					if(ObjectUtil.isInt(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的整型值");
					} else if(ObjectUtil.isBoolean(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的布尔值");
					} else if(ObjectUtil.isFloat(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的单精度浮点型值");
					} else if(ObjectUtil.isLong(paramStr)) {
						throw new StartupErrorException(exceptionMessage+"的长整型值");
					}
					AnnotationInfo<RequestParam> annoInfo = new AnnotationInfo<>();
					annoInfo.setAnnotatoionType(requestParam);
					annoInfo.setReturnType(paramType);
					requestParamAnnoList.add(annoInfo);
				}
			}
		}
		AnnotationListInfo<AnnotationInfo<RequestParam>> annoList= new AnnotationListInfo<>();
		if(parameterTypes.length<=0) {
			return;
		}
		annoList.setCount(parameterTypes.length);
		if(requestParamAnnoList.size()>0) {
			annoList.setAnnoList(requestParamAnnoList);
		}
		requestParamMap.put(beanClass.getName()+":"+method.getName(), annoList);
		pojoParamMap.put(beanClass.getName()+":"+method.getName(), parameterTypes[2]);
		LOGGER.debug("RequestParam注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解");
	}
	@SuppressWarnings("unchecked")
	private <T extends Annotation> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno,AnnotationResolverCallback<T> callbak) {
		Object obj = BeanFactory.getBean(beanClass);//如果mvc需要脱离ioc框架，那么这个直接创建实例，而不是从容器获取实例
		if(obj == null||method == null) {
			return;
		}
		if (!method.isAnnotationPresent(clazzAnno)) {
			return;
		}
		Annotation anno = method.getAnnotation(clazzAnno);
		AnnotationInfo<Annotation> annoInfo = new AnnotationInfo<>();
		annoInfo.setAnnotatoionType(anno);
		annoInfo.setReturnType(method.getReturnType());
		callbak.resolver((AnnotationInfo<T>)annoInfo);
		LOGGER.debug("Controller相关注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
	}
}
