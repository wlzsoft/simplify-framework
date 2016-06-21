package com.meizu;

import java.net.Socket;

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
public class TaskFactory {
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	public static void add(Socket socket) {
		Thread thread = ThreadPool.get();
		if(thread==null&&!ThreadPool.isFull()) {
			ThreadPool.add(new Thread(new MessageHandler(socket)));
		}
		if(thread!=null) {
			thread.start();
		}
	}
}
