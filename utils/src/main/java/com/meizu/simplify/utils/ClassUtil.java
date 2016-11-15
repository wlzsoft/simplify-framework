package com.meizu.simplify.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.enums.EncodingEnum;
import com.meizu.simplify.utils.enums.SpecialCharacterEnum;

/**
  * <p><b>Title:</b><i>类查找加载工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午2:15:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午2:15:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ClassUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
    private ClassUtil() {
    }
//    ===========================查找类相关的===================================
	/**
	 * 
	 * 方法用途: 查找指定包下继承了指定类或实现了指定接口的类集合<br>
	 * 操作步骤: TODO<br>
	 * @param parentClass 父类或接口
	 * @param packageNames 包名
	 * @return 返回指定包下继承了指定类或实现了指定接口的类集合
	 */
	public static List<Class<?>> findClassesByParentClass(Class<?> parentClass,
			String... packageNames) {
		List<Class<?>> classes = new ArrayList<>();
		for (Class<?> targetClass : findClasses(packageNames)) {
			if (targetClass != parentClass
					&& parentClass.isAssignableFrom(targetClass)
					&& !Modifier.isAbstract(targetClass.getModifiers())) {
				classes.add(targetClass);
			}
		}
		return classes;
	}
	
	/**
	 * 
	 * 方法用途: 通过类型为Interface的class来查找其实现类列表<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @return
	 */
	public static List<Class<?>> findClassesByInterfaces(Class<?> clazz,String... packageNames) {
		if(!clazz.isInterface()) {
			throw new UncheckedException("类型异常，必须是接口");
		}
		return findClassesByParentClass(clazz,packageNames);
	}

	/**
	 * 
	 * 方法用途: 查找指定包下标注了指定注解的类集合<br>
	 * 操作步骤: TODO<br>
	 * @param annotationClass 注解
	 * @param packageNames 包名
	 * @return 返回指定包下标注了指定注解的类集合
	 */
	public static List<Class<?>> findClassesByAnnotationClass(
			Class<? extends Annotation> annotationClass, String... packageNames) {
		List<Class<?>> classes = new ArrayList<>();
		for (Class<?> targetClass : findClasses(packageNames)) {
			if (targetClass.isAnnotationPresent(annotationClass)) {
				classes.add(targetClass);
			}
		}
		return classes;
	}

	/**
	 * 方法用途: 通过类名获取Class对象<br>
	 * 操作步骤: TODO<br>
	 * @param className 类名
	 * @return
	 */
	public static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			LOGGER.warn("通过[" + className + "]获取对应的Class对象时发生异常。", e);
		}
		return null;
	}
	
	/**
	 * 缓存Class对象的集合
	 */
	private static Map<String,List<Class<?>>> classListMap = new ConcurrentHashMap<>();
	
	/**
	 * 方法用途: 清楚Class对象集合的缓存记录，并设置类对象集合的引用为空<br>
	 * 操作步骤: TODO<br>
	 */
	public static void clearClassList() {
		classListMap.clear();
		classListMap = null;
	}
	
	/**
	 * 方法用途: 获取Class对象集合的缓存记录<br>
	 * 操作步骤: TODO<br>
	 */
	public static Map<String,List<Class<?>>> getClassList() {
		return classListMap;
	}
	
	/**
	 * 
	 * 方法用途: 查找指定包下的类集合<br>
	 * 操作步骤: TODO<br>
	 * @param packageNames 包名
	 * @return 返回指定包下的类集合
	 */
	public static List<Class<?>> findClasses(String... packageNames) {
		return findClasses(true,packageNames);
	}
	
	/**
	 * 
	 * 方法用途: 查找指定包下的类集合<br>
	 * 操作步骤: TODO<br>
	 * @param packageNames 包名
	 * @param isCache 是否缓存
	 * @return 返回指定包下的类集合
	 */
	public static List<Class<?>> findClasses(boolean isCache,String... packageNames) {
		String packageNamesStr = CollectionUtil.listToStringBySplit(packageNames, "", "",",");
		if(isCache) {//缓存集合
			if(classListMap == null) {
				classListMap = new ConcurrentHashMap<>();
			}
			if(CollectionUtil.isNotEmpty(classListMap)) {
				 List<Class<?>> list = classListMap.get(packageNamesStr);
				 if(CollectionUtil.isNotEmpty(list)) {
					 return list;
				 }
			}
		}
		List<Class<?>> classes = new ArrayList<>();
		for (String className : findClassNames(packageNames)) {
			try {
				classes.add(getClass(className));
			} catch (ExceptionInInitializerError e) {
				LOGGER.error("加载[" + className + "]类时发生异常:由于加载类过程中，类中的静态变量和静态块有做new的操作，在执行相关构造函数时报错，具体错误信息如下：", e);
			} catch (Throwable e) {
				LOGGER.warn("加载[" + className + "]类时发生异常。", e);
//				System.err.println("加载[" + className + "]类时发生异常。"+e.getMessage());
			}
		}
		if(isCache) {//缓存集合
			classListMap.put(packageNamesStr, classes);
		}
		return classes;
	}
	
	/**
	 * 
	 * 方法用途: 查找指定包下的类名集合<br>
	 * 操作步骤: 默认缓存类名集合<br>
	 * @param packageNames 包名
	 * @return 返回指定包下的类名集合
	 */
	private static List<String> findClassNames(String... packageNames) {
		return findClassNames(true,packageNames);
	}
	
	/**
	 * 缓存classNames的集合
	 * TODO 废弃，用classListMap代替
	 */
	private static Map<String,List<String>> classNameListMap = new ConcurrentHashMap<>();
	
	/**
	 * 方法用途: 清楚类名集合的缓存记录，并设置类名集合的引用为空<br>
	 * 操作步骤: TODO 废弃，用classListMap代替<br>
	 */
	public static void clearClassNameList() {
		classNameListMap.clear();
		classNameListMap = null;
	}
	
	/**
	 * 
	 * 方法用途: 查找指定包下的类名集合<br>
	 * 操作步骤: 可选择是否缓存类名集合<br>
	 * @param isCache 是否缓存
	 * @param packageNames 包名
	 * @return 返回指定包下的类名集合
	 */
	private static List<String> findClassNames(boolean isCache,String... packageNames) {
		
		String packageNamesStr = CollectionUtil.listToStringBySplit(packageNames, "", "",",");
		if(isCache) {//缓存集合
			if(classNameListMap == null) {
				classNameListMap = new ConcurrentHashMap<>();
			}
			if(CollectionUtil.isNotEmpty(classNameListMap)) {
				 List<String> list = classNameListMap.get(packageNamesStr);
				 if(CollectionUtil.isNotEmpty(list)) {
					 return list;
				 }
			}
		}
		List<String> classNames = new ArrayList<>();
		for (String packageName : packageNames) {
			try {
				String packagePath = packageName.replace(".", SpecialCharacterEnum.BACKSLASH.toString());
				Enumeration<URL> packageUrls = Thread.currentThread()
						.getContextClassLoader().getResources(packagePath);
				while (packageUrls.hasMoreElements()) {
					URL packageUrl = packageUrls.nextElement();
					if (packageUrl.getProtocol().equals("jar")) {
						classNames.addAll(getClassNamesFromJar(packageUrl,
								packageName));
					} else {
						classNames.addAll(getClassNamesFromDir(packageUrl,
								packageName));
					}
				}
			} catch (Exception e) {
				LOGGER.error("获取指定包下类名集合时发生异常。"+e.getMessage());
//					throw new UncheckedException("获取指定包下类名集合时发生异常。", e);
			}
		}
		if(isCache) {//缓存集合
			classNameListMap.put(packageNamesStr, classNames);
		}
		
		return classNames;
	}

	/**
	 * 
	 * 方法用途: 从jar包中获取指定包下的类名集合<br>
	 * 操作步骤: TODO<br>
	 * @param url jar包的url
	 * @param packageName 包名
	 * @return 返回jar包中指定包下的类名集合
	 */
	private static List<String> getClassNamesFromJar(URL url, String packageName) {
		List<String> classNames = new ArrayList<>();
		try {
			String jarPath = URLDecoder.decode(url.toExternalForm(),
					EncodingEnum.UTF_8.toString());
			LOGGER.debug("开始获取[{}]中的类名...", jarPath);
//			System.out.println("开始获取["+jarPath+"]中的类名...");
			jarPath = StringUtil.substringAfter(jarPath, "jar:file:");
			jarPath = StringUtil.substringBeforeLast(jarPath, "!");
			JarFile jarInputStream = new JarFile(jarPath);
			Enumeration<JarEntry> jarEntries = jarInputStream.entries();
			while (jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				classNames.addAll(getClassNamesFromJar(jarEntry, packageName));
			}
			jarInputStream.close();
		} catch (Exception e) {
			LOGGER.warn("获取jar包中的类名时发生异常。", e);
//			System.err.println("获取jar包中的类名时发生异常。"+ e);
			
		}
		return classNames;
	}

	/**
	 * 
	 * 方法用途: 从jar包文件单元中获取指定包下的类名集合<br>
	 * 操作步骤: TODO<br>
	 * @param jarEntry jar包文件单元
	 * @param packageName 包名
	 * @return 返回jar包文件单元中获取指定包下的类名集合
	 */
	private static List<String> getClassNamesFromJar(JarEntry jarEntry,
			String packageName) {
		List<String> classNames = new ArrayList<>();
		if (!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
			String className = jarEntry.getName();
			className = className.replaceFirst(".class$", "");
			className = className.replace('/', '.');
			if (className.contains(packageName)) {
				classNames.add(className);
			}
		}
		return classNames;
	}

	/**
	 * 
	 * 方法用途: 从目录中获取指定包下的类名集合<br>
	 * 操作步骤: TODO<br>
	 * @param url 目录url
	 * @param packageName 包名
	 * @return 返回目录中指定包下的类名集合
	 */
	private static List<String> getClassNamesFromDir(URL url, String packageName) {
		try {
			String dirPath = URLDecoder.decode(url.getFile(), EncodingEnum.UTF_8.toString());
			LOGGER.debug("开始获取[{}]中的类名...", dirPath);
//			System.out.println("开始获取["+dirPath+"]中的类名...");
			return getClassNamesFromDir(new File(dirPath), packageName);
		} catch (Exception e) {
			throw new UncheckedException("从目录中获取类名时发生异常。", e);
		}
	}

	/**
	 * 
	 * 方法用途: 从目录中获取指定包下的类名集合<br>
	 * 操作步骤: TODO<br>
	 * @param dir 目录
	 * @param packageName 包名
	 * @return 返回目录中指定包下的类名集合
	 */
	private static List<String> getClassNamesFromDir(File dir,String packageName) {
		String separator = System.getProperty("file.separator");
		List<String> classNames =  getClassFromDir(dir,new ICallbackClass<String>() {
			@Override
			public String call(File file,Object... params) {
				String className = file.getPath();
				className = className.replace(separator, ".");
				className = packageName	+ StringUtil.substringAfterLast(className,packageName);
				className = className.replaceFirst(".class$", "");
				return className;
			}
		});
		
		return classNames;
	}
	
	/**
	  * <p><b>Title:</b><i>类查找加载工具类</i></p>
	 * <p>Desc: TODO</p>
	 * <p>source folder:{@docRoot}</p>
	 * <p>Copyright:Copyright(c)2014</p>
	 * <p>Company:meizu</p>
	 * <p>Create Date:2016年1月7日 下午2:15:40</p>
	 * <p>Modified By:luchuangye-</p>
	 * <p>Modified Date:2016年1月7日 下午2:15:40</p>
	 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
	 * @version Version 0.1
	 * @param <T>
	 */
	public interface ICallbackClass<T> {
		
		/**
		 * 方法用途: 从目录中获取指定包下的类名集合<br>
		 * 操作步骤: TODO<br>
		 * @param file 返回最原始的file对象
		 * @param params 0个或多个参数值
		 * @return 最终转换的后对象
		 */
		public T call(File file,Object... params);
	} 
	
	/**
	 * 方法用途: 从目录中获取的类文件-可定制处理后的返回结果集<br>
	 * 操作步骤: TODO<br>
	 * @param dir
	 * @param call 通过这个回调函数处理单个class文件并返回结果
	 * @param params 处理时要传递的参数
	 * @return 返回class文件处理后的结果集
	 */
	public  static <T> List<T> getClassFromDir(File dir,ICallbackClass<T> call,Object... params) {
		List<T> classFiles = new ArrayList<>();
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					classFiles.addAll(getClassFromDir(file,call,params));
				} else if (file.getName().endsWith(".class")) {
					T t = call.call(file,params);
					if(t != null) {
						classFiles.add(t);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("获取目录中的类文件对象发生异常。", e);
//			System.err.println("获取目录中的类名时发生异常。"+ e);
		}
		return classFiles;
	}

