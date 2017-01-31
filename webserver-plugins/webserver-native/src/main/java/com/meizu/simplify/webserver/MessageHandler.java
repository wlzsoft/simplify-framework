package com.meizu.simplify.webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.meizu.simplify.utils.StringUtil;
import com.meizu.simplify.utils.UUIDUtil;
import com.meizu.simplify.webserver.websocket.WebSocket.Handler;

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
public class MessageHandler {
	public static Map<String, HttpSessionImplWrapper> sessions = new HashMap<String, HttpSessionImplWrapper>();
	
	public static HttpResponse parseMessage(Socket socket,SocketChannel sc, InputStream inputStream) throws IOException {
		InputStreamReader isr = null;
		if(inputStream!=null) {
			isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
		}
		BufferedReader br = null;
		if(isr != null) {
			br = new BufferedReader(isr);
		}
		//解析请求内容
		//解析请求行--http1.1中，必须要有的请求line 格式是： [method] [url] [version] 例子： GET /trade/list/bill-list HTTP/1.1
//		sc.read(dst) //TODO nio读取一行
		String requestLine = br.readLine();
		if(requestLine == null) {//TODO 很奇怪，请求发起后正常，但是过一小会，会再次接收到请求，而且内容是空的，导致异常，这个先跳过[经过分析，只有chrome浏览器会有这个问题，估计是chrome后续会发送一些心跳信息过来]
			return null;//需要仔细分析
		}
		System.out.println(requestLine);
		HttpRequest request = new HttpRequest();
		request.parseRequestLine(requestLine);
		//解析请求头信息
		String requestHead = null;
		while(StringUtil.isNotBlank(requestHead = br.readLine())) {
			System.out.println(requestHead);
			request.parseRequestHeader(requestHead);
		}
		
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
			new Handler(socket,inputStream,request).exec();
			return null;
		}
		
		//请求源数据已经处理完毕
		//向浏览器响应内容
		HttpResponse response = new HttpResponse(socket,sc);
		//session解析处理
		String sessionId = request.getCookiesMap().get("sessionId");
		HttpSessionImplWrapper session = null;
		if(StringUtil.isBlank(sessionId)) {
			session = new HttpSessionImplWrapper();
			session.setSessionId(UUIDUtil.getRandomUUID());
			sessions.put(session.getSessionId(), session);
		} else {
			session = sessions.get(sessionId);
			if(session == null) {
				session = new HttpSessionImplWrapper();
				session.setSessionId(UUIDUtil.getRandomUUID());
				sessions.put(session.getSessionId(), session);
			}
		}
		request.setSession(session);
		// cookie的可以为set-cookie，是http协议规定的，请求头上面cookie设为sessionID
		response.getResponseHeader().put("Set-Cookie","sessionId=" + session.getSessionId());
		//执行具体业务处理--先是路由选择-路径选择
		HttpRoute.route(request, response);
		//真实的响应动作
		response.sendToClient();
		if(inputStream != null) {
			inputStream.close();
		}
		if(isr != null) {
			isr.close();
		}
		if(br != null) {
			br.close();
		}
		if(sc != null) {
			sc.close();
		}
		if(socket != null) {
			socket.close();
//				socket.shutdownInput();
//				socket.shutdownOutput();
			System.out.println("============"+socket.isClosed()+"|"+socket);
		}
		return response;
	}

}
