package com.meizu;

import java.util.ArrayList;
import java.util.List;

/**
  * <p><b>Title:</b><i>线程池</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月21日 下午2:15:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月21日 下午2:15:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ThreadPool {
	private static int poolSize = 4;
	private static List<Thread> threadList = new ArrayList<>(poolSize);
	public static void add(Thread thread) {
		if(threadList.size()<20) {
			threadList.add(thread);
			thread.start();
		}
	}
	public static boolean isFull() {
		if(threadList.size()>=20) {
			return true;
		} 
		return false;
	}
	public static Thread get() {
		return null;
	}
}
