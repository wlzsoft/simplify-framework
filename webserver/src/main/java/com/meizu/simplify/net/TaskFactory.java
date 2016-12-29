package com.meizu.simplify.net;

import java.net.ServerSocket;
import java.util.concurrent.TimeUnit;

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
public class TaskFactory implements ITaskFactory{
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	public void add(ServerSocket serverSocket) {
		MessageRunnable mh = new MessageRunnable(serverSocket);
		for (int i=0; i<ThreadPool.getPoolSize(); i++) {
			ThreadPool.add(mh,"连接"+(i+1));
		}
		MessageRunnable mh2 = new MessageRunnable(serverSocket);
		ThreadPool.add(mh2,"连接b");
		while(Bootstrap.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
