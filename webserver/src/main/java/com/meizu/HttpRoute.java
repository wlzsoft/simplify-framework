package com.meizu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.meizu.simplify.encrypt.base64.Base64Encrypt;
import com.meizu.simplify.encrypt.sign.sha1.SHA1Encrypt;
import com.meizu.simplify.mvc.SecurityFilter;
import com.meizu.util.ByteAndCharConvertUtil;

public class HttpRoute {
	private static final SecurityFilter filter = new SecurityFilter();

	public static void route(HttpRequest request, HttpResponse response) {
		String upgrade = request.getHeader("Upgrade");
		if(upgrade!= null&&upgrade.equals("websocket")) {
//			request info:
//			GET /websocket/notice HTTP/1.1
//			Host: 127.0.0.1:8080
//			Connection: Upgrade
//			Pragma: no-cache
//			Cache-Control: no-cache
//			Upgrade: websocket
//			Origin: http://127.0.0.1:8080
//			Sec-WebSocket-Version: 13
//			User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.106 Safari/537.36
//			Accept-Encoding: gzip, deflate, sdch
//			Accept-Language: zh-CN,zh;q=0.8
//			Cookie: cookie_lang=1; JSESSIONID=1dqg8b1r9lt7o7j4mr0uqp4g3; Hm_lvt_82116c626a8d504a5c0675073362ef6f=1457403984; Hm_lpvt_82116c626a8d504a5c0675073362ef6f=1457415472; sessionId=393bbe756f294f2e8c3677164bddefa6
//			Sec-WebSocket-Key: utj2uKJA660Zw7uwVFQi8Q==
//			Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits
//			ContentLength :null
			
//			response info:
//			HTTP/1.1 101 Switching Protocols
//			Connection:Upgrade
//			Server:beetle websocket server
//			Upgrade:WebSocket
//			Date:Mon, 26 Nov 2012 23:42:44 GMT
//			Access-Control-Allow-Credentials:true
//			Access-Control-Allow-Headers:content-type
//			Sec-WebSocket-Accept:FCKgUr8c7OsDsLFeJTWrJw6WO8Q= 
			
//			byte[] bytes_sha1_in = (request.getHeader("Sec-WebSocket-Key")+ "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes();
//			byte[] bytes_sha1_out = SHA1Encrypt.sign(bytes_sha1_in);
//			String str_sha1_out = new String(Base64Encrypt.encode((bytes_sha1_out)));
//			response.setStatusCode("101");
//			response.setReason("Switching Protocols");
//			response.setHeader("Connection", "Upgrade");
//			response.setHeader("Upgrade", "WebSocket");
//			response.setHeader("Access-Control-Allow-Credentials", "true");
//			response.setHeader("Access-Control-Allow-Headers", "content-type");
//			response.setHeader("Sec-WebSocket-Accept", str_sha1_out);
//			String html = "success";
//			response.setBody(html.toCharArray());
			
			return;
			
		}
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
