package com.meizu.simplify.mvc;

import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestService {

    public Object doSomeThing(User bb) {
        System.out.println("ִtest2测试");
        return true;
    }
    public Object doSomeThing2(String aa) {
        System.out.println("ִtest2测试2");
        return true;
    }
}
