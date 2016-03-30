package com.meizu.simplify.mvc.view;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.ioc.annotation.DefaultBean;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>模版处理回调接口</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@DefaultBean
public interface ITemplate {
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException;
}
