package com.meizu.mvc;

import com.meizu.mvc.annotation.RequestMap;
import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestController {

	@RequestMap(path="doSomeThing")
    public Object doSomeThing(User bb) {
        System.out.println("ִtest2测试");
        return true;
    }
	@RequestMap(path="doSomeThing2")
    public Object doSomeThing2(String aa) {
        System.out.println("ִtest2测试2");
        return true;
    }

}
