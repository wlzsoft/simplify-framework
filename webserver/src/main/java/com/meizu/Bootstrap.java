package com.meizu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>引导启动</i></p>
 * <p>Desc: TODO后续代替WebServer</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月13日 上午11:42:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月13日 上午11:42:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class Bootstrap {
	private static boolean isRunning = true;
	public static void main(String[] args) {
		boolean isStart = start();
		if(!isRunning&&isStart) {
			System.out.println("服务器正常关闭");
		} else {
			System.out.println("服务启动失败,已停止服务");
			System.exit(-1);
		}
	}
	/**
	 * 方法用途: 服务启动入口<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	public static boolean start() {
		System.out.println("开始启动服务器...");
		int backlog = 5;
		int port = 8060;
		String host = "127.0.0.1";
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			InetSocketAddress inetSocketAddress = null;
			if(StringUtil.isBlank(host)) {
				inetSocketAddress = new InetSocketAddress(port);
			} else {
				inetSocketAddress = new InetSocketAddress(host,port);
			}
			if(backlog>0) {
				serverSocket.bind(inetSocketAddress,backlog);
			} else {
				serverSocket.bind(inetSocketAddress);
			}
			/*char c = '中';//unicode 和 utf8的区别，在java中的影响
			Charset cs = Charset.forName("GBK");
			CharBuffer cb = CharBuffer.allocate(1);
			cb.put(c);
			cb.flip();
			ByteBuffer bb = cs.encode (cb);
			System.out.println(bb.array().length);*/
			while(isRunning) {
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求");
				
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
				BufferedReader br = new BufferedReader(isr);
				String content = null;
				while((content = br.readLine())!=null) {
					System.out.println(content);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static void stop() {
		isRunning = false;
		System.out.println("已通知系统停止运行");
	}
}
