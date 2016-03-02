package com.meizu.servlet;

import javax.servlet.http.HttpSession;

import com.meizu.HttpRequest;
import com.meizu.HttpResponse;
import com.meizu.HttpRoute;

public class LoginServlet implements Servlet {

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		String userName = request.getParameter("userName");
		HttpSession session = request.getSession();
		if (userName != null && userName.equals("admin")) {
			request.setRequestUrl("/");//服务器内部跳转
			session.setAttribute("admin", userName);
			HttpRoute.route(request, response);
		} else {
			String html = "<html><head><title>登录</title></head><body>";
			html+=userName;
			html+=" Welcome to Server</body></html>";
			response.setBody(html.toCharArray());
		}

	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		doGet(request, response);

	}

}
