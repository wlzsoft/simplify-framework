package vip.simplify.view.beetl;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.template.annotation.TemplateType;
import vip.simplify.template.beetl.BeetlTemplate;
import vip.simplify.view.IPageTemplate;
import vip.simplify.webcache.annotation.WebCache;



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
public class BeetlPageTemplate  implements IPageTemplate {
	@Inject
	private PropertiesConfig config;
	@Inject
	private BeetlTemplate beetlTemplate;
	

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
		String content = beetlTemplate.render(parameter, templateUrl, prefixUri,beetlTemplate.extend);	
		checkCacheAndWrite(request, response, webCache, staticName, content,config);
		
	}



}
