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
	public MessageHandler(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	@Override
	public void run() {
		while(Bootstrap.isRunning) {
			try {
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求");
				InputStream inputStream = socket.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
				BufferedReader br = new BufferedReader(isr);
				String content = null;
				while(StringUtil.isNotBlank(content = br.readLine())) {
					System.out.println(content);
				}
				isr.close();
				br.close();
				socket.close();
//				socket.shutdownInput();
//				socket.shutdownOutput();
				System.out.println("============"+socket.isClosed()+"|"+socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
