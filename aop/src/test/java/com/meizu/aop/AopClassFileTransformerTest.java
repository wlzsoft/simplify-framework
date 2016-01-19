package com.meizu.aop;

import org.junit.Test;

import com.meizu.aop.service.TestService;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;

import javassist.CannotCompileException;
import javassist.CtClass;

public class AopClassFileTransformerTest {

	@Test
	public void test() {
		CtClass cc = new AopClassFileTransformer().buildClazz("com/meizu/aop/service/TestService");
//		new AopClassFileTransformer().transformInit("com/meizu/aop/service/TestService");
		long start = System.currentTimeMillis();
		try {
			 User bb = new User();
		        bb.setName("yyyyy");
			((TestService)(cc.toClass().newInstance())).doSomeThing(bb);
		} catch (InstantiationException | IllegalAccessException | CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		new TestService().doSomeThing();
		System.out.println(System.currentTimeMillis()-start);
	}
	@Test
	public void testAll() {
		Startup.start();
		long start = System.currentTimeMillis();
		User bb = new User();
        bb.setName("yyyyy");
		BeanFactory.getBean(TestService.class).doSomeThing(bb);
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
}
