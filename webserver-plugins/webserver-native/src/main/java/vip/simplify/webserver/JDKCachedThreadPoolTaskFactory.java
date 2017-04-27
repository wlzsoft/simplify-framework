package vip.simplify.webserver;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
  * <p><b>Title:</b><i>任务工厂</i></p>
 * <p>Desc: 使用jdk自带的线程池的任务工厂-支持可伸缩灵活配置</p>
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
public class JDKCachedThreadPoolTaskFactory extends AbstractBioTaskFactory{
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param host
	 * @param port
	 * @param backlog
	 */
	@Override
	public void add(String host,int port,int backlog)  throws IOException{
		try {
			super.add(host, port, backlog);
			while (ServerStatus.isRunning) {
				Socket socket = serverSocket.accept();
				ExecutorService service = null;//Executors.newCachedThreadPool();
				int maxPoolSize = 50;
				int queueSize = 10_000;
				service = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L,	TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
	//			service.submit(new BioMessageRunnable(socket));
				service.submit(new BioMessageCallable(socket));
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
