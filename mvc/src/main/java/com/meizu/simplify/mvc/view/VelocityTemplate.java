package com.meizu.simplify.mvc.view;

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

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.BeanFactory;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.view.annotation.TemplateType;
import com.meizu.simplify.utils.ClearCommentUtil;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;
import com.meizu.simplify.webcache.web.Cache;
import com.meizu.simplify.webcache.web.CacheBase;


/**
 * <p><b>Title:</b><i>Velocity 页面处理返回方式</i></p>
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
@Bean
@TemplateType("velocity")
public class VelocityTemplate  implements ITemplate{
	//private static SimplePool writerPool = new SimplePool(64);
	
	private static PropertiesConfig config;
	
	public VelocityTemplate() {
		init();
	}
	
	@Deprecated//后续从配置中读取就可以
	public static String getPath() {
		String path = VelocityTemplate.class.getResource("/").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	
	public static void init() {
		config = BeanFactory.getBean(PropertiesConfig.class);
//		String classPath = config.getClasspath();
		String classPath = getPath();
		Velocity.setProperty(Velocity.INPUT_ENCODING, config.getCharset());
		Velocity.setProperty(Velocity.OUTPUT_ENCODING, config.getCharset());

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
		Velocity.setProperty("file.resource.loader.cache", config.getDebug() ? "false" : "true");
		Velocity.setProperty("file.resource.loader.modificationCheckInterval", "5");
		
		String directives = "com.meizu.simplify.mvc.view.function.EncryptFunctionDirective";
		if(!StringUtil.isEmpty(config.getDirectives())) directives += "," + config.getDirectives();
		Velocity.setProperty("userdirective", directives);

		File file = new File(classPath + "layouts/macros.vm");
		if (file.exists()) Velocity.setProperty("velocimacro.library", file.getAbsolutePath());

		if (config.getDebug()) {
			Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			Velocity.setProperty("runtime.log.logsystem.log4j.category", "org.apache.velocity");
		}

		Velocity.init();
	}
	

	/**
	 * 设置内容类型和编码
	 * 
	 * @param request
	 * @param response
	 */
	private void setContentType(HttpServletRequest request, HttpServletResponse response) {
		response.setCharacterEncoding(config.getCharset());
		response.setContentType("text/html; charset=" + config.getCharset());
	}

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {

		// 设置编码
		setContentType(request, response);

		// 取模版
		Template template = Velocity.getTemplate(templateUrl);

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
			
			String content = vw.toString();
			if (webCache != null && webCache.mode() != WebCache.CacheMode.nil) {
				// 是否去除空格
				if(webCache.removeSpace()) {
					content = ClearCommentUtil.clear(content);
					content = StringUtil.removeHtmlSpace(content);
				}
				Cache cache = CacheBase.getCache(webCache);
				if(cache != null && cache.doCache(webCache, staticName, content,response)){
					// 缓存成功.
				}
			}
			response.setCharacterEncoding(config.getCharset());
			response.setContentType("text/html; charset=" + config.getCharset());
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
				vw = new StringWriter();  //new VelocityWriter(new OutputStreamWriter(output, config.getCharset()), 4 * 1024, true);
			} else {
				vw.recycle(new OutputStreamWriter(output, config.getCharset()));
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
}
