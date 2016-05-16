package com.meizu.simplify.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.InitBean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.annotation.TemplateExtend;
import com.meizu.simplify.utils.ClassUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


/**
 * <p><b>Title:</b><i>Freemarker 页面处理返回方式</i></p>
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
@TemplateExtend(extend="ftl")
public class FreemarkerTemplate  implements ITemplate{
	public String extend;
	private Configuration cfg = null; 
	@Resource
	private PropertiesConfig config;
	public FreemarkerTemplate() {
		extend = getExtend();
	}
	@InitBean 
	public void init() {
		// 创建一个FreeMarker实例  
        cfg = new Configuration(Configuration.VERSION_2_3_24);  
        cfg.setClassForTemplateLoading(FreemarkerTemplate.class,"/");       
	}
	
	public Configuration getConfiguration() {
		return cfg;
	}
	

	@Override
	public String render(Map<String,Object> parameter, String templateUrl, String prefixUri) throws IOException{
		return render(parameter, templateUrl, prefixUri,extend);
	}
	@Override
	public String render(Map<String, Object> parameters, String templateUrl, String prefixUri,String extend) throws IOException {
        // 获取模板文件  
        Template template = cfg.getTemplate(prefixUri+templateUrl+extend);  
		StringWriter vw = new StringWriter(0);
		String content = "";
		try {
			// 合并数据模型和模板，并将结果输出到vw中  
			template.process(parameters, vw); // 往模板里写数据  
			content = vw.toString();
		} catch (TemplateException e) {  
	            e.printStackTrace();  
		} finally {
			if (vw != null) {
				vw.flush();
				vw.close();
			}
		}
		return content;
	}
}
