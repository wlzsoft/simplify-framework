package vip.simplify.demo.mvc.controller;

import static vip.simplify.mvc.controller.Controller.get;

import vip.simplify.demo.mvc.entity.Test;
import vip.simplify.demo.mvc.service.TestService;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午6:17:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午6:17:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestSimpleController {
	@Inject
	private TestService testService;
{
    get("/test", (req, res) -> {
    	Test test = testService.doSomeThing2(null);
    	System.out.println("get testttttttttttttttttttttttttttttttt:"+test.getName());
        req.getParameter("test");
        return test;
    });
    
    get("/test/simple", (req,res)-> testService.doSomeThing2(null));
}}