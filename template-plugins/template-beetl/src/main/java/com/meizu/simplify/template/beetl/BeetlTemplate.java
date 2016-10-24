package com.meizu.simplify.template.beetl;

import java.io.IOException;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.ITemplate;
import com.meizu.simplify.template.annotation.TemplateExtend;
import com.meizu.simplify.template.beetl.function.BeetlFunctionDirectivePackage;



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
@TemplateExtend
public class BeetlTemplate  implements ITemplate {
	private GroupTemplate gt = null;
	public String extend;
	@Resource
	private PropertiesConfig config;
	
	public BeetlTemplate() {
		init();
	}
	@InitBean
	public void initconfig() {
		
	}
	public void init() {
//		StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();//字符串模板
		BeetlClasspathResourceLoader resourceLoader = new BeetlClasspathResourceLoader();		
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
	public String render(Map<String,Object> parameter, String templateUrl, String prefixUri) {
		return render(parameter, templateUrl, prefixUri,extend);
	}
	@Override
	public String render(Map<String,Object> parameter, String templateUrl, String prefixUri,String extend) {
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
