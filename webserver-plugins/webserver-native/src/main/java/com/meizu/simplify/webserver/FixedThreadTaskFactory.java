package com.meizu.simplify.webserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
  * <p><b>Title:</b><i>任务工厂</i></p>
 * <p>Desc: 定长线程的任务工厂</p>
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
//@Bean
//@Plugin(type=PluginTypeEnum.WEBSERVER,value="native")
public class FixedThreadTaskFactory extends AbstractBioTaskFactory{
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog)  throws IOException{
		try {
			super.add(host, port, backlog);
			MessageRunnable mh = new MessageRunnable(serverSocket);
			for (int i=0; i<ThreadPool.getPoolSize(); i++) {
				ThreadPool.add(mh,"连接"+(i+1));
			}
			MessageRunnable mh2 = new MessageRunnable(serverSocket);
			ThreadPool.add(mh2,"连接b");
			while(ServerStatus.isRunning) {
				try {
					TimeUnit.SECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
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
