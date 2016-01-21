package com.meizu.demo.mvc.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.BaseServlet;
import com.meizu.demo.mvc.model.ReadModel;
import com.meizu.mvc.annotation.RequestMap;
import com.meizu.mvc.annotation.RequestParam;
import com.meizu.mvc.controller.ActionForward;
import com.meizu.mvc.controller.IForward;

public class ReadAction extends BaseServlet<ReadModel> {
	private static final long serialVersionUID = 4769858916633847282L;

//	@@RequestMap(path = "/d/(.+)/(.+)$")
	@RequestMap(path = "/(.+)/(.+)/tags$ /manage/discuss_(.+).shtml$ /manage/login.shtml$ /manage/$")
	public IForward doTags(HttpServletRequest request, HttpServletResponse response, final ReadModel model, @RequestParam(defaultValue = "0", param = "1") String enc, @RequestParam(defaultValue = "0", param = "2") String pid)  {
//		String domain = CookiesUtil.getDomain(request);
//			return new MessageForward(StringUtil.format("{0}({1})", model.getJsonp(), result.toString()));
		// 判断当前访问站点来源
		String sDomain = request.getServerName();
		if(sDomain.indexOf("meizu.com") > 0)  {
		}
//		resultStr = resultStr.replaceAll("\r\n", "<br/>").replaceAll("\\s", "&nbsp;");
		if (model.getScript() == 1) { 
//			return new MessageForward(StringUtil.format("<script>document.domain='{0}';{1}({2})</script>",domain , model.getJsonp(), result.toString()));
		} else {
//			return new MessageForward(StringUtil.format("{0}({1})", model.getJsonp(), result.toString()));
		}
		return new ActionForward("/read.jsp");
//		return new MessageForward("请查询部分数据进行删除，不能删除这个范围的数据");
//		return new VelocityForward("/manage/login.html");
//		return new RedirectForward("/manage/discuss_" + point + ".shtml");
//		return new MessageForward(StringUtil.format("{0}", "result"));
	}
	

	/**
	 * 检查权限
	 */
	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, ReadModel model) throws ServletException, IOException {
		// 检查管理员是否登录
		if (!"login".equals(model.getCmd()) && request.getSession().getAttribute("SystemCons.SYSTEM_MANAGE_LOGIN_FLAG") == null) {
			response.sendRedirect("/manage/login.shtml");
			return false;
		}
		return true;
	}
}