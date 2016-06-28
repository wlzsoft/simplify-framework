package com.meizu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月15日 下午5:50:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月15日 下午5:50:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MessageHandler implements Runnable{
	private ServerSocket serverSocket;
	private long time = 0;
	public MessageHandler(ServerSocket serverSocket,long time) {
		this.serverSocket = serverSocket;
		this.time  = time;
	}
	@Override
	public void run() {
		while(Bootstrap.isRunning) {
			/*HttpRequest request = new HttpRequest();
			HttpResponse response = null;
			
			try {
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求,由线程"+Thread.currentThread().getName());
				response = new HttpResponse(socket);
				InputStream is = socket.getInputStream();
				// 开始解析HttpRequest
				BufferedReader tr = new BufferedReader(new InputStreamReader(is));
				String requestLine = tr.readLine();
				if (requestLine != null) {	
					System.out.println(requestLine);
					request.parseRequestLine(requestLine);
					boolean flag = true;
					while (flag) {
						String read = tr.readLine();
						if (read == null || read.trim().length() < 1) {// 当到请求正文的时为0和内容为空的时候退出
							break;
						} else {
							System.out.println(read);
							request.parseRequestHeader(read);
						}
					}

					String sessionId = request.getCookiesMap().get("sessionId");
					HttpSessionImplWrapper session = null;
					if (sessionId == null || sessionId.length() < 32) {
						session = new HttpSessionImplWrapper();
						session.setSessionId(SessionIdFactory.getSessionId());
						WebServer.sessions.put(session.getSessionId(), session);
					} else {
						session = WebServer.sessions.get(sessionId);
						if (session == null) {
							session = new HttpSessionImplWrapper();
							session.setSessionId(SessionIdFactory.getSessionId());
							WebServer.sessions.put(session.getSessionId(), session);
						}
					}
					request.setSession(session);
					// cookie的可以为set-cookie，是http协议规定的，请求头上面cookie设为sessionID
					response.getResponseHeader().put("Set-Cookie",
							"sessionId=" + session.getSessionId());

					String contentLength = request.getRequestHeader().get(
							"Content-Length");
					System.out.println("ContentLength :" + contentLength);
					if (contentLength != null) {
						int length = Integer.parseInt(contentLength);
						char[] buffer = new char[length];
						tr.read(buffer);
						System.out.println("datas : " + new String(buffer));
						String postData = new String(buffer);
						String[] parameters = postData.split("&");
						for (String str : parameters) {
							String[] datas = str.split("=");
							request.getParameters().put(datas[0], datas[1]);
						}
						request.setBody(buffer);
					}
					String upgrade = request.getHeader("Upgrade");
					if(upgrade!= null&&upgrade.equals("websocket")) {
						new Handler(socket,is,request).exec();
						return;
					}
					// 解析请求头完毕
					HttpRoute.route(request, response);
					response.sendToClient();
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode("500");
			}*/
			try {
				
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求,由线程"+Thread.currentThread().getName());
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
				BufferedReader br = new BufferedReader(isr);
				String content = null;
				while(StringUtil.isNotBlank(content = br.readLine())) {
					System.out.println(content);
				}
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				HttpResponse response = new HttpResponse(socket);
				response.setStatusCode("404");
				response.setReason("Not Found");
				String html = "<html><head></head><body>File Not Found !</body></html>";
				response.setBody(html.toCharArray());
				response.sendToClient();
				isr.close();
				br.close();
				socket.close();
//				socket.shutdownInput();
//				socket.shutdownOutput();
				System.out.println("============"+socket.isClosed()+"|"+socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
