package com.meizu.simplify.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.meizu.HttpResponse;

/**
  * <p><b>Title:</b><i>任务工厂</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月21日 下午2:29:08</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月21日 下午2:29:08</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class BioTaskFactory implements ITaskFactory{
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	public void add(ServerSocket serverSocket) {
		while (Bootstrap.isRunning) {
			try {
				Socket client = serverSocket.accept();
				System.out.println("来自客户端[" + client.getRemoteSocketAddress()
						+ "] 的请求 ");
				//对每一个客户端都启动一个线程处理
				new Thread(new Runnable() {
					@Override
					public void run() {
						HttpResponse response = null;
						try {
							response = MessageHandler.parseMessage(client, client.getInputStream());
						} catch (Exception e) {
							e.printStackTrace();
							if(response != null) {
								response.setStatusCode("500");
							}
						}
					}
				}).start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
