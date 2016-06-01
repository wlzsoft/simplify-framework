package com.meizu.simplify.mvc;

import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.ioc.annotation.Bean;

@Bean
public class TestController extends BaseController<Model>{

	@RequestMap(path="doSomeThing")
    public Object doSomeThing(HttpServletRequest request,HttpServletResponse response,Model model) {
        System.out.println("ִtest2测试");
        return true;
    }
	@RequestMap(path="doSomeThing2")
    public Object doSomeThing2(HttpServletRequest request,HttpServletResponse response,Model model) {
        System.out.println("ִtest2测试2");
        return true;
    }

}
