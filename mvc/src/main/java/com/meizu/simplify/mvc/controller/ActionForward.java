package com.meizu.simplify.mvc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:25:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:25:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ActionForward implements IForward {
	private String str = null;

	public ActionForward(String str) {
		this.str = str;
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, WebCache cacheSet, String staticName) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(str);

		// 跳转前检查静态规则
		if (cacheSet != null && cacheSet.mode() != WebCache.CacheMode.nil) {
			String content = getPageContent(request, response, rd);
			
			// 是否去除空格
			if(cacheSet.removeSpace()) content = StringUtil.removeHtmlSpace(content);

			Cache cache = CacheBase.getCache(cacheSet);
			if(cache != null && cache.doCache(cacheSet, staticName, content,response)){
				// 缓存成功.
			}
			response.setCharacterEncoding(MvcInit.charSet);
			response.setContentType("text/html; charset=" + MvcInit.charSet);
			response.getWriter().print(content);

		} else {
			rd.forward(request, response);
		}
	}

	/**
	 * 获取页面内容
	 * 
	 * @param request
	 * @param response
	 * @param rd
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private String getPageContent(HttpServletRequest request, HttpServletResponse response, RequestDispatcher rd) throws ServletException, IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		HttpServletResponse res = new HttpServletResponseWrapper(response) {
			@Override
			public PrintWriter getWriter() {
				return pw;
			}
		};
		rd.include(request, res);
		pw.flush();
		return os.toString();
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
