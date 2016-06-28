package com.meizu.demo.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.demo.mvc.entity.User;
import com.meizu.demo.mvc.model.TestModel;
import com.meizu.demo.mvc.service.TestService;
import com.meizu.demo.system.SystemController;
import com.meizu.simplify.config.info.Message;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.annotation.AjaxAccess;
import com.meizu.simplify.mvc.annotation.AjaxAccess.Methods;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.annotation.WebCache.CacheMode;


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
public class TestController extends SystemController<TestModel> {

//	@Resource //注释掉，目前打开会报错，因为无实现类
//	private HttpServletRequest request;//TODO：暂未实现，正考虑是否实现的必要 
	
	@Resource
	private TestService testService;
	
	@AjaxAccess(allowOrigin = "http://ab.mezu.com",allowHeaders="X-Requested-With",allowMethods={Methods.Post},maxAge=30)
//	@AjaxAccess(allowOrigin = "http://ab.mezu.com")
	@RequestMap(path = "/testrestajaxjson")
	public List<Test> doRestAjaxJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		List<Test> testList = new ArrayList<>();
		testList.add(test);
		return testList;
	}
	
	@RequestMap(path = "/ajaxjsonptest")
	public String ajaxjsonptest(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc()+","+model.getName());
		testList.add(test);
		request.setAttribute("testList", testList);
		return "beetl:ajaxjsonptest";
	}
	
	@RequestMap(path = "/testvoidjson")
	public void doVoidJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc()+","+model.getName());
		testList.add(test);
		request.setAttribute("testList", testList);
	}
	
	@RequestMap(path = "/testrestjson")
	public List<Test> doRestJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc()+","+model.getName());
		testList.add(test);
		return testList;
	}
	
	@RequestMap(path = "/testbeetl/")
	public String doTestBeetl(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		
		User user = new User();
		user.setName("lcyc123");
		List<Test> testList = new ArrayList<>();
		Test test = new Test();
		test.setName("syk");
		test.setFid(1);
		testList.add(test);
		request.setAttribute("user", user);
		request.setAttribute("tests", testList);
		return "beetl:pagefunction";
	}
	
	@RequestMap(path = "/testhttl/")
	public String doTestHttl(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		 
		User user = new User();
		user.setName("lcy");
		List<Test> testList = new ArrayList<>();
		Test test = new Test();
		test.setName("sykcsss");
		test.setFid(1);
		testList.add(test);
		request.setAttribute("user", user);
		request.setAttribute("tests", testList);
		return "httl:tests";
	}
	@RequestMap(path = "/testredirect/")
	public String doTestRedirect(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		System.out.println(test.getName());
		return "redirect:/index";
	}
	
	@RequestMap(path = "/testredirect2/")
	public String doTestRedirect2(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
//		Test test = testService.doSomeThing2();
		if (request.getMethod().equals("GET")) {
			
		}
		return "redirect:/test/testjson.json";
	}
	@RequestMap(path = "/testvelocity3/")//TODO 这个例子没测试通过，因为异常处理模版用的velocity模版，没有捕获异常
	public String doTestVelocity3(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		if(true) {
			Message.error("success","数据为空");
//			Message.info("数据为空");//1.直接错误信息方式
//			Message.info("success","数据为空");//1.读取配置文件信息方式
//			Message.warn("success","数据为空");
		}
		return "velocity:login";
	}
	/**
	 * 方法用途: 针对webserver模块的测试<br>
	 * 操作步骤: 基于webserver时，把下面注释掉代码打开<br>
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMap(path = "/testvelocity2/")
	public String doTestVelocity2(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		/*String userName = request.getParameter("userName");
		HttpSession session = request.getSession();
		if (userName != null && userName.equals("admin")) {//服务器内部跳转到首页
			request.setAttribute("userName", userName);
			request.setRequestURI("/");//服务器内部跳转
			session.setAttribute("admin", userName);
			HttpRoute.route(request, response);
		}*/
		return "velocity:login";
	}
	@WebCache(mode=CacheMode.Mem,enableBrowerCache=true,removeSpace=true,timeToLiveSeconds=36000)
	@RequestMap(path = "/testvelocity/")
	public String doTestVelocity(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		if(test != null) {
			request.setAttribute("userName", test.getName());
		} else {
			request.setAttribute("userName", "nologin");
		}
		return "velocity:login";
	}
	
	@RequestMap(path = "/testfreemarker/")
	public String doTestFreemarker(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		if(test != null) {
			request.setAttribute("userName", test.getName());
		} else {
			request.setAttribute("userName", "nologin");
		}
		return "freemarker:login";
	}
	
	@RequestMap(path = "/testwebsocket/")
	public String doTestWebsocket(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return "velocity:websocket";
	}
	
//	@WebSocket
	@RequestMap(path = "/websocket/notice")
	public String doTestWebsocketNotice(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return "{id:1,name:'"+test.getName()+"'}";
	}	
	@RequestMap(path = "/testjson.json")
	public Test doTestJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
