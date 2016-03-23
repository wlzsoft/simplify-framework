package com.meizu.simplify.mvc;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.utils.ClassUtil;
import com.meizu.simplify.utils.PropertieUtil;


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
	
	protected static PropertieUtil config = new PropertieUtil("properties/config.properties");//注意，要合并成一个实例，不要太多重复实例 TODO
	
	public static HashMap<String, ServletModel> servletMap = new HashMap<String, ServletModel>();
	public static Map<String, ControllerAnnotationInfo> controllerMap = new ConcurrentHashMap<>();
	public static boolean debug = false;
	public static String charSet = null;
//	public static String webcharSet = "ISO-8859-1";//页面级别的乱码控制，主要是post和get请求可能会产生的乱码问题，目前暂未开放 TODO
	public static Integer urlcacheCount = 100;
	public static String class_path; 
	public static String directives; // velocity自定义Directive
	public static String getPath() {
		String path = MvcInit.class.getResource("/").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	public static void init() {
		debug = config.getBoolean("system.debug", false);
		charSet = config.getString("system.charset", null);
//		webcharSet = config.getString("system.webcharSet", "ISO-8859-1");
		class_path = config.getString("system.classpath", null);
		directives = config.getString("system.directives", null);
		
		// 查找指定class路径
		if (class_path != null) {
			
			List<Class<?>> controllerClassList = ClassUtil.findClasses(class_path);
			if (controllerClassList == null || controllerClassList.size()<=0) {
				throw new UncheckedException("没有扫描到配置的路径[system.classpath="+class_path+"]有任何Controller被注册，请检查config.properties文件system.classpath的配置");
			}
			for (Class<?> controllerClass : controllerClassList) {
				resolverRequestInfo(controllerClass);
			}
			LOGGER.info("Framework Debug -> " + debug);
	//		LOGGER.log("Framework UrlCache Limit -> " + urlcacheCount);
	//		LOGGER.log("Framework Charset -> " + charSet);
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
