package com.meizu.simplify.aop;

import org.junit.Test;

import com.meizu.simplify.ioc.Startup;

public class AopClassFileTransformerTest {

	@Test
	public void testAll() {
		Startup.start();
		long start = System.currentTimeMillis();
		System.out.println((System.currentTimeMillis()-start)+"ms");
	}
}
