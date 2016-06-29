package com.meizu.demo.mvc.controller;

import static com.meizu.simplify.mvc.controller.Controller.get;

import com.meizu.demo.mvc.model.TestModel;
import com.meizu.demo.mvc.service.TestService;
import com.meizu.demo.system.SystemController;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.annotation.RequestMap;


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
@RequestMap(path = "/test")
public class TestSimpleController extends SystemController<TestModel> {
	@Resource
	private TestService testService;
{
	
    get("/test", (req, res) -> {
    	System.out.println("get testttttttttttttttttttttttttttttttt");
        req.getParameter("test");
        return "simpleControll test";
    });
}}