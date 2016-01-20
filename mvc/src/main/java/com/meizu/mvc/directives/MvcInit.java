package com.meizu.mvc.directives;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;

import com.meizu.mvc.FiFoMap;
import com.meizu.mvc.directives.SecurityFilter.RequestSet;
import com.meizu.simplify.utils.PropertieUtil;
import com.meizu.simplify.utils.StringUtil;


/**
 * Mvc框架初始化
 * 
 * 将mvc部分分离出来
 */

@SuppressWarnings("unchecked")
public class MvcInit {
	protected static PropertieUtil config = new PropertieUtil("config.properties");
	
	public static FiFoMap<String, Object[]> urlCache; // url请求缓存
	public static HashMap<String, ServletModel> servletMap = new HashMap<String, ServletModel>(); // servletMap
	public static boolean debug = false;
	public static String charSet = null;
	public static String webcharSet = "ISO-8859-1";
	
	public static boolean hibernate = false;
	
	public static Integer urlcacheCount = 100;
	
	public static String class_path; // class位置
	
	public static String directives; // velocity自定义Directive
	
	public static String authorization_user = "";
	public static String authorization_password = "";
	
	public static String hibernate_shards;
	public static String hibernate_selectionstrategy;
	public static String hibernate_resolutionstrategy;
	
	public static int limitExecutionTime = 0;
	
	public MvcInit() {
	}
	
	public static String getPath() {
		String path = MvcInit.class.getResource("/config.properties").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	
	static {
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
			if (fns != null) {
				for (int i = 0; i < fns.length; i++) {
					String name = fns[i].getAbsoluteFile().getName().replace(".class", "");
					try {
						Class<HttpServlet> entityClass = (Class<HttpServlet>) Class.forName(class_path + "." + name);
						for (Method method : entityClass.getMethods()) {
							if (method != null && method.getName().indexOf("do") == 0) {
								// 检查annotation 设置
								if (method.isAnnotationPresent(RequestSet.class)) {
									RequestSet rset = (RequestSet) method.getAnnotation(RequestSet.class);
									for (String _path : rset.path().split("\\s+", -1)) {
										if (_path != null && _path.length() > 0) {
//											PrintHelper.getPrint().debug("ADDED " + class_path + " -> " + _path);
											servletMap.put(_path, new ServletModel(entityClass, method.getName()));
										}
									}
								}
							}
						}
					} catch (ClassNotFoundException e) {
					}
				}
			}
		}
		
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework Debug -> " + debug);
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework UrlCache Limit -> " + urlcacheCount);
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "DxFramework Charset -> " + charSet);
//		PrintHelper.getPrint().log(ControlPrint.LOG_EMPTY, "DxFramework v1.6 Init.");
	}
}
