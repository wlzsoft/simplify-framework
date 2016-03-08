package com.meizu.demo.system;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.demo.mvc.model.TestModel;
import com.meizu.simplify.mvc.controller.IForward;
import com.meizu.simplify.mvc.directives.Model;
import com.meizu.simplify.mvc.directives.SecurityContoller;

/**
 * <p><b>Title:</b><i>controller基类</i></p>
 * <p>Desc: 目前这个类没有实质的作用，具体业务开发时，部分业务公用代码移植到这里</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午2:34:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午2:34:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class BaseController<T extends Model> extends SecurityContoller<T> {
	
	@Override
	public final void process(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		request.setAttribute("url", request.getRequestURI());
		super.process(request, response);
	}
	
	@Override
	public final IForward execute(final HttpServletRequest request, final HttpServletResponse response, final T t) throws ServletException, IOException {
		return super.execute(request, response, t);
	}
	
	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T model) throws ServletException, IOException {
		
		
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
