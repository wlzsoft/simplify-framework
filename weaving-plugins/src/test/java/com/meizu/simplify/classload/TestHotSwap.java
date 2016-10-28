package com.meizu.simplify.classload;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import com.meizu.simplify.classload.entity.TestEntity;
import com.meizu.simplify.classload.entity.TestUser;

public class TestHotSwap {
	public static void main(String args[])
			throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, IOException, InterruptedException {
		TestEntity testEntity = new TestEntity();
		TestUser testUser = new TestUser();
		testEntity.setTestUser(testUser);

		System.out.printf("TestEntity classLoader is %s \r\n", testEntity.getClass().getClassLoader());
		System.out.printf("TestUser classLoader is %s \r\n", testUser.getClass().getClassLoader());
		System.out.printf("TestEntity.testUser classLoader is %s \r\n", testEntity.getTestUser().getClass().getClassLoader());
		while(true) {
			ByteCodeURLClassLoader c1 = new ByteCodeURLClassLoader(
					new URL[] {new URL("file:\\E:\\workspace-git\\simplify-framework\\weaving-plugins\\target\\test-classes\\") },
					testEntity.getClass().getClassLoader());
			
			Class<?> clazz = c1.load("com.meizu.simplify.classload.entity.TestEntity");
			Object testEntityIns = clazz.newInstance();
			
			Method method2 = clazz.getMethod("getTestUser");
			Object testIns = method2.invoke(testEntityIns);
			
			System.out.printf(" reloaded TestEntity.testUser classLoader is %s", testIns.getClass().getClassLoader());
			c1.close();
			Thread.sleep(5000);
		}
	}
}