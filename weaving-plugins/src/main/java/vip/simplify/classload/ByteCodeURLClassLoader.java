package vip.simplify.classload;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p><b>Title:</b><i>自定义ClassLoader来实现自定义类字节码加载-针对字节码修改后的热部署的实现</i></p>
* <p>Desc: 1.只需要一个classloader实例，可以重复载入同名类而不会报异常：java.lang.LinkageError: loader (instance of  vip/simplify/classload/XXXClassLoader): attempted  duplicate class definition for name:"vip/simplify/xxx"
*          2.修改了双亲委派,在ExtClassLoader和AppClassLoader之前重写了URLClassLoader用来重新加载类。 需要在ByteCodeURLClassLoader类中维护重载后的类的成员变量状态 </p>
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
public class ByteCodeURLClassLoader extends URLClassLoader {

	public ByteCodeURLClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	public Class<?> defineClass(String name, byte[] byteCode) throws ClassFormatError{
		return super.defineClass(name, byteCode, 0, byteCode.length);
	}
    public Class<?> defineClass(byte[] byteCode) {
        return this.defineClass(null, byteCode);
    }
	@Override
	public InputStream getResourceAsStream(String name) {
		return super.getResourceAsStream(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	public Class<?> load(String name) throws ClassNotFoundException {
		return load(name, false);
	}

	public Class<?> load(String name, boolean resolve) throws ClassNotFoundException {
		if (null != super.findLoadedClass(name)) {
			return reload(name, resolve);
		}
		Class<?> clazz = super.findClass(name);
		if (resolve) {
			super.resolveClass(clazz);
		}
		return clazz;
	}

	public Class<?> reload(String name, boolean resolve) throws ClassNotFoundException {
		ByteCodeURLClassLoader hscl = new ByteCodeURLClassLoader(super.getURLs(), super.getParent());
		Class<?> classList = hscl.load(name, resolve);
		try {
			hscl.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classList;
	}
}
