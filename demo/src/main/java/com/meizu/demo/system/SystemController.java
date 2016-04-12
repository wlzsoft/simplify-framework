package com.meizu.demo.system;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.meizu.simplify.encrypt.des.DESEncrypt;
import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.model.Model;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.StringUtil;

/**
 * <p><b>Title:</b><i>controller基类</i></p>
 * <p>Desc: 目前这个类没有实质的作用，具体业务开发时，部分业务公用代码移植到这里</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午2:34:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午2:34:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class SystemController<T extends Model> extends BaseController<T> {
	
	@Override
	public final void process(final HttpServletRequest request, final HttpServletResponse response,String requestUrl) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
		request.setAttribute("url", request.getRequestURI());
		super.process(request, response,requestUrl);
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, T model,String requestUrl)
			throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			ServletException {
		super.execute(request, response, model,requestUrl);
	}

	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T model) throws ServletException, IOException {
		
		
		if(true) {
			return true;
		}
		
		if (!"login".equals(model.getCmd())) {
			response.sendRedirect("/template/login.html");
			return false;
		}
		String auth = "";//CookiesUtil.loadCookie("SYSTEM_LOGIN_FLAG", request);
		JSONObject authjson = JSONObject.parseObject(DESEncrypt.decrypt(auth, "SYSTEM_AUTOLOGIN_KEY"));
		if (authjson != null && ObjectUtil.isInt(authjson.get("uid"))) {
			String[] domainArr = StringUtil.parseString(request.getServerName(),"").split("\\.");
			String curDomain = "";
			if(domainArr != null&&domainArr.length>1) {
				 curDomain = domainArr[1];
			}
			if(authjson.containsKey("domain") && !curDomain.equalsIgnoreCase(authjson.getString("domain"))) {
				return false;
			}
			Object uname = "";
//			StringUtil.unescape(uname);
		 } else {
			response.sendError(403, "{result:-1}");
			return false;
		 }
		
		
		return true;
	}
}
