package com.meizu.simplify.template;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Map;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.annotation.TemplateType;

import httl.Engine;
import httl.Template;
/**
 * <p><b>Title:</b><i>Httl 模板 页面处理返回方式</i></p>
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
@TemplateType(value ="httl",extend = "httl")
public class HttlTemplate implements ITemplate {
	private Engine engine = null;
	private String extend;
	@Resource
	private PropertiesConfig config;
	
	public void init() {
		engine = Engine.getEngine();
		extend = getExtend();
	}

	public HttlTemplate() {
		init();
	}


	@Override
	public String render(Map<String, Object> parameters,String templateUrl, String prefixUri) throws IOException {
		Template template = null;
		try {
			template = engine.getTemplate(prefixUri+templateUrl+extend);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		
		String content = "";
		StringWriter vw = new StringWriter(0);
		try {
			template.render(parameters, vw);
			content = vw.toString();
		} catch (ParseException e) {
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
