package com.meizu.simplify.webserver;

import java.io.IOException;
import java.net.Socket;

/**
  * <p><b>Title:</b><i>任务工厂</i></p>
 * <p>Desc: 未使用连接池</p>
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
public class BioTaskFactory extends AbstractBioTaskFactory{
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog) {
		try {
			super.add(host, port, backlog);
			while (ServerStatus.isRunning) {
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端[" + socket.getRemoteSocketAddress()
						+ "] 的请求 ");
				//对每一个客户端都启动一个线程处理
				new Thread(new BioMessageRunnable(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
