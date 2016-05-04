package com.meizu.authfilter.interfacefilter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.mvc.controller.BaseController;
import com.meizu.simplify.mvc.model.Model;

/**
 * <p><b>Title:</b><i>app接口认证</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月19日 上午11:30:10</p>
 * <p>Modified By:Administrator-</p>
 * <p>Modified Date:2016年4月19日 上午11:30:10</p>
 * @author <a href="mailto:wanglizong@meizu.com" title="邮箱地址">wanglizong</a>
 * @version Version 0.1
 *
 */
public class AppInterfaceAuth <T extends Model> extends BaseController<T> {

	@Override
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response, T t)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		return super.checkPermission(request, response, t);
	}
	
}
