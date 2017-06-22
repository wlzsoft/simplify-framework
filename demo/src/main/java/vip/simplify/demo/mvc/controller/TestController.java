package vip.simplify.demo.mvc.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.demo.mvc.entity.Test;
import vip.simplify.demo.mvc.entity.User;
import vip.simplify.demo.mvc.model.TestModel;
import vip.simplify.demo.mvc.service.TestFirstService;
import vip.simplify.demo.mvc.service.TestService;
import vip.simplify.demo.mvc.service.outter.TestOutterService;
import vip.simplify.demo.system.SystemController;
import vip.simplify.cache.CacheProxyDao;
import vip.simplify.cache.ICacheDao;
import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.redis.RedisPool;
import vip.simplify.config.info.Message;
import vip.simplify.dao.orm.BaseDao;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.mvc.annotation.AjaxAccess;
import vip.simplify.mvc.annotation.AjaxAccess.Methods;
import vip.simplify.mvc.annotation.RequestMap;
import vip.simplify.mvc.annotation.RequestParam;
import vip.simplify.utils.ClassPathUtil;
import vip.simplify.utils.StringUtil;
import vip.simplify.webcache.annotation.WebCache;
import vip.simplify.webcache.annotation.WebCache.CacheMode;

import redis.clients.jedis.ShardedJedisPool;


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

//	@Inject //注释掉，目前打开会报错，因为无实现类
//	private HttpServletRequest request;//TODO：暂未实现，正考虑是否实现的必要 
	
	@Inject
	private TestService testService;

	@Inject
	private TestFirstService testFirstService;

	@Inject
	private TestOutterService testOutterService;

	@AjaxAccess(allowOrigin = "http://ab.mezu.com,http://xx.cc.com",allowHeaders="X-Requested-With",allowMethods={Methods.Post},maxAge=30)
//	@AjaxAccess(allowOrigin = "http://ab.mezu.com")
	@RequestMap(path = "/testrestajaxjson")
	public List<Test> doRestAjaxJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		List<Test> testList = new ArrayList<>();
		testList.add(test);
		return testList;
	}
	
	@RequestMap(path = "/ajaxjsonptest")
	public String ajaxjsonptest(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc()+","+model.getName());
		testList.add(test);
		request.setAttribute("testList", testList);
		return "beetl:ajaxjsonptest";
	}
	
	@RequestMap(path = "/testvoidjson")
	public void doVoidJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc()+","+model.getName());
		testList.add(test);
		request.setAttribute("testList", testList);
	}
	
	@RequestMap(path = "/testrestjson")
	public List<Test> doRestJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		List<Test> testList = new ArrayList<>();
		System.out.println(model.getDesc() + "," + model.getName());
		testList.add(test);
		return testList;
	}
	
	@RequestMap(path = "/testjson.json")
	public Test doTestJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
//		List<Test> testList = new ArrayList<>();
//		testList.add(test);
		test.setName("test80");
		testFirstService.test();
		testOutterService.test();
		return test;
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
		Test test = testService.doSomeThing2(null);
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
//			throw new RuntimeException("数据为空");
			Message.error("数据为空");
//			Message.error("success","数据为空");
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
		Test test = testService.doSomeThing2(null);
		if(test != null) {
			request.setAttribute("userName", test.getName());
		} else {
			request.setAttribute("userName", "nologin");
		}
		return "velocity:login";
	}
	
	@RequestMap(path = "/testfreemarker/")
	public String doTestFreemarker(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		if(test != null) {
			request.setAttribute("userName", test.getName());
		} else {
			request.setAttribute("userName", "nologin");
		}
		return "freemarker:login";
	}
	
	@RequestMap(path = "/testwebsocket/")
	public String doTestWebsocket(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		request.setAttribute("userName", test.getName());
		return "velocity:websocket";
	}
	
