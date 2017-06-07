package vip.simplify.cache.service;

import vip.simplify.cache.annotation.CacheDataAdd;
import vip.simplify.cache.entity.User;
import vip.simplify.ioc.annotation.Bean;

@Bean
public class TestService {

	@CacheDataAdd(key="aaa")
    public Object doSomeThing(User bb) {
        System.out.println("ִtest2测试");
        return true;
    }
	@CacheDataAdd(key="bbb")
    public Object doSomeThing2(String aa) {
        System.out.println("ִtest2测试2");
        return true;
    }


    public static void main(String[] args) {
    	long start = System.nanoTime();
        TestService h = new TestService();
        User bb = new User();
        bb.setName("yyyyy");
        h.doSomeThing(bb);
        System.out.println(System.nanoTime()-start);
    }

}
