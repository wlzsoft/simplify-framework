package com.meizu.stresstester.test;

import org.junit.Test;

import com.meizu.stresstester.StressTestUtils;
import com.meizu.stresstester.core.StressTask;

public class MainTest {

	@Test
	public void test() {
		StressTestUtils.testAndPrint(100, 1000, new StressTask() {

			@Override
			public Object doTask() throws Exception {
				System.out.println("Do my task.");
				return null;
			}
		});
	}

}
