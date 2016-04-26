package com.meizu.simplify.mvc.view;

import java.io.IOException;
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

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.view.annotation.TemplateType;
import com.meizu.simplify.mvc.view.function.BeetlFunctionDirectivePackage;
import com.meizu.simplify.webcache.annotation.WebCache;



/**
 * <p><b>Title:</b><i>Beetl 模板 页面处理返回方式</i></p>
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
@TemplateType("beetl")
public class BeetlTemplate  implements IPageTemplate {
	private GroupTemplate gt = null;
	private String extend;
	@Resource
	private PropertiesConfig config;
	
	public BeetlTemplate() {
		init();
	}
	
	public void init() {
//		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();//字符串模板
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();		
		try {
			Configuration cfg = Configuration.defaultConfiguration();
			gt = new GroupTemplate(resourceLoader, cfg);
			gt.registerFunctionPackage("t", new BeetlFunctionDirectivePackage());//自定义模板函数
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		extend = getExtend();
		
	}


	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		String prefixUri = "/template/beetl/";
		setContentType(request, response,config);
		
		// 将request中的对象赋给模版
		Map<String,Object> parameter = new HashMap<String,Object>();
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			parameter.put(name, request.getAttribute(name));
		}
		
		String content = render(parameter, templateUrl, prefixUri);	
		checkCacheAndWrite(request, response, webCache, staticName, content,config);
		
	}

	private String render(Map<String,Object> parameter, String templateUrl, String prefixUri) {
//		共享变量-静态变量-全局变量
//		Map<String,Object> shared = new HashMap<String,Object>();
//		shared.put("type", "all");
//		gt.setSharedVars(shared);
//		Template template = gt.getTemplate("hello,${name}");//字符串模板
		Template template = gt.getTemplate(prefixUri+templateUrl+extend);
		template.binding(parameter);
		String content = template.render();
		return content;
	}


}
