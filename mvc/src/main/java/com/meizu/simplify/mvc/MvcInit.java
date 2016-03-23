package com.meizu.simplify.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * 
 * <p><b>Title:</b><i> Mvc框架初始化</i></p>
 * <p>Desc: </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:47:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:47:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class MvcInit {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MvcInit.class);
	
	protected static PropertiesConfig config;
	
	public static HashMap<String, ServletModel> servletMap = new HashMap<String, ServletModel>();
	public static Map<String, ControllerAnnotationInfo> controllerMap = new ConcurrentHashMap<>();
	public static String class_path; 
	public static String getPath() {
		String path = MvcInit.class.getResource("/").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	public static void init() {
		CacheBase.init();
		config = BeanFactory.getBean(PropertiesConfig.class);
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
				resolverRequestInfo(controllerClass);
			}
			LOGGER.info("Framework Debug -> " + config.getDebug());
//			LOGGER.info("Framework UrlCache Limit -> " + config.getUrlcacheCount());
//			LOGGER.info("Framework Charset -> " + config.getCharset());
			LOGGER.info("Framework v0.0.1-SNAPSHOT Init");
		}
	
	}
	
	/**
	 * 
	 * 方法用途: 解析请求元数据信息<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 */
	public static void resolverRequestInfo(Class<?> entityClass) {
		Object obj = getInstance(entityClass);
		if(obj == null) {
			return;
		}
		for (Method method : entityClass.getMethods()) {
			if (method != null) {
				if (method.isAnnotationPresent(RequestMap.class)) {
					RequestMap requestMap = (RequestMap) method.getAnnotation(RequestMap.class);
					for (String path : requestMap.path()) {
						if (path != null && path.length() > 0) {
							RequestMap preControlMap = entityClass.getAnnotation(RequestMap.class);
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

	/**
	 * 
	 * 方法用途: 获取工厂中的bean实例<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 * @return
	 */
	private static Object getInstance(Class<?> entityClass) {
		Object obj = null;
		obj = BeanFactory.getBean(entityClass);//和ioc框架集成，需要使用这个方式获取实体
	    /*try {
			obj = entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return obj;
	}
}
