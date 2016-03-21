package com.meizu.simplify.mvc.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.utils.ClearCommentUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;

import httl.Engine;
import httl.Template;


/**
 * <p><b>Title:</b><i>Httl 模板 页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:26:39</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:26:39</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class HttlForward implements IForward {
	private String str = null;
	public static void init() {
		String classPath = MvcInit.getPath();
	}

	public HttlForward(String str) {
		this.str = str;
	}

	/**
	 * 设置内容类型和编码
	 * 
	 * @param request
	 * @param response
	 */
	private void setContentType(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding(MvcInit.charSet);
		response.setContentType("text/html; charset=" + MvcInit.charSet);
	}

	public void doAction(HttpServletRequest request, HttpServletResponse response, WebCache cacheSet, String staticName) throws ServletException, IOException {

		// 设置编码
		setContentType(request, response);

		Engine engine = Engine.getEngine();
		Template template = null;
		try {
			// 取模版
			template = engine.getTemplate(str);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// 将request中的对象赋给模版
		Map<String, Object> parameters = new HashMap<>();
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			parameters.put(name, request.getAttribute(name));
		}

		StringWriter vw = new StringWriter(0);
		try {
			template.render(parameters, vw);
			String content = vw.toString();
			if (cacheSet != null && cacheSet.mode() != WebCache.CacheMode.nil) {
				// 是否去除空格
				if(cacheSet.removeSpace()) {
					content = ClearCommentUtil.clear(content);
					content = StringUtil.removeHtmlSpace(content);
				}
				Cache cache = CacheBase.getCache(cacheSet);
				if(cache != null && cache.doCache(cacheSet, staticName, content,response)){
					// 缓存成功.
				}
			}
			response.setCharacterEncoding(MvcInit.charSet);
			response.setContentType("text/html; charset=" + MvcInit.charSet);
			response.getWriter().print(content);
			
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (vw != null) {
				vw.flush();
				vw.close();
			}
		}
		
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
