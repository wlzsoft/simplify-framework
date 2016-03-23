package com.meizu.simplify.mvc.resolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.exception.CacheException;
import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.StartupErrorException;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanContainer;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Init;
import com.meizu.simplify.ioc.resolver.IAnnotationResolver;
import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.mvc.ServletModel;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.webcache.web.CacheBase;

/**
  * <p><b>Title:</b><i>mvc请求地址解析器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月6日 上午11:08:36</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月6日 上午11:08:36</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Init(4)
public class ControllerAnnotationResolver implements IAnnotationResolver<Class<?>>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAnnotationResolver.class);
	private PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
	public static HashMap<String, ServletModel> servletMap = new HashMap<String, ServletModel>();
	public static Map<String, ControllerAnnotationInfo> controllerMap = new ConcurrentHashMap<>();
	public String class_path; 
	
	@Override	
	public void resolve(List<Class<?>> resolveList) {
		CacheBase.init();
		init();
	}
	
	public void init() {
//		String webcharSet = config.getWebcharSet();
//		String directives = config.getDirectives(); 
		class_path = config.getClasspath(); 
		// 查找指定class路径
		if (class_path != null) {
			
			List<Class<?>> controllerClassList = ClassUtil.findClasses(class_path);
			if (controllerClassList == null || controllerClassList.size()<=0) {
				throw new UncheckedException("没有扫描到配置的路径[system.classpath="+class_path+"]有任何Controller被注册，请检查config.properties文件system.classpath的配置");
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
							throw new CacheException("bean["+beanClass.getName()+"] 无法找到bean中方法依赖的第三方class，确认是否缺少class文件==>"+e.getMessage());
						}
						
						for (Method method : methodArr) {
							if (method.isAnnotationPresent(RequestMap.class)) {
								resolveAnno(beanClass, method,RequestMap.class);
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
	
	private <T extends Annotation> void resolveAnno(Class<?> beanClass, Method method,Class<T> clazzAnno) {
		
		resolverRequestInfo(beanClass,method,clazzAnno);
		
//		TypeVariable<?>[] tv = method.getTypeParameters();//泛型方法才会使用到

		
		//		@RequestParam解析开始
		//RequestParam 的解析应该迁移到这里，目前requestParma的解析全在BaseController类中 TODO
		//这个参数注解的getParameterAnnotations的长度和getParameterTypes的长度相等
		Class<?>[] paramTypeClazz = method.getParameterTypes();
		Annotation[][] annotationTwoArr = method.getParameterAnnotations();
		for (int i=0; i< paramTypeClazz.length;i++) {
			Class<?> paramType = paramTypeClazz[i];
			Annotation[] annotationsParamType = annotationTwoArr[i];
			for (Annotation annotation : annotationsParamType) {//这里的循环次数是0到1次，包含@RequestParam的会循环一次，因为目前参数上只会有RequestParam注解
				if(annotation.annotationType() == RequestParam.class) {
					RequestParam param = (RequestParam)annotation;
					String paramStr = param.name();
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
				}
			}
		}
//		@RequestParam解析结束
	}
	
	/**
	 * 
	 * 方法用途: 解析请求元数据信息<br>
	 * 操作步骤: TODO<br>
	 * @param beanClass
	 */
	public <T extends Annotation>  void resolverRequestInfo(Class<?> beanClass,Method method ,Class<T> clazzAnno) {
		LOGGER.debug("请求映射注解解析：方法["+beanClass.getName()+":"+method.getName()+"] 上的注解["+clazzAnno.getName()+"]");
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
						controllerMap.put(path, new ControllerAnnotationInfo(obj, method.getName()));
						LOGGER.info("成功添加请求映射 [" + class_path + "."+obj.getClass().getName()+":"+method.getName()+"] -> " + path);
					}
				}
			}
		}
	}
}
