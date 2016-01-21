package com.meizu.mvc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.meizu.mvc.MvcInit;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.webcache.annotation.CacheSet;
import com.meizu.webcache.web.Cache;
import com.meizu.webcache.web.CacheBase;


/**
 * 页面处理返回方式
 * 
 */
public class ActionForward implements IForward {
	private String str = null;

	public ActionForward(String str) {
		this.str = str;
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher(str);

		// 跳转前检查静态规则
		if (cacheSet != null && cacheSet.mode() != CacheSet.CacheMode.nil) {
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

		} else rd.forward(request, response);
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
