package vip.simplify.classload;

/**
 * <p><b>Title:</b><i>自定义ClassLoader来实现自定义类字节码加载-针对字节码修改后的热部署的实现</i></p>
* <p>Desc: TODO </p>
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
public class ByteCodeClassLoader extends ClassLoader {
    
	public ByteCodeClassLoader(ClassLoader parent) {
		super(parent);
	}
	
	public Class<?> defineClass(String name, byte[] byteCode) throws ClassFormatError{
		return super.defineClass(name, byteCode, 0, byteCode.length);
	}
    public Class<?> defineClass(byte[] byteCode) {
        return this.defineClass(null, byteCode);
    }

    /**
     * 只有 parent的classloader 是  ClassLoader.getSystemClassLoader() 的时候，才需要定制下面 方法,否则使用容器自己的ClassLoader，比如tomcat或是jetty的ClassLoader
     */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		return super.findClass(name);
	}

	/**
     * 只有 parent的classloader 是  ClassLoader.getSystemClassLoader() 的时候，才需要定制下面 方法,否则使用容器自己的ClassLoader，比如tomcat或是jetty的ClassLoader
     */
	@Override
	protected String findLibrary(String libname) {
		return super.findLibrary(libname);
	}
	
	public Class<?> load(String name) throws ClassNotFoundException {
		return load(name, false,null);
	}

	public Class<?> load(String name, boolean resolve,byte[] byteCode) throws ClassNotFoundException {
		if (null != super.findLoadedClass(name)) {
			return reload(name, resolve,byteCode);
		}
		
//		Class<?> clazz = findClass(name);
		Class<?> clazz = this.defineClass(byteCode);//Class.forName("vip.simplify.demo.mvc.service.TestService");
		if (resolve) {
			super.resolveClass(clazz);
		}
		return clazz;
	}

	public Class<?> reload(String name, boolean resolve,byte[] clazz) throws ClassNotFoundException {
		ByteCodeClassLoader hscl = new ByteCodeClassLoader(super.getParent());
		Class<?> classList = hscl.load(name, resolve,clazz);
		return classList;
	}
}