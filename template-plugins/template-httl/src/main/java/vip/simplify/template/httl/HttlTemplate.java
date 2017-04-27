package vip.simplify.template.httl;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Map;

import vip.simplify.config.PropertiesConfig;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.template.ITemplate;
import vip.simplify.template.annotation.TemplateExtend;

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
@TemplateExtend(extend = "httl")
public class HttlTemplate implements ITemplate {
	private Engine engine = null;
	public String extend;
	@Inject
	private PropertiesConfig config;
	
	public void init() {
		engine = Engine.getEngine();
		extend = getExtend();
	}

	public HttlTemplate() {
		init();
	}

	@Override
	public String render(Map<String,Object> parameter, String templateUrl, String prefixUri) throws IOException{
		return render(parameter, templateUrl, prefixUri,extend);
	}
	@Override
	public String render(Map<String, Object> parameters,String templateUrl, String prefixUri,String extend) throws IOException {
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
