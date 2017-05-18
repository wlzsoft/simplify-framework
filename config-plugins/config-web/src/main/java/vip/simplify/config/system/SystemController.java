package vip.simplify.config.system;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.mvc.controller.BaseController;
import vip.simplify.mvc.model.Model;

/**
 * <p><b>Title:</b><i>controller基类</i></p>
 * <p>Desc: 用于权限控制，和一些公用的逻辑</p>
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
	public boolean checkPermission(HttpServletRequest request, HttpServletResponse response,String cmd, String requestUrl, T model) throws ServletException, IOException {
		return true;
	}
}
