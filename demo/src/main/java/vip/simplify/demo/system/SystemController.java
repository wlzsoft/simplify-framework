package vip.simplify.demo.system;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import vip.simplify.encrypt.symmetriccryptography.des.DESEncrypt;
import vip.simplify.mvc.controller.BaseController;
import vip.simplify.mvc.model.Model;
import vip.simplify.utils.ObjectUtil;
import vip.simplify.utils.StringUtil;

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
	public final void process(final HttpServletRequest request, final HttpServletResponse response,String requestUrl,String requestMethodName,boolean isStatic,String[] urlparams)  {
		/*String path = request.getContextPath();
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";*/
		request.setAttribute("url", request.getRequestURI());
		super.process(request, response,requestUrl,requestMethodName,isStatic,urlparams);
	}
	
	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response,String cmd, String requestUrl, T model) throws ServletException, IOException {
		
		boolean ischeck = true;
		if(ischeck) {
			return true;
		}
		
		if (!"login".equals(cmd)) {
			response.sendRedirect("/template/login.html");
			return false;
		}
		String auth = "";//CookiesUtil.loadCookie("SYSTEM_LOGIN_FLAG", request);
		JSONObject authjson = JSONObject.parseObject(DESEncrypt.hexToDecryptECB(auth, "SYSTEM_AUTOLOGIN_KEY","UTF-8"));
		if (authjson != null && ObjectUtil.isInt(authjson.get("uid"))) {
			String[] domainArr = StringUtil.parseString(request.getServerName(),"").split("\\.");
			String curDomain = "";
			if(domainArr != null&&domainArr.length>1) {
				 curDomain = domainArr[1];
			}
			if(authjson.containsKey("domain") && !curDomain.equalsIgnoreCase(authjson.getString("domain"))) {
				return false;
			}
//			Object uname = "";
//			StringUtil.unescape(uname);
		 } else {
			response.sendError(403, "{result:-1}");
			return false;
		 }
		
		
		return true;
	}
}
