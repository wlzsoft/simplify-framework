package com.meizu.simplify.classload;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class SimplifyClassLoaderExecuterTest {

	@Test
	public void testLoadClass()
			throws ClassNotFoundException, IOException, NoSuchMethodException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, InstantiationException, InterruptedException {
		String path = "E:\\workspace-git\\simplify-framework\\weaving-plugins\\target\\test-classes\\com\\meizu\\simplify\\classload\\TestService.class";
		SimplifyClassLoaderExecuter mc = new SimplifyClassLoaderExecuter();
		while (true) {
			try {
				Class<?> c = mc.loadClass(path);
				if(c != null) {
					Object o = c.newInstance();
					Method m = c.getMethod("getName");
					m.invoke(o);
					System.out.println(c.getClassLoader());
				}	
			} catch(Error e) {
				e.printStackTrace();
			}
			
			Thread.sleep(5000);
		}

	}
}