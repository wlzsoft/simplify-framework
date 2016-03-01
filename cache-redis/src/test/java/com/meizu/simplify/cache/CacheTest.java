package com.meizu.simplify.cache;

import org.junit.Test;

import com.meizu.simplify.cache.entity.User;
import com.meizu.simplify.cache.service.TestService;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.Startup;

public class CacheTest {

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
