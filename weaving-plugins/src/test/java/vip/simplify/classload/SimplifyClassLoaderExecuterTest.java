package vip.simplify.classload;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

public class SimplifyClassLoaderExecuterTest {

	public static void main(String[] args)
			throws ClassNotFoundException, IOException, NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException, InterruptedException {
//		String path = "E:\\workspace-git\\simplify-framework\\weaving-plugins\\target\\test-classes\\com\\meizu\\simplify\\classload\\TestService.class";
		ByteCodeClassLoader mc = new ByteCodeClassLoader(Thread.currentThread().getContextClassLoader());
		while (true) {
			try {
//				Class<?> c = mc.loadClass(path);
				Class<?> c = mc.load("TestService");
				if(c != null) {
					Object o = c.newInstance();
					Method m = c.getMethod("getName");
					m.invoke(o);
					System.out.println(c.getClassLoader());
				}	
			} catch(Error e) {
				e.printStackTrace();
			}
			TimeUnit.SECONDS.sleep(5);
		}

	}
}