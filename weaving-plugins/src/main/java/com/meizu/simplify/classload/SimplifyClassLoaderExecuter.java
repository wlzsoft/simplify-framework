package com.meizu.simplify.classload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.DateUtil;

/**
 * <p><b>Title:</b><i>作为自定义ClassLoad的执行器</i></p>
* <p>Desc: 解决类似：attempted  duplicate class definition for name: "com/meizu/xxx" 的问题 </p>
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
public class SimplifyClassLoaderExecuter {
	
	private static ByteCodeClassLoader byteCodeClassLoader = new ByteCodeClassLoader(Thread.currentThread().getContextClassLoader());
//	private static ByteCodeClassLoader byteCodeClassLoader = new ByteCodeClassLoader(ClassLoader.getSystemClassLoader());
	
	/**
	 * 方法用途: 设置修改类默认使用的parent classloader
	 * 操作步骤: TODO<br>
	 * @param classLoader 类加载器
	 */
	public static void setParentClassLoader(ClassLoader classLoader) {
		byteCodeClassLoader = new ByteCodeClassLoader(classLoader);
	}
	
	/**
	 * 方法用途: 获取动态加载类的classloader，此classloader的parent loader是通过 setByteCodeClassLoader 添加的，默认使用ByteCodeClassLoader加载
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static ByteCodeClassLoader getByteCodeClassLoader() {
		return byteCodeClassLoader;
	}
	
	
	
	Long lastModified = 0l;

	/**
	 * 方法用途: 加载类， 如果类文件修改过加载，如果没有修改，返回当前的<br>
	 * 操作步骤: TODO<br>
	 * @param classFileName 
	 * @throws java.lang.LinkageError: loader (instance of  com/meizu/simplify/classload/XXXClassLoader): attempted  duplicate class definition for name:"com/meizu/xxx"
	 */
	public Class<?> loadClass(String classFileName) throws IOException {
		if (!isClassModified(classFileName)) {
			return null;
		}
		/*
		 * 这里每次使用前都要new一个新的自定义ClassLoader，不能重用同一个ClassLoader实例，否则会出现LinkageError错误
		 * 原因是：之前的ClassLoader已经加载过类了，再次使用同一个ClassLoader同一个类在ClassLoader规范中是不允许
		 * 只有重新抛弃之前的ClassLoader实例及已经加载过的类，然后重新new一个ClassLoader才可以正常重新加载类。这种过程，一般在启动时，或是插件安装时会发生。虽然性能不好，但是不主要业务的功能调用，所以没有问题.
		 */
		setParentClassLoader(ClassLoader.getSystemClassLoader());//set方法中 new是必须，不能共享ClassLoader实例
		return byteCodeClassLoader.defineClass(getBytes(classFileName));
	}

	/**
	 * 方法用途: 判断是否被修改过<br>
	 * 操作步骤: TODO<br>
	 * @param classFileName 
	 */
	private boolean isClassModified(String classFileName) {
		File file = new File(classFileName);
		if (file.lastModified() > lastModified) {
			Date lastModifedDate = new Date(file.lastModified());
			String lastModifedDateStr = DateUtil.format(lastModifedDate);
			System.out.println("源class文件"+classFileName+"的字节码在"+lastModifedDateStr+"被修改");
			return true;
		}
		return false;
	}

	/**
	 * 方法用途: 从本地读取文件<br>
	 * 操作步骤: TODO<br>
	 * @param classFileName 
	 */
	private byte[] getBytes(String classFileName) throws IOException {
		File file = new File(classFileName);
		long len = file.length();
		lastModified = file.lastModified();
		byte raw[] = new byte[(int) len];
		FileInputStream fin = new FileInputStream(file);
		int r = fin.read(raw);
		try {
			if (r != len) {
				throw new UncheckedException("没有完全读取文件数据, 读取的长度：" + r + " 不等于实际文件长度: " + len);
			}
		} finally {
			fin.close();
		}
		return raw;
	}
}