package com.meizu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.meizu.simplify.mvc.ControllerFilter;
import com.meizu.util.ByteAndCharConvertUtil;

public class HttpRoute {
	private static final ControllerFilter filter = new ControllerFilter();

	public static void route(HttpRequest request, HttpResponse response) {

		String requestUrl = request.getRequestURI();
		if (isResource(requestUrl)) {
			try {
				FilterChain fc = new FilterChain() {
					@Override
					public void doFilter(ServletRequest request, ServletResponse resp) throws IOException, ServletException {
						HttpResponse response = (HttpResponse) resp;
						response.setStatusCode("404");
						response.setReason("Not Found");
						String html = "<html><head></head><body>File Not Found !</body></html>";
						response.setBody(html.toCharArray());
					}
				};
				filter.doFilter(request, response, fc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 加载静态页面从web.properties里面获取
			String contextpath = WebServer.config.get("path");
			if(contextpath == null) {
				contextpath = HttpRoute.class.getClassLoader().getResource("webapp").getPath();
			}
			File file = new File(contextpath + requestUrl);
			if (file.exists()) {
				response.setStatusCode("200");
				response.setReason("OK");
				byte[] buffer = new byte[(int) file.length()];
				try (FileInputStream fis = new FileInputStream(file)) {
					fis.read(buffer);
					response.setBody(ByteAndCharConvertUtil.getChars(buffer));
				} catch (Exception e) {

				}
			} else {
				response.setStatusCode("404");
				response.setReason("Not Found");
				String html = "<html><head></head><body>File Not Found !</body></html>";
				response.setBody(html.toCharArray());
			}
		}
	}

	public static boolean isResource(String url) {
		if (url.endsWith(".html") || url.endsWith(".htm")
				|| url.endsWith(".css") || url.endsWith(".js")) {
			return false;
		} else {
			return true;
		}
	}
}
