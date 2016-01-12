package com.meizu.simplify.utils;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.enums.Chars;
import com.meizu.simplify.enums.Encoding;
import com.meizu.simplify.exception.UncheckedException;

/**
  * <p><b>Title:</b><i>类查找加载工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午2:15:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午2:15:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ClassUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);
	
    private ClassUtil() {
    }
    
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
		List<Class<?>> classes = new ArrayList<Class<?>>();
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
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (Class<?> targetClass : findClasses(packageNames)) {
			if (targetClass.isAnnotationPresent(annotationClass)) {
				classes.add(targetClass);
			}
		}
		return classes;
	}

	/**
	 * 
	 * 方法用途: 查找指定包下的类集合<br>
	 * 操作步骤: TODO<br>
	 * @param packageNames 包名
	 * @return 返回指定包下的类集合
	 */
	public static List<Class<?>> findClasses(String... packageNames) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		for (String className : findClassNames(packageNames)) {
			try {
				classes.add(Class.forName(className));
			} catch (Throwable e) {
				LOGGER.warn("加载[" + className + "]类时发生异常。", e);
			}
		}
		return classes;
	}

	/**
	 * 
	 * 方法用途: 查找指定包下的类名集合<br>
	 * 操作步骤: TODO<br>
	 * @param packageNames 包名
	 * @return 返回指定包下的类名集合
	 */
	private static List<String> findClassNames(String... packageNames) {
		List<String> classNames = new ArrayList<String>();
		try {
			for (String packageName : packageNames) {
				String packagePath = packageName.replace(".", Chars.BACKSLASH.toString());
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
			}
		} catch (Exception e) {
			throw new UncheckedException("获取指定包下类名集合时发生异常。", e);
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
		List<String> classNames = new ArrayList<String>();
		try {
			String jarPath = URLDecoder.decode(url.toExternalForm(),
					Encoding.UTF_8.toString());
			LOGGER.debug("开始获取[{}]中的类名...", jarPath);
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
		List<String> classNames = new ArrayList<String>();
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
			String dirPath = URLDecoder.decode(url.getFile(), Encoding.UTF_8.toString());
			LOGGER.debug("开始获取[{}]中的类名...", dirPath);
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
	private static List<String> getClassNamesFromDir(File dir,
			String packageName) {
		List<String> classNames = new ArrayList<String>();
		try {
			File[] files = dir.listFiles();
			String separator = System.getProperty("file.separator");
			for (File file : files) {
				if (file.isDirectory()) {
					classNames.addAll(getClassNamesFromDir(file, packageName
							+ "." + file.getName()));
				} else if (file.getName().endsWith(".class")) {
					String className = file.getPath();
					className = className.replace(separator, ".");
					className = packageName
							+ StringUtil.substringAfterLast(className,
									packageName);
					className = className.replaceFirst(".class$", "");
					classNames.add(className);
				}
			}
		} catch (Exception e) {
			LOGGER.warn("获取目录中的类名时发生异常。", e);
		}
		return classNames;
	}

	
	

}