//		List<Test> testList = new ArrayList<>();
//		testList.add(test);
		return test;
	}
	
	@RequestMap(path = "/testmessage/")
	public String doTestMessage(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.addTest(null);
		return "{id:1,name:'"+test.getName()+"'}";
	}
	
	@RequestMap(path = "/testvoid/")
	public String doTestVoid(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		testService.addTestObj(null);
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return "jsp:/index";
	}
	
	@RequestMap(path = "/test/")
	public String doTest(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		testService.addTest(null);
		Test test = testService.doSomeThing2();
		if(test != null) {
			request.setAttribute("userName", test.getName());
		}
		return "uri:/index";
	}
	
	@RequestMap(path = "/testSelect/")
	public String doTestSelect(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Test> test = BaseDao.getIns(Test.class).find("select * from test_web where name=?","lcy");
		request.setAttribute("testList", test);
		return "jsp:/testList";
	}
	
	@RequestMap(path = "/testSelect2/")
	public String adoTestSelect2(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Test> test = BaseDao.getIns(Test.class).find("select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=?","lcy");
		request.setAttribute("testList", test);
		return "jsp:/testList";
	}
	
	@RequestMap(path = "/testSelect3/")
	public String adoTestSelect3(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Map<String,Object>> test = BaseDao.getInsMap().find("select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=?","lcy");
		request.setAttribute("testList", test);
		return "jsp:/testList";
	}
	@RequestMap(path = "/testSqlTemplate/")
	public String testSqlTemplate(HttpServletRequest request, HttpServletResponse response, TestModel model)   {
		try {
			testService.testSqlTemplate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "beetl:ajaxjsonptest";
	}
	
	/**
	 * 方法用途: 不支持写法<br>
	 * 操作步骤: Test参数，要使用TestModel代替，TestModel中包含Test属性即可<br>
	 * @param request
	 * @param response
	 * @param test
	 * @param business
	 * @param operation
	 * @param data
	 * @return
	 */
	/*@RequestMap(path = "/testFormEntityParam")
	public String testFormEntityParam(HttpServletRequest request, HttpServletResponse response, Test test, @RequestParam(defaultValue = "0",name="business") Integer business, @RequestParam(defaultValue = "0",name="operation") String operation, @RequestParam(defaultValue = "0",name="data") String data) {
		if (StringUtil.isEmpty(operation)) return StringUtil.format("{0}", "operation:null");
		String result = "business:"+business+",operation:"+operation+",data:"+data;//json字符串
		return result;
	}*/
	@RequestMap(path = "/testFormParam")
	public String testFormParam(HttpServletRequest request, HttpServletResponse response, TestModel model, @RequestParam(defaultValue = "0",name="business") Integer business, @RequestParam(defaultValue = "0",name="operation") String operation, @RequestParam(defaultValue = "0",name="data") String data) {
		if (StringUtil.isEmpty(operation)) return StringUtil.format("{0}", "operation:null");
		String result = "business:"+business+",operation:"+operation+",data:"+data;//json字符串
		return result;
	}
	@RequestMap(path = {"/(.+)/(.+)/demo/(.+)$","/(.+)/(.+)/demo2$","/demo/demo_(.+).html$","/demo/demo.html$","/demo/$","/demo/(.+)/(.+)$"})
	public String testUrlRestParam(HttpServletRequest request, HttpServletResponse response, TestModel model, /*@RequestParam(defaultValue = "0", index = 0) String enc,*/ @RequestParam(defaultValue = "0", index = 1) Integer business, @RequestParam(defaultValue = "0", index = 2) String operation, @RequestParam(defaultValue = "0", index = 3) String data)  {
 
		Test test = model.getTest();
		if(test!= null) {
			System.out.println("测试类名称:"+test.getName());
		}
		// 检查是否operation为空
		if (StringUtil.isEmpty(operation)) return StringUtil.format("{0}", "operation:null");
		
		// 存在脚本生成地址，无法使用加密 
		//if (!enc.equalsIgnoreCase(MD5.calcMD5(StringUtil.format("{0}{1}", Pointers.getKey(business), operation)))) return new ErrorView(getMsg("VERIFY.FAILED"));

//		HttpServletRequest request= HttpRequestPool.getRequest();//TODO 获取当前请求(request池中获取request对象，而无需传参的方式 ),通过ThreadLocal来保留request变量
//		String domain = CookiesUtil.getDomain(request);
		// 判断当前访问站点来源
//		String sDomain = request.getServerName();
//		if(sDomain.indexOf("meizu.com") > 0)  {
//		}
		
//		String isflag = request.getParameter("isflag");
		String result = "business:"+business+",operation:"+operation+",data:"+data;//json字符串
//		result = result.replaceAll("\r\n", "<br/>").replaceAll("\\s", "&nbsp;");
		return result;
	}
}