package com.meizu.aop.service;

import com.meizu.cache.annotation.CacheDataAdd;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestService {

	@CacheDataAdd(key="aaa")
    public Object doSomeThing(String bb) {
        System.out.println("ִtest2测试");
        return true;
    }
	@CacheDataAdd(key="bbb")
    public Object doSomeThing2(String aa) {
        System.out.println("ִtest2测试2");
        return true;
    }


    public static void main(String[] args) {
    	long start = System.currentTimeMillis();
        TestService h = new TestService();
        h.doSomeThing("cc");
        System.out.println(System.currentTimeMillis()-start);
    }

}
