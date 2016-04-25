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

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.dto.AnnotationInfo;
import com.meizu.simplify.exception.BaseException;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.enums.InitTypeEnum;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.dto.AnnotationListInfo;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ObjectUtil;
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
@Init(InitTypeEnum.CONTROL)
public class ControllerAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAnnotationResolver.class);
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	/**
	 * <requestMap地址,controller实例>
	 */
	public static Map<String, ControllerAnnotationInfo<BaseController<?>>> controllerMap = new ConcurrentHashMap<>();
	/**
	 * <requestMap地址,controller实例>
	 */
	public static Map<String, ControllerAnnotationInfo<BaseController<?>>> controllerRegularExpressionsList = new ConcurrentSkipListMap<>();
	/**
	 * <包名.类名.方法名,注解对象>
	 */
	public static Map<String, AnnotationListInfo<AnnotationInfo<RequestParam>>> requestParamMap = new ConcurrentHashMap<>();
	
	private String classPath; 
	
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		CacheBase.init();
		init();
	}
	
	public void init() {
//		String webcharSet = config.getWebcharSet();
//		String directives = config.getDirectives(); 
		classPath = config.getClasspath(); 
		// 查找指定class路径
		if (classPath != null) {
			String[] classPathArr = classPath.split(",");
			for (String cpath : classPathArr) {
				List<Class<?>> controllerClassList = ClassUtil.findClasses(cpath);
				if (controllerClassList == null || controllerClassList.size()<=0) {
					throw new UncheckedException("没有扫描到配置的路径["+cpath+"]有任何Controller被注册，请检查config.properties文件system.classpath的配置");
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
									resolveRequestParam(beanClass, method,cpath);
								}
							}
						}
					}
				}
			}
			LOGGER.info("Framework Debug -> " + config.getDebug());
//			LOGGER.info("Framework UrlCache Limit -> " + config.getUrlcacheCount());
//			LOGGER.info("Framework Charset -> " + config.getCharset());
			LOGGER.info("Framework v0.0.1-SNAPSHOT Init");
		}
	}
	
	private <T extends Annotation> void resolveRequestParam(Class<?> beanClass, Method method,String cpath) {
		
		//这个参数注解的getParameterAnnotations的长度和getParameterTypes的长度相等
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
	}
	
	/**
	 * 
	 * 方法用途: 解析请求元数据信息<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
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
				for (String path : ((RequestMap)requestMap).path()) {
					if (path != null && path.length() > 0) {
						RequestMap preControlMap = beanClass.getAnnotation(RequestMap.class);
						if(preControlMap!=null && preControlMap.path().length>0) {
							path = preControlMap.path()[0] + path;
						}
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
									controllerRegularExpressionsList.put(path.substring(0,path.length()-1)+endFix+"$", new ControllerAnnotationInfo<BaseController<?>>((BaseController<?>)obj, method.getName()));
								}
							}
							controllerRegularExpressionsList.put(path, new ControllerAnnotationInfo<BaseController<?>>((BaseController<?>)obj, method.getName()));
						} else {//非正则表达式处理
							if(path.endsWith("/")) {
								path = path.substring(0, path.length()-1);
							}
							boolean isExist = isContainsEndFix(path, endFixArr);
							if(!isExist) {
								for (String endFix : endFixArr) {
									controllerMap.put(path+endFix, new ControllerAnnotationInfo<BaseController<?>>((BaseController<?>)obj, method.getName()));
								}
							}
							controllerMap.put(path, new ControllerAnnotationInfo<BaseController<?>>((BaseController<?>)obj, method.getName()));
						}
						LOGGER.info("成功添加请求映射 [" + cpath + "."+obj.getClass().getName()+":"+method.getName()+"] -> " + path);
					}
				}
			}
		}
	}

	private boolean isContainsEndFix(String path, List<String> endFixArr) {
		boolean isExist = false;
		for (String endFix : endFixArr) {
			if(path.contains(endFix)) {//多视图的解析
				isExist = true;
				break;
			}
		}
		return isExist;
	}
}
