package com.meizu.aop;

import org.junit.Test;

import com.meizu.aop.service.TestService;

import javassist.CannotCompileException;
import javassist.CtClass;

public class AopClassFileTransformerTest {

	@Test
	public void test() {
		CtClass cc = new AopClassFileTransformer().buildClazz("com/meizu/aop/service/TestService");
//		new AopClassFileTransformer().transformInit("com/meizu/aop/service/TestService");
		long start = System.currentTimeMillis();
		try {
			((TestService)(cc.toClass().newInstance())).doSomeThing();
		} catch (InstantiationException | IllegalAccessException | CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		new TestService().doSomeThing();
		System.out.println(System.currentTimeMillis()-start);
	}
}
