package vip.simplify.mvc;

import vip.simplify.mvc.annotation.RequestMap;
import vip.simplify.mvc.controller.BaseController;
import vip.simplify.mvc.model.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.ioc.annotation.Bean;

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
