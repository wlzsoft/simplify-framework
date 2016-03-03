package com.meizu.demo.mvc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.entity.Test;
import com.meizu.demo.mvc.model.TestModel;
import com.meizu.demo.mvc.service.TestService;
import com.meizu.demo.system.BaseController;
import com.meizu.simplify.dao.orm.BaseDao;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.annotation.RequestMap;
import com.meizu.simplify.mvc.annotation.RequestParam;
import com.meizu.simplify.mvc.controller.ActionForward;
import com.meizu.simplify.mvc.controller.IForward;
import com.meizu.simplify.mvc.controller.JsonForward;
import com.meizu.simplify.mvc.controller.MessageForward;
import com.meizu.simplify.mvc.controller.RedirectForward;
import com.meizu.simplify.mvc.controller.VelocityForward;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.annotation.WebCache.CacheMode;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: 对参数@RequestParam的解析的测试
 *          对非正则表达式的url的映射解析的404问题的解决
 *          第一次性能耗费了800ms左右的时间，排查和解决
 *          dao模块的开发
 *          aop日志打印太过详细的问题</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午6:17:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午6:17:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TestController extends BaseController<TestModel> {

//	@Resource //注释掉，目前打开会报错，因为无实现类
	private HttpServletRequest request;//TODO：暂未实现，正考虑是否实现的必要 
	
	@Resource
	private TestService testService;
	
	@RequestMap(path = "/testredirect/")
	public IForward doTestRedirect(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		return new RedirectForward("/index.jsp");
	}
	
	@RequestMap(path = "/testredirect2/")
	public IForward doTestRedirect2(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
//		Test test = testService.doSomeThing2();
		if (request.getMethod().equals("GET")) {
			
		}
		return new RedirectForward("/testjson/");
	}
	
	@RequestMap(path = "/testvelocity/")
	@WebCache(mode=CacheMode.Mem,enableBrowerCache=true,removeSpace=false,timeToLiveSeconds=36000)
	public IForward doTestVelocity(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return new VelocityForward("/template/login.html");
	}
	@RequestMap(path = "/testjson/")
	public IForward doTestJson(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		List<Test> testList = new ArrayList<>();
		testList.add(test);
		return new JsonForward(testList);
	}
	
	@RequestMap(path = "/testmessage/")
	public IForward doTestMessage(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		Test test = testService.doSomeThing2();
		return new MessageForward("{id:1,name:'"+test.getName()+"'}");
	}
	
	@RequestMap(path = "/testvoid/")
	public IForward doTestVoid(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		testService.addTestObj(null);
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return new ActionForward("/index.jsp");
	}
	
	@RequestMap(path = "/test/")
	public IForward doTest(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		testService.addTest(null);
		Test test = testService.doSomeThing2();
		request.setAttribute("userName", test.getName());
		return new ActionForward("/index.jsp");
	}
	
	@RequestMap(path = "/testSelect/")
	public IForward doTestSelect(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Test> test = BaseDao.getIns(Test.class).find("select * from test_web where name=?","lcy");
		request.setAttribute("testList", test);
		return new ActionForward("/testList.jsp");
	}
	
	@RequestMap(path = "/testSelect2/")
	public IForward adoTestSelect2(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Test> test = BaseDao.getIns(Test.class).find("select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=?","lcy");
		request.setAttribute("testList", test);
		return new ActionForward("/testList.jsp");
	}
	
	@RequestMap(path = "/testSelect3/")
	public IForward adoTestSelect3(HttpServletRequest request, HttpServletResponse response, TestModel model)  {
		List<Map<String,Object>> test = BaseDao.getInsMap().find("select test_web.*,user.name as createName from test_web inner join user on test_web.createId=user.id where test_web.name=?","lcy");
		request.setAttribute("testList", test);
		return new ActionForward("/testList.jsp");
	}
	
	@RequestMap(path = "/(.+)/(.+)/demo/(.+)$ /(.+)/(.+)/demo2$ /demo/demo_(.+).html$ /demo/demo.html$ /demo/$ /demo/(.+)/(.+)$")
	public IForward doDemo(HttpServletRequest request, HttpServletResponse response, TestModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid, @RequestParam(defaultValue = "0", param = "3") String id) throws ServletException, IOException, InterruptedException {
 
		
		// 检查是否id为空
		if (StringUtil.isEmpty(id)) return new MessageForward(StringUtil.format("{0}", "id:null"));
		
		// 存在脚本生成地址，无法使用加密 
		//if (!enc.equalsIgnoreCase(MD5.calcMD5(StringUtil.format("{0}{1}", Pointers.getKey(pid), id)))) return new ErrorForward(getMsg("VERIFY.FAILED"));

//		HttpServletRequest request= HttpRequestPool.getRequest();//TODO 获取当前请求(request池中获取request对象，而无需传参的方式 ),通过ThreadLocal来保留request变量
		String domain = null;//CookiesUtil.getDomain(request);
		// 判断当前访问站点来源
//		String sDomain = request.getServerName();
//		if(sDomain.indexOf("meizu.com") > 0)  {
//		}
//		resultStr = resultStr.replaceAll("\r\n", "<br/>").replaceAll("\\s", "&nbsp;");
		
		
		String isflag = request.getParameter("isflag");
		String result = "";
		if (model.getScript() == 1) { 
			return  new MessageForward(StringUtil.format("<script>document.domain='{0}';{1}({2})</script>", domain, model.getJsonp(), result.toString()));
		} else if(model.getScript() == 2){
			return  new MessageForward(StringUtil.format("{0}", result.toString()));
		} else {
			return  new MessageForward(StringUtil.format("{0}({1})", model.getJsonp(), result.toString()));
		}

	}
	

	/**
	 * 检查权限
	 */
	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, TestModel model) throws ServletException, IOException {
		
		
		/*if (!"login".equals(model.getCmd())) {
			response.sendRedirect("/test/login.html");
			return false;
		}
		String auth = CookiesUtil.loadCookie("SYSTEM_LOGIN_FLAG", request);
		JSONObject authjson = JSONObject.fromObject(DESStaticKey.decrypt(auth, "SYSTEM_AUTOLOGIN_KEY"));
		if (authjson != null && ObjectUtils.isInt(authjson.get("uid"))) {
			model.setFromSite(authjson.optString("fromid","0"));
			String[] domainArr = StringUtil.notNull(request.getServerName()).split("\\.");
			String curDomain = "";
			if(domainArr != null&&domainArr.length>1) {
				 curDomain = domainArr[1];
			}
			if(authjson.containsKey("domain") && !curDomain.equalsIgnoreCase(authjson.getString("domain"))) {
				return false;
			}
			StringUtil.unescape(uname);
		 } else {
			response.sendError(403, "{result:-1}");
			return false;
		 }
		*/
		
		return true;
	}
}