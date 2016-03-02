package com.meizu;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.meizu.simplify.mvc.SecurityFilter;
import com.meizu.util.WebUtil;

public class HttpRoute {
	private static final SecurityFilter filter = new SecurityFilter();
	private static Map<String, String> routeMap = new HashMap<String, String>();
	// 静态变量加载路由配置
	static {
		Properties props = new Properties();
		try {
			InputStream is = WebServer.class.getClassLoader().getResourceAsStream("route.properties");
			props.load(is);
			for (Object key : props.keySet()) {
				routeMap.put((String) key, (String) props.get(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void route(HttpRequest request, HttpResponse response) {
		String requestUrl = request.getRequestURI();
		if (isRoute(requestUrl)) {
			/*String servletName = routeMap.get(requestUrl);
			if (servletName != null) {*/
				try {
					filter.doFilter(request, response, null);
					/*
					 //servlet实现，直接去掉
					 Servlet servlet = (Servlet) Class.forName(servletName).newInstance();
					if (request.getMethod().equals("GET")) {
						servlet.doGet(request, response);
					} else {
						servlet.doPost(request, response);
					}*/
				} catch (Exception e) {
					e.printStackTrace();
				}
			/*} else {
				response.setStatusCode("404");
				response.setReason("Not Found");
				String html = "<html><head></head><body>File Not Found !</body></html>";
				response.setBody(html.toCharArray());
			}*/
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
					response.setBody(WebUtil.getChars(buffer));
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

	// 判断是否需要在路由表中去寻找
	public static boolean isRoute(String url) {
		if (url.endsWith(".html") || url.endsWith(".htm")
				|| url.endsWith(".css") || url.endsWith(".js")) {
			return false;
		} else {
			return true;
		}
	}
}
