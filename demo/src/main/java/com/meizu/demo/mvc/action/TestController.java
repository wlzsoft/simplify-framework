package com.meizu.demo.mvc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.BaseController;
import com.meizu.demo.mvc.model.ReadModel;
import com.meizu.mvc.annotation.RequestMap;
import com.meizu.mvc.annotation.RequestParam;
import com.meizu.mvc.controller.ActionForward;
import com.meizu.mvc.controller.IForward;
import com.meizu.mvc.controller.MessageForward;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.StringUtil;


@Bean
public class TestController extends BaseController<ReadModel> {

	@Resource
	private TestService testService;
	
//	@@RequestMap(path = "/d/(.+)/(.+)$")
	@RequestMap(path = "/(.+)/(.+)/tags$ /manage/discuss_(.+).shtml$ /manage/login.shtml$ /manage/$")
	public IForward doTags(HttpServletRequest request, HttpServletResponse response, final ReadModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid)  {
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
		request.setAttribute("userName", "lcy19870112@126.com");
		return new ActionForward("/index.jsp");
//		return new RedirectForward("/index.jsp");
//		return new MessageForward("测试中");
//		return new VelocityForward("/template/login.html");
//		return new RedirectForward("/manage/discuss_" + point + ".shtml");
//		return new MessageForward(StringUtil.format("{0}", "result"));
	}
	
	@RequestMap(path = "/(.+)/(.+)/w/(.+)$")
	public IForward doW(HttpServletRequest request, HttpServletResponse response, ReadModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid, @RequestParam(defaultValue = "0", param = "3") String id) throws ServletException, IOException, InterruptedException {
 
		
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
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, ReadModel model) throws ServletException, IOException {
		// 检查管理员是否登录
//		if (!"login".equals(model.getCmd()) && request.getSession().getAttribute("SystemCons.SYSTEM_MANAGE_LOGIN_FLAG") == null) {
//			response.sendRedirect("/manage/login.shtml");
//			return false;
//		}
		return true;
		// 检查用户登录标记
				/*String auth = CookiesUtil.loadCookie(SystemCons.SYSTEM_LOGIN_FLAG, request);
				if (!StringUtil.isEmpty(auth)) {
					JSONObject authjson = JSONObject.fromObject(DESStaticKey.decrypt(auth, SystemCons.SYSTEM_AUTOLOGIN_KEY));
					
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
						int uid = authjson.getInt("uid");
						String uname = StringUtil.notNull(CookiesUtil.loadCookie("_nickname", request), authjson.getString("uid"));
						if (uid < 1 || StringUtil.isEmpty(uname)) {
							request.getSession().setAttribute(SystemCons.SYSTEM_LOGIN_FLAG, null);
							return false;
						}
						
						Member member = MEMBER_DAO.findByUid(uid);
						if (member == null) {
							member = new Member();
							member.setUid(uid);
							member.setUname(StringUtil.unescape(uname)); 
							MEMBER_DAO.save(member);
						} else {
								member.setUname(StringUtil.unescape(uname)); 
								MEMBER_DAO.update(member);
						}
						request.getSession().setAttribute(SystemCons.SYSTEM_LOGIN_FLAG, member);
					} else {
						request.getSession().setAttribute(SystemCons.SYSTEM_LOGIN_FLAG, null);
						response.sendError(403, "{result:-1}");
						return false;
					}
				}
				if (StringUtil.isEmpty(auth)) {
					request.getSession().setAttribute(SystemCons.SYSTEM_LOGIN_FLAG, null);
					response.sendError(403, "{result:-1}");
					return false;
				}*/
	}
}