//	@WebSocket
	@RequestMap(path = "/websocket/notice")
	public String doTestWebsocketNotice(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2(null);
		request.setAttribute("userName", test.getName());
		return "{id:1,name:'"+test.getName()+"'}";
	}	
	
	@RequestMap(path = "/testmessage/")
	public String doTestMessage(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.addTest(null);
		return "{id:1,name:'"+test.getName()+"'}";
	}
	
	ICacheDao<String, Object> cachedDao = CacheProxyDao.getCache();

	private CacheExpireTimeEnum expire = CacheExpireTimeEnum.CACHE_EXP_HOUR;
	
	@RequestMap(path = "/testvoid/")
	public String doTestVoid(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		/*if(true) {
			throw new NullPointerException("null");
		}*/
		testService.addTest(null);
		cachedDao.set("test22", expire.timesanmp(), "ioisoeijfsdjfsd");//测试连接泄漏的问题，在expire起作用后，会出问题
//		Test test = testService.doSomeThing2(null);
//		request.setAttribute("userName", test.getName());
		ShardedJedisPool pool = RedisPool.init("redis_ref_hosts");
		System.out.println("当前redis连接池状态：NumActive(当前激活数):"+pool.getNumActive()+"-NumIdle(当前空闲数):"+pool.getNumIdle()+"-NumWaiters(当前等待数):"+pool.getNumWaiters());
		return "jsp:/index";
	}
	
	@RequestMap(path = "/test/")
	public String doTest(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		testService.addTestObj(null);
		Test test = testService.doSomeThing2(null);
		if(test != null) {
			request.setAttribute("userName", test.getName());
		}
		return "uri:/index";
	}
	
	@RequestMap(path = "/testSelect/")
	public String doTestSelect(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Test> test = BaseDao.getIns(Test.class).find("select * from test_web where name=?", "lcy");
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
	 *  不支持下列方法的写法
	 *  Test参数，要使用TestModel代替，TestModel中包含Test属性即可
	 */
	/*@RequestMap(path = "/testFormEntityParam")
	public String testFormEntityParam(HttpServletRequest request, HttpServletResponse response, Test test, @RequestParam(defaultValue = "0",name="business") Integer business, @RequestParam(defaultValue = "0",name="operation") String operation, @RequestParam(defaultValue = "0",name="data") String data) {
		if (StringUtil.isEmpty(operation)) return StringUtil.format("{0}", "operation:null");
		String result = "business:"+business+",operation:"+operation+",data:"+data;//json字符串
		return result;
	}*/
	@RequestMap(path = "/testFormParam")
	public String testFormParam(HttpServletRequest request, HttpServletResponse response,/*会注入到页面中，页面处理方式*/ TestModel model, /*会注入到页面中，页面中获取方式${business}*/@RequestParam(name="business") Integer business, @RequestParam(defaultValue = "0",name="operation") String operation, @RequestParam(defaultValue = "0",name="data") String data) {
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
	
	/**
	 * 方法用途: 注意，这个例子的代码的文件下载，不能是大文件的下载，大文件下载不能一次性flush一个超大的byte数组，而是每次flush这个byte数组的一部分<br>
	 * 操作步骤: 说明：这个controller除了演示下载还有以下功能：1.演示了下载后的文件类型，文件扩展名的设置。2.演示了使用js和iframe控制下载出错，当前页面不刷新的功能，页面js例子参考download.ftl。后续做进一步封装，减少使用成本 
	 * 另外后续要增加请求连接的后缀处理方式，避免只有stream这个后缀，防止有特殊需求，需要用到其他后缀<br>
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMap(path = "/testDownloadFile.stream")
	public byte[] testDownloadFile(HttpServletRequest request, HttpServletResponse response, TestModel model)    {
		File file = new File(ClassPathUtil.getClassPath()+"test.xlsx");
		response.setContentType("application/msexcel");//封装以枚举方式存储，多个类型选择
		String fileName = "testDownloadFile";
		String extension= "xlsx";//封装以枚举方式存储，多个类型选择，并且通过路径输入扩展名，提供更简便的使用方式,后续需要重构多视图的处理
		response.setHeader("Content-disposition","inline; filename="+fileName+"."+extension);
		try {
//			Message.error("download test");
			//以下代码重点用于测试下载功能，不能做正式代码参考，因为没做兼容考虑，一旦文件较大，会导致流截断，因为空间只有1024*80
			FileInputStream fis = new FileInputStream(file);
			byte[] byteArr = new byte[1024*80];
			fis.read(byteArr);
			fis.close();
			return byteArr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 方法用途: 注意，这个例子的代码的文件下载，支持大文件下载，每次读取流中的一部分字节，然后写出并flush到客户端<br>
	 * 操作步骤: TODO <br>
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMap(path = "/testDownloadFile.stream")
	public InputStream testDownloadBigFile(HttpServletRequest request, HttpServletResponse response, TestModel model)    {
		File file = new File(ClassPathUtil.getClassPath()+"test.jpg");
		try {
			//以下代码重点用于测试下载功能，不能做正式代码参考，因为没做兼容考虑
			FileInputStream fis = new FileInputStream(file);
			return fis;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}