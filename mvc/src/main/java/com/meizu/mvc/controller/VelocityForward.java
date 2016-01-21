package com.meizu.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.meizu.mvc.MvcInit;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.webcache.annotation.CacheSet;
import com.meizu.webcache.web.Cache;
import com.meizu.webcache.web.CacheBase;


/**
 * Velocity 页面处理返回方式
 * 
 */
public class VelocityForward implements IForward {
	//private static SimplePool writerPool = new SimplePool(64);
	private String str = null;
	static {
		String classPath = MvcInit.getPath();

		Velocity.setProperty(Velocity.INPUT_ENCODING, MvcInit.charSet);
		Velocity.setProperty(Velocity.OUTPUT_ENCODING, MvcInit.charSet);

		/*
		 * Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		 * Velocity.setProperty("class.resource.loader.cache", Init.debug ?
		 * "false" : "true");
		 * Velocity.setProperty("class.resource.loader.class",
		 * "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
		 * );
		 * Velocity.setProperty("class.resource.loader.modificationCheckInterval"
		 * , "0"); Velocity.setProperty("class.resource.loader.path", ".");
		 */

		Velocity.setProperty(Velocity.RESOURCE_LOADER, "file");
		Velocity.setProperty("file.resource.loader.description", " Velocity File Resource Loader");
		Velocity.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		Velocity.setProperty("file.resource.loader.path", classPath);
		Velocity.setProperty("file.resource.loader.cache", MvcInit.debug ? "false" : "true");
		Velocity.setProperty("file.resource.loader.modificationCheckInterval", "5");
		
		String directives = "com.duxiu.framework.mvc.directives.DesDecDirective";
		if(!StringUtil.isEmpty(MvcInit.directives)) directives += "," + MvcInit.directives;
		Velocity.setProperty("userdirective", directives);

		File file = new File(classPath + "layouts/macros.vm");
		if (file.exists()) Velocity.setProperty("velocimacro.library", file.getAbsolutePath());

		if (MvcInit.debug) {
			Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			Velocity.setProperty("runtime.log.logsystem.log4j.category", "org.apache.velocity");
		}

		Velocity.init();
	}

	public VelocityForward(String str) {
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

	@SuppressWarnings("unchecked")
	public void doAction(HttpServletRequest request, HttpServletResponse response, CacheSet cacheSet, String staticName) throws ServletException, IOException {

		// 设置编码
		setContentType(request, response);

		// 取模版
		Template template = Velocity.getTemplate(str);

		// 将request中的对象赋给模版
		VelocityContext context = new VelocityContext();
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			context.put(name, request.getAttribute(name));
		}

		StringWriter vw = new StringWriter(0);
		try {
			template.merge(context, vw);
			
			/* 文件缓存 */
			String content = vw.toString();
			if (cacheSet != null) {
				// 是否去除空格
				if(cacheSet.removeSpace()) content = StringUtil.removeSpace(content);
				Cache cache = CacheBase.getCache(cacheSet);
				if(cache != null && cache.doCache(cacheSet, staticName, content,response)){
					// 缓存成功.
				}
			}
			response.setCharacterEncoding(MvcInit.charSet);
			response.setContentType("text/html; charset=" + MvcInit.charSet);
			response.getWriter().print(content);
			
		} finally {
			if (vw != null) {
				vw.flush();
				vw.close();
			}
		}
		
		/*ServletOutputStream output = response.getOutputStream();
		VelocityWriter vw = null;
		try {
			vw = (VelocityWriter) writerPool.get();
			if (vw == null) {
				vw = new StringWriter();  //new VelocityWriter(new OutputStreamWriter(output, MvcInit.charSet), 4 * 1024, true);
			} else {
				vw.recycle(new OutputStreamWriter(output, MvcInit.charSet));
			}
			template.merge(context, vw);
		} finally {
			if (vw != null) {
				try {
					vw.flush();
				} catch ( IOException e ) {}
				vw.recycle(null);
				writerPool.put(vw);
			}
		}*/
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

}
