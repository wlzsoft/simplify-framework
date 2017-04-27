package vip.simplify.utils;

import java.io.File;

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
public class ClassPathUtil {
	
    private ClassPathUtil() {
    }
	
	/**
	 * 方法用途: 得到类所在的实际位置。一个类处在某个位置的class或jar包中，根据此位置得到此类对应的文件。<br>
	 * 不同位置的类得到的文件是不一样的<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 类
	 * @return 类在系统中的实际文件名
	 */
	public static String getRealPathName(Class<?> clazz) {
		String path = getClassPath(clazz,getClassNameWithoutPackage(clazz) + ".class");
		return path;
	}
	private static String getClassNameWithoutPackage(Class<?> clazz) {
		String className = clazz.getName();
		int pos = className.lastIndexOf('.') + 1;
		if (pos == -1) {
			pos = 0;
		}
		String name = className.substring(pos);
		return name;
	}

	/**
	 * 方法用途: 得到唯一文件<br>
	 * 操作步骤: 一个类处在某个位置的class或jar包中，根据此位置得到此类对应的文件。<br>
	 * 不同位置的类得到的文件是不一样的<br>
	 * @param clazz 类
	 * @param extension 带点的文件扩展名
	 * @return File
	 */
	public static File getUniqueFile(Class<?> clazz, String extension) {
		int key = 0;
		String classNameWithoutPk = getClassNameWithoutPackage(clazz);
		String path = getClassPath(clazz,classNameWithoutPk + ".class");
		if (path != null) {
			key = path.hashCode();
		}
		File propFile = new File(System.getProperty("java.io.tmpdir"), classNameWithoutPk+ key + extension);
		return propFile;
	}
	public static String getClassPath(Class<?> clazz,String packageName) {
		String path = clazz.getResource(packageName).getPath();
		return path;
	}
	public static String getClassPath(Class<?> clazz) {
		return getClassPath(clazz,"/");
	}
	public static String getClassPath() {
		return getClassPath(ClassPathUtil.class);
	}

}
