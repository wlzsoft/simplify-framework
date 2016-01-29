package com.meizu.demo.mvc.service;

import com.meizu.demo.mvc.entity.User;
import com.meizu.simplify.cache.annotation.CacheDataAdd;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestService {

	@CacheDataAdd(key="aaa")
    public Object doSomeThing(User bb) {
        System.out.println("2test2d测试");
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
        User bb = new User();
        bb.setName("yyyyy");
        h.doSomeThing(bb);
        System.out.println(System.currentTimeMillis()-start);
    }

}
