package com.meizu.simplify.mvc.view;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.mvc.view.annotation.TemplateType;
import com.meizu.simplify.webcache.annotation.WebCache;

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
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
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
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
		String prefixUri = "/template/httl/";
		setContentType(request, response, config);
		Template template = null;
		try {
			template = engine.getTemplate(prefixUri+templateUrl+extend);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// 将request中的对象赋给模版
		Map<String, Object> parameters = new HashMap<>();
		Enumeration<String> atts = request.getAttributeNames();
		while (atts.hasMoreElements()) {
			String name = atts.nextElement();
			parameters.put(name, request.getAttribute(name));
		}

		StringWriter vw = new StringWriter(0);
		try {
			template.render(parameters, vw);
			String content = vw.toString();
			checkCacheAndWrite(request, response, webCache, staticName, content, config);
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if (vw != null) {
				vw.flush();
				vw.close();
			}
		}

	}

}
