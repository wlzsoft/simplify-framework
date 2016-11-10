package com.meizu.simplify.datasource;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.test.SimplifyJUnit4ClassRunner;

@RunWith(SimplifyJUnit4ClassRunner.class)
@Bean
public class HostRouteServiceTest {
	@Test
	public void test() {
		new HostRouteService().switchHost();
	}
}
