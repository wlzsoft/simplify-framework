package com.meizu.simplify.mvc.view;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.mvc.MvcInit;
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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class RedirectForward implements IForward {
	private String url = null;
	private HashMap<String, String> paramMap = new HashMap<String, String>();
	
	public RedirectForward(String url) {
		this.url = url;
	}
	
	public RedirectForward(String url, HashMap<String, String> paramMap) {
		this.url = url;
		this.paramMap = paramMap;
	}
	
	public RedirectForward addPrarm(String key, String value) {
		paramMap.put(key, value);
		return this;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public HashMap<String, String> getParamMap() {
		return paramMap;
	}
	
	public void setParamMap(HashMap<String, String> paramMap) {
		this.paramMap = paramMap;
	}
	
	@Override
	public void doAction(HttpServletRequest request, HttpServletResponse response, WebCache cacheSet, String staticName) throws ServletException, IOException {
		if (paramMap.keySet().size() > 0) {
			StringBuffer w = new StringBuffer("<html><head></head><body onload=\"form1.submit()\"><form id=\"form1\" method=\"post\" action=\"" + url + "\">");
			for (String key : paramMap.keySet()) {
				w.append("<input type=\"hidden\"  name=\"" + key + "\" value=\"" + paramMap.get(key) + "\"/>");
			}
			w.append("</form></body></html>");
			response.setContentType("text/html; charset=" + MvcInit.charSet);
			response.getWriter().print(w.toString());
		} else {
			response.sendRedirect(url);
		}
	}
}
