package com.meizu.simplify.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.meizu.HttpRequest;
import com.meizu.HttpResponse;
import com.meizu.HttpRoute;
import com.meizu.HttpSessionImplWrapper;
import com.meizu.WebSocket;
import com.meizu.simplify.utils.StringUtil;
import com.meizu.util.SessionIdFactory;

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
	public static Map<String, HttpSessionImplWrapper> sessions = new HashMap<String, HttpSessionImplWrapper>();
	private ServerSocket serverSocket;
	private long time = 0;
	public MessageHandler(ServerSocket serverSocket,long time) {
		this.serverSocket = serverSocket;
		this.time  = time;
	}
	@Override
	public void run() {
		while(Bootstrap.isRunning) {
			HttpResponse response = null;
			try {
				//接收请求
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求,由线程"+Thread.currentThread().getName());
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
				BufferedReader br = new BufferedReader(isr);
				//解析请求内容
				//解析请求行--http1.1中，必须要有的请求line 格式是： [method] [url] [version] 例子： GET /trade/list/bill-list HTTP/1.1 
				String requestLine = br.readLine();
				HttpRequest request = new HttpRequest();
				request.parseRequestLine(requestLine);
				//解析请求头信息
				String requestHead = null;
				while(StringUtil.isNotBlank(requestHead = br.readLine())) {
					System.out.println(requestHead);
					request.parseRequestHeader(requestHead);
				}
				//session解析处理
				HttpSessionImplWrapper session = null;
				String sessionId = request.getCookiesMap().get("sessionId");
				if(StringUtil.isBlank(sessionId)) {
					session = new HttpSessionImplWrapper();
					session.setSessionId(SessionIdFactory.getSessionId());
					sessions.put(session.getSessionId(), session);
				} else {
					session = sessions.get(sessionId);
					if(session == null) {
						session = new HttpSessionImplWrapper();
						session.setSessionId(SessionIdFactory.getSessionId());
						sessions.put(session.getSessionId(), session);
					}
				}
				request.setSession(session);
				
				//解析请求体信息-请求参数
				//post方法请求的参数数据的处理
				String contentLength = request.getRequestHeader().get("Content-Length");
				System.out.println("ContentLength :" + contentLength);
				if (contentLength != null) {
					int length = Integer.parseInt(contentLength);
					char[] buffer = new char[length];
					br.read(buffer);
					System.out.println("datas : " + new String(buffer));
					String postData = new String(buffer);
					String[] parameters = postData.split("&");
					for (String str : parameters) {
						String[] datas = str.split("=");
						request.getParameters().put(datas[0], datas[1]);
					}
					request.setBody(buffer);
				}
				//websocket解析处理
				String upgrade = request.getHeader("Upgrade");
				if(upgrade!= null&&upgrade.equals("websocket")) {
					new WebSocket.Handler(socket,inputStream,request).exec();
					return;
				}
				
				try {
					TimeUnit.MILLISECONDS.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//请求源数据已经处理完毕
				//向浏览器响应内容
				response = new HttpResponse(socket);
				//执行具体业务处理--先是路由选择-路径选择
				HttpRoute.route(request, response);
				//真实的响应动作
				response.sendToClient();
				isr.close();
				br.close();
				socket.close();
//				socket.shutdownInput();
//				socket.shutdownOutput();
				System.out.println("============"+socket.isClosed()+"|"+socket);
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatusCode("500");
			}
		}
	}

}
