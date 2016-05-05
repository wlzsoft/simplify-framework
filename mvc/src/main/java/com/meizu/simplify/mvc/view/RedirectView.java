package com.meizu.simplify.mvc.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:29</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:29</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedirectView  {
	
	
	public static void exe(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		response.sendRedirect(templateUrl);
	}
	
	public static void exe(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl, HashMap<String, String> paramMap) throws ServletException, IOException {
		PropertiesConfig config = BeanFactory.getBean(PropertiesConfig.class);
		if(paramMap == null) {
			return;
		}
		Set<Entry<String, String>> paramSet = paramMap.entrySet();
		if (paramSet.size() > 0) {
			StringBuffer w = new StringBuffer("<html><head></head><body onload=\"form1.submit()\"><form id=\"form1\" method=\"post\" action=\"" + templateUrl + "\">");
			for (Entry<String, String> paramEntry : paramSet) {
				w.append("<input type=\"hidden\"  name=\"" + paramEntry.getKey() + "\" value=\"" + paramEntry.getValue() + "\"/>");
			}
			w.append("</form></body></html>");
			response.setContentType("text/html; charset=" + config.getCharset());
			response.getWriter().print(w.toString());
		}
	}
}
