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

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import com.meizu.simplify.mvc.MvcInit;
import com.meizu.simplify.utils.ClearCommentUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;



/**
 * <p><b>Title:</b><i>Beetl 模板 页面处理返回方式</i></p>
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
public class BeetlForward implements IForward {
	private String str = null;
	public static void init() {
		String classPath = MvcInit.getPath();
	}

	public BeetlForward(String str) {
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

//		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();//字符串模板
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();		
		Configuration cfg = Configuration.defaultConfiguration();
		GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
//		共享变量-静态变量-全局变量
//		Map<String,Object> shared = new HashMap<String,Object>();
//		shared.put("type", "all");
//		gt.setSharedVars(shared);
		gt.registerFunctionPackage("t", new FunctionPackage());//自定义模板函数
		Template template = gt.getTemplate(str);
//		Template template = gt.getTemplate("hello,${name}");//字符串模板

		// 将request中的对象赋给模版
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			template.binding(name, request.getAttribute(name));
		}

		String content = template.render();	
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
		
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
