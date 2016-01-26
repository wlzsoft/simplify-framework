package com.meizu.demo.mvc.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.entity.User;
import com.meizu.demo.mvc.model.TestModel;
import com.meizu.demo.mvc.service.TestService;
import com.meizu.demo.system.BaseController;
import com.meizu.mvc.annotation.RequestMap;
import com.meizu.mvc.annotation.RequestParam;
import com.meizu.mvc.controller.ActionForward;
import com.meizu.mvc.controller.IForward;
import com.meizu.mvc.controller.MessageForward;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.StringUtil;


@Bean
public class TestController extends BaseController<TestModel> {

	@Resource
	private TestService testService;
	
	@RequestMap(path = "/(.+)/(.+)/testpage$ /test/demo_(.+).html$ /test/login.html$ /test/$ /testa/(.+)/(.+)$")
	public IForward doTest(HttpServletRequest request, HttpServletResponse response, final TestModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid)  {
//		String domain = CookiesUtil.getDomain(request);
//			return new MessageForward(StringUtil.format("{0}({1})", model.getJsonp(), result.toString()));
		// 判断当前访问站点来源
//		String sDomain = request.getServerName();
//		if(sDomain.indexOf("meizu.com") > 0)  {
//		}
//		resultStr = resultStr.replaceAll("\r\n", "<br/>").replaceAll("\\s", "&nbsp;");
//		if (model.getScript() == 1) { 
//			return new MessageForward(StringUtil.format("<script>document.domain='{0}';{1}({2})</script>",domain , model.getJsonp(), result.toString()));
//		} else {
//			return new MessageForward(StringUtil.format("{0}({1})", model.getJsonp(), result.toString()));
//		}
		System.out.println(testService+"HHHHHHHHHHHHHHHHHHHHHHHH");
		if(testService != null) {
			 User bb = new User();
		        bb.setName("yyyyy2");
			testService.doSomeThing2("basdfsd");
		}
		request.setAttribute("userName", "lcy19870112@126.com");
		return new ActionForward("/index.jsp");
//		return new RedirectForward("/index.jsp");
//		return new MessageForward("测试中");
//		return new VelocityForward("/template/login.html");
//		return new RedirectForward("/test/demo_" + point + ".html");
//		return new MessageForward(StringUtil.format("{0}", "result"));
	}
	
	@RequestMap(path = "/(.+)/(.+)/demo/(.+)$")
	public IForward doDemo(HttpServletRequest request, HttpServletResponse response, TestModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid, @RequestParam(defaultValue = "0", param = "3") String id) throws ServletException, IOException, InterruptedException {
 
		
		// 检查是否id为空
		if (StringUtil.isEmpty(id)) return new MessageForward(StringUtil.format("{0}", "id:null"));
		
		// 存在脚本生成地址，无法使用加密 
		//if (!enc.equalsIgnoreCase(MD5.calcMD5(StringUtil.format("{0}{1}", Pointers.getKey(pid), id)))) return new ErrorForward(getMsg("VERIFY.FAILED"));

		String domain = null;// CookiesUtil.getDomain(request);
//		model.setDomain(request.getServerName());
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