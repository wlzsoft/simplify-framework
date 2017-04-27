package vip.simplify.aop;

import org.junit.Test;

import vip.simplify.ioc.Startup;

public class AopClassFileTransformerTest {

	@Test
	public void testAll() {
		Startup.start();
		long start = System.currentTimeMillis();
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
}
