package com.meizu.simplify.mvc.view;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.FreemarkerTemplate;
import com.meizu.simplify.template.annotation.TemplateType;
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
public class FreemarkerPageTemplate  implements IPageTemplate{
	@Resource
	private PropertiesConfig config;
	@Resource
	private FreemarkerTemplate freemarkerTemplate;

	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		String prefixUri = "/template/freemarker/";
		// 指定FreeMarker模板文件的位置  
		freemarkerTemplate.getConfiguration().setServletContextForTemplateLoading(request.getServletContext(), prefixUri);  
		setContentType(request, response,config);
		// 将request中的对象赋给模版
		Map<String, Object> parameters = new HashMap<>();
		Enumeration<String> atts = request.getAttributeNames();
		while ( atts.hasMoreElements() ) {
			String name = atts.nextElement();
			parameters.put(name, request.getAttribute(name));
		}
		
		String content = freemarkerTemplate.render(parameters, templateUrl, "",freemarkerTemplate.extend);
		checkCacheAndWrite(request, response, webCache, staticName, content,config);
	}
	
}
