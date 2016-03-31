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
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.view.annotation.TemplateType;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.webcache.annotation.WebCache;


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
	
	private String extend;
	
	@Resource
	private PropertiesConfig config;
	
	@Deprecated//后续从配置中读取就可以
	public static String getPath() {
		String path = VelocityTemplate.class.getResource("/").getPath();
		return path.substring(0, path.lastIndexOf("/"));
	}
	public VelocityTemplate() {
		extend = getExtend();
	}
	@InitBean 
	public void init() {
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
	


	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		String prefixUri = "/template/velocity/";
		setContentType(request, response,config);

		Template template = Velocity.getTemplate(prefixUri+templateUrl+extend);

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
			checkCacheAndWrite(request, response, webCache, staticName, content,config);
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
