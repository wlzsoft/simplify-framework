package com.meizu.mvc;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.meizu.mvc.SecurityFilter.RequestSet;
import com.meizu.simplify.utils.PropertieUtil;


/**
 * 
 * <p><b>Title:</b><i> Mvc框架初始化</i></p>
 * <p>Desc: 将mvc部分分离出来, 抽离使用，提供第二种mvc处理方式，或是直接废弃
 *          注意：目前这个类会做部分初始化操作</p>
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
@Deprecated
public class MvcInit {
	protected static PropertieUtil config = new PropertieUtil("properties/config.properties");
	
//	public static FiFoMap<String, Object[]> urlCache; // url请求缓存
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
		String path = MvcInit.class.getResource("/properties/config.properties").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	
	static {
		debug = config.getBoolean("system.debug", false);
		charSet = config.getString("system.charset", null);
		webcharSet = config.getString("system.webcharSet", "ISO-8859-1");
		
		class_path = config.getString("system.classpath", null);
		directives = config.getString("system.directives", null);
//		urlCache = new FiFoMap<String, Object[]>((urlcacheCount = config.getInteger("system.urlcacheCount", 100)));
		
		// 查找指定class路径
		if (class_path != null) {
			String path = null;//StringUtils.format("{0}/{1}", getPath(), class_path.replaceAll("\\.", "/"));
			File file = new File(path);
			File[] fns = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith(".class");
				}
			});
			if (fns != null) {
				for (int i = 0; i < fns.length; i++) {
					String name = fns[i].getAbsoluteFile().getName().replace(".class", "");
//					try {
//						Class<HttpServlet> entityClass = (Class<HttpServlet>) Class.forName(class_path + "." + name);
						Class<?> entityClass = null;
						for (Method method : entityClass.getMethods()) {
							if (method != null && method.getName().indexOf("do") == 0) {
								// 检查annotation 设置
								if (method.isAnnotationPresent(RequestSet.class)) {
									RequestSet rset = (RequestSet) method.getAnnotation(RequestSet.class);
									for (String _path : rset.path().split("\\s+", -1)) {
										if (_path != null && _path.length() > 0) {
//											PrintHelper.getPrint().debug("ADDED " + class_path + " -> " + _path);
//											servletMap.put(_path, new ServletModel(entityClass, method.getName()));
										}
									}
								}
							}
						}
//					} catch (ClassNotFoundException e) {
//					}
				}
			}
		}
		
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework Debug -> " + debug);
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework UrlCache Limit -> " + urlcacheCount);
//		PrintHelper.getPrint().log(ControlPrint.LOG_SET, "Framework Charset -> " + charSet);
//		PrintHelper.getPrint().log(ControlPrint.LOG_EMPTY, "Framework v0.0.1-SNAPSHOT Init.");
	}
}
