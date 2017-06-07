package vip.simplify.cache;

import org.junit.Test;

import vip.simplify.cache.entity.User;
import vip.simplify.cache.service.TestService;
import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;

public class CacheTest {

	@Test
	public void testAll() {
		Startup.start();
		long start = System.nanoTime();
		User bb = new User();
        bb.setName("yyyyy");
		BeanFactory.getBean(TestService.class).doSomeThing(bb);
		System.out.println((System.nanoTime()-start)+"ns");
	}
}
