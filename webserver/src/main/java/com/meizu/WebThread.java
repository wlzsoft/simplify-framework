package com.meizu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import com.meizu.util.SessionIdGenerator;

/**
 * 服务器线程,一个请求一个线程
 */
public class WebThread implements Runnable {
	private Socket socket;
	private final BufferedReader br;
	private final BufferedWriter bw;
	
//	//响应对象
//		private HttpResponse response;
//		//请求对象
//		private HttpRequest request;
//		//客户端
//		private Socket client;
//		//状态码
//		private int code = 200;

	public WebThread(Socket socket) throws Exception {
		
		this.socket = socket;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
	}
	
	
	@Override
	public void run() {
		try {
			
			// 开始解析HttpRequest
			HttpRequest request = new HttpRequest();
			HttpResponse response = new HttpResponse();
			String requestLine = br.readLine();
			if (requestLine != null) {
				System.out.println(requestLine);
				request.parseRequestLine(requestLine);
				boolean flag = true;
				while (flag) {
					String read = br.readLine();// 不停的开始读
					if (read == null || read.trim().length() < 1) {// 当到请求正文的时为0和内容为空的时候退出
						break;
					} else {
						System.out.println(read);
						request.parseRequestHeader(read);
					}
				}

				// 创建一个session 判断是否存在 ，没有就创建
				String sessionId = request.getCookies().get("sessionId");
				HttpSession session = null;
				if (sessionId == null || sessionId.length() < 32) {
					session = new HttpSession();
					session.setSessionId(SessionIdGenerator.getSessionId());
					WebServer.sessions.put(session.getSessionId(), session);
				} else {
					session = WebServer.sessions.get(sessionId);
					if (session == null) {
						session = new HttpSession();
						session.setSessionId(SessionIdGenerator.getSessionId());
						WebServer.sessions.put(session.getSessionId(), session);
					}
				}
				request.setSession(session);
				// cookie的可以为set-cookie，是http协议规定的，请求头上面cookie设为sessionID
				response.getResponseHeader().put("Set-Cookie",
						"sessionId=" + session.getSessionId());

				String contentLength = request.getRequestHeader().get(
						"Content-Length");// 判断是否有content length
				System.out.println("ContentLength :" + contentLength);
				if (contentLength != null) {
					int length = Integer.parseInt(contentLength);
					char[] buffer = new char[length];
					br.read(buffer);
					System.out.println("datas : " + new String(buffer));
					// 创建一个buffer能够存放datas也就是parameter里面的内容
					String postData = new String(buffer);
					String[] parameters = postData.split("&");
					for (String str : parameters) {
						String[] datas = str.split("=");
						request.getParameters().put(datas[0], datas[1]);// 存放进request里面
					}
					request.setBody(buffer);// 放入body中
				}
				// 解析请求头完毕
				// 路由处理
				HttpRoute.route(request, response);
				// 返回客户端
				sendToClient(response);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			this.code = 500;
//			try {
//				response.response(code);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
		}
		
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	// 写出发回客户端的内容 响应标头
	public void sendToClient(HttpResponse response) throws Exception {
		bw.write(response.getVersion() + " " + response.getStatusCode() + " "
				+ response.getReason() + "\r\n");
		bw.write("Date: " + new Date() + "\r\n");
		bw.write("Server: Parrot\r\n");
		bw.write("Accept-Ranges: bytes\r\n");
		bw.write("Content-Length: " + response.getBody().length + "\r\n");
		bw.write("Content-Type: text/html\r\n");
		bw.write("Set-Cookie: "
				+ response.getResponseHeader().get("Set-Cookie") + "\r\n");// 把cookie写上去
		bw.write("\r\n");
		bw.write(response.getBody());
		bw.flush();
		bw.close();
	}

}
