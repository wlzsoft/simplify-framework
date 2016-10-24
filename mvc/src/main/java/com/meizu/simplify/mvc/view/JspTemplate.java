package com.meizu.simplify.mvc.view;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.meizu.simplify.config.PropertiesConfig;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.template.ITemplate;
import com.meizu.simplify.template.annotation.TemplateExtend;
import com.meizu.simplify.template.annotation.TemplateType;
import com.meizu.simplify.view.IPageTemplate;
import com.meizu.simplify.webcache.annotation.WebCache;


/**
 * <p><b>Title:</b><i>页面处理返回方式</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月26日 下午3:25:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月26日 下午3:25:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@TemplateType(value = "jsp")
@TemplateExtend(extend="jsp")
public class JspTemplate implements IPageTemplate,ITemplate{
	
	
	@Resource
	private PropertiesConfig config;
	private String extend;
	public void init() {
		extend = getExtend();
	}

	public JspTemplate() {
		init();
	}
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, WebCache webCache, String staticName,String templateUrl) throws ServletException, IOException {
//		String prefixUri = "/template/jsp";
		String prefixUri = config.getTemplate();
		String content = render(request, response, templateUrl, prefixUri);
		checkCacheAndWrite(request, response, webCache, staticName, content,config);
	}

	private String render(HttpServletRequest request, HttpServletResponse response, String templateUrl,
			String prefixUri) throws ServletException, IOException {
		if(templateUrl.equals("500")) {
			templateUrl = "/500";
		}
		RequestDispatcher rd = request.getRequestDispatcher(prefixUri+templateUrl+extend);
		if(rd == null) {
			throw new UncheckedException("该容器不支持jsp视图");
		}
		String content = getPageContent(request, response, rd);
		return content;
	}


	/**
	 * 获取页面内容
	 * 
	 * @param request
	 * @param response
	 * @param rd
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	private static String getPageContent(HttpServletRequest request, HttpServletResponse response, RequestDispatcher rd) throws ServletException, IOException {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
		HttpServletResponse res = new HttpServletResponseWrapper(response) {
			@Override
			public PrintWriter getWriter() {
				return pw;
			}
		};
		rd.include(request, res);
		pw.flush();
		return os.toString();
	}

}