//	========================查找方法相关====================================
	/**
	 * 
	 * 方法用途: 查找指定class的方法中上标注有指定注解的方法<br>
	 * 操作步骤: TODO<br>
	 * @param annoClass
	 * @param beanClass
	 * @return
	 */
	public static <T extends Annotation> List<Method> findMethodByAnnotation(Class<T> annoClass,Class<?> beanClass) {
		Method[] methodArr = beanClass.getMethods();
		List<Method> resultMethodList = new ArrayList<>();
		for (Method method : methodArr) {
			T ib = method.getAnnotation(annoClass);
			if(ib != null) {
				resultMethodList.add(method);
			}
		}
		return resultMethodList;
	}
	
	/**
	 * 
	 * 方法用途: 查找指定class的属性中上标注有指定注解的属性<br>
	 * 操作步骤: TODO<br>
	 * @param annoClass
	 * @param beanClass
	 * @return
	 * 注： 这个方法和 findMethodByAnnotation 的代码极其相似，是否合并，后续考虑 TODO
	 */
	public static <T extends Annotation> List<Field> findDeclaredFieldByAnnotation(Class<T> annoClass,Class<?> beanClass) {
		Field[] fieldArr = beanClass.getDeclaredFields();
		List<Field> resultFieldList = new ArrayList<>();
		for (Field field : fieldArr) {
			T ib = field.getAnnotation(annoClass);
			if(ib != null) {
				resultFieldList.add(field);
			}
		}
		return resultFieldList;
	}

}
