package com.meizu.servlet;

import com.meizu.HttpRequest;
import com.meizu.HttpResponse;
public class IndexServlet implements Servlet {
	@Override
	public void doGet(HttpRequest request, HttpResponse response) {
		response.setStatusCode("200");
		response.setReason("OK");
		String html = "<html><head></head><body>Index Servlet</body></html>";
		response.setBody(html.toCharArray());
	}

	@Override
	public void doPost(HttpRequest request, HttpResponse response) {
		doGet(request, response);
	}

}
