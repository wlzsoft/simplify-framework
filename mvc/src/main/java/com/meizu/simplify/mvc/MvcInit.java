package com.meizu.simplify.mvc;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.dto.ControllerAnnotationInfo;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;


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
	
	protected static PropertieUtil config = new PropertieUtil("properties/config.properties");
	
	public static FiFoMap<String, Object[]> urlCache; // url请求缓存,对urlcache的缓存记录方式做了先进先出模式
	public static HashMap<String, ServletModel> servletMap = new HashMap<String, ServletModel>(); // servletMap
	public static Map<String, ControllerAnnotationInfo> controllerMap = new ConcurrentHashMap<>();
	public static boolean debug = false;
	public static String charSet = null;
	public static String webcharSet = "ISO-8859-1";
	public static Integer urlcacheCount = 100;
	public static String class_path; // class位置
	public static String directives; // velocity自定义Directive
	
	
	public MvcInit() {
	}
	
	public static String getPath() {
		String path = MvcInit.class.getResource("/").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	
	public static void init() {
		debug = config.getBoolean("system.debug", false);
		charSet = config.getString("system.charset", null);
		webcharSet = config.getString("system.webcharSet", "ISO-8859-1");
		
		class_path = config.getString("system.classpath", null);
		directives = config.getString("system.directives", null);
		urlCache = new FiFoMap<String, Object[]>((urlcacheCount = config.getInteger("system.urlcacheCount", 100)));
		
		// 查找指定class路径
		if (class_path != null) {
			String path = StringUtil.format("{0}/{1}", getPath(), class_path.replaceAll("\\.", "/"));
			File file = new File(path);
			File[] fns = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".class");
				}
			});
			
			if (fns == null) {
				throw new UncheckedException("没有扫描到配置的路径["+path+"]有任何Controller被注册，请检查config.properties文件system.classpath的配置");
			}
			for (int i = 0; i < fns.length; i++) {
				String name = fns[i].getAbsoluteFile().getName().replace(".class", "");
				try {
					Class<?> entityClass = (Class<?>) Class.forName(class_path + "." + name);
					resolverRequestInfo(entityClass);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
	//		LOGGER.log("Framework Debug -> " + debug);
	//		LOGGER.log("Framework UrlCache Limit -> " + urlcacheCount);
	//		LOGGER.log("Framework Charset -> " + charSet);
	//		LOGGER.log("Framework v0.0.1-SNAPSHOT Init.");
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
				// 检查annotation 设置
				if (method.isAnnotationPresent(RequestMap.class)) {
					RequestMap rset = (RequestMap) method.getAnnotation(RequestMap.class);
					for (String _path : rset.path().split("\\s+", -1)) {
						if (_path != null && _path.length() > 0) {
							LOGGER.debug("ADDED [" + class_path + "."+obj.getClass().getName()+":"+method.getName()+"] -> " + _path);
//							System.out.println("ADDED [" + class_path + "."+obj.getClass().getName()+":"+method.getName()+"] -> " + _path);
							controllerMap.put(_path, new ControllerAnnotationInfo(obj, method.getName()));
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
