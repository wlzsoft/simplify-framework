package com.meizu.servlet;

import com.meizu.HttpRequest;
import com.meizu.HttpResponse;
import com.meizu.HttpRoute;
import com.meizu.HttpSession;

public class LoginServlet implements Servlet {

	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		String userName = request.getParameter("userName");// 得到刚刚存在parameter里的值
		HttpSession session = request.getSession();
		if (userName != null && userName.equals("admin")) {// 如果访问的数据中匹配，就跳转
			request.setRequestUrl("/ok.html");
			session.setAttribute("admin", userName);// 登录成功，放入session
			HttpRoute.route(request, response);// 塞给路由
		} else {
			String html = "<html><head><title>登录</title></head><body>";
			html+=request.getParameter("usr");
			html+=" Welcome to Server</body></html>";
			response.setBody(html.toCharArray());
			request.setRequestUrl("/error.html");
			HttpRoute.route(request, response);
		}

	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		doGet(request, response);

	}

}
