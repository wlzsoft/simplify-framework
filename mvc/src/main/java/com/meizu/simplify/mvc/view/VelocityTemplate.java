package com.meizu.simplify.mvc.view;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.view.annotation.TemplateType;
import com.meizu.simplify.utils.ClassUtil;
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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@TemplateType("velocity")
public class VelocityTemplate  implements IPageTemplate,ITemplate{
	//private static SimplePool writerPool = new SimplePool(64);
	
	private String extend;
	
	@Resource
	private PropertiesConfig config;
	
	
	public VelocityTemplate() {
		extend = getExtend();
	}
	@InitBean 
	public void init() {
		String classPath = ClassUtil.getClassPath();
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
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, classPath);
		Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_CACHE, config.getDebug() ? "false" : "true");
		Velocity.setProperty("file.resource.loader.modificationCheckInterval", "5");
		
		String directives = "com.meizu.simplify.mvc.view.function.EncryptFunctionDirective";
		if(!StringUtil.isEmpty(config.getDirectives())) directives += "," + config.getDirectives();
		Velocity.setProperty("userdirective", directives);

		File file = new File(classPath + "template/velocity/functions/macros.vm");
		if (file.exists()) Velocity.setProperty("velocimacro.library", file.getAbsolutePath());

		Velocity.setProperty(Velocity.RUNTIME_LOG, classPath+"velocity.log");
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
		
		// 将request中的对象赋给模版
		Map<String, Object> parameters = new HashMap<>();
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			parameters.put(name, request.getAttribute(name));
		}
		
		String content = render(parameters, templateUrl, prefixUri);
		checkCacheAndWrite(request, response, webCache, staticName, content,config);
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
	
	public String render(Map<String, Object> parameters, String templateUrl, String prefixUri) throws IOException {
		Template template = Velocity.getTemplate(prefixUri+templateUrl+extend);
		VelocityContext context = new VelocityContext(parameters);
		StringWriter vw = new StringWriter(0);
		String content = "";
		try {
			template.merge(context, vw);
			content = vw.toString();
		} finally {
			if (vw != null) {
				vw.flush();
				vw.close();
			}
		}
		return content;
	}
}
