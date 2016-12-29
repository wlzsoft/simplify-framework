package com.meizu.simplify.net;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.meizu.simplify.ioc.Startup;

/**
  * <p><b>Title:</b><i>引导启动</i></p>
 * <p>Desc: 1.使用自己实现的连接池，该连接池受到java本身api的限制，性能提升不高，但已经远远优于BioBootstrap
 *          2.目前这个线程池不不完整，只是固定大小的，不能伸缩
 *          3.目前这个线程池和连接绑定了，实现上有问题</p>
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
	public volatile static boolean isRunning = true;
	public static void main(String[] args) {
		Startup.start();
		//配置加载开始
		Properties props = new Properties();
		InputStream is = Bootstrap.class.getClassLoader().getResourceAsStream("web.properties");
		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//配置加载结束
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
		int backlog = 5;//连接等待队列
		int port = 8060;
		String host = null;//"10.2.70.36";
		try {
			ITaskFactory factory = new JDKCachedThreadPoolTaskFactory();
//			ITaskFactory factory = new FixedThreadTaskFactory();
//			ITaskFactory factory = new BioTaskFactory();
			factory.add(host,port,backlog);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		return true;
	}
	
	public static void stop() {
		isRunning = false;
		System.out.println("已通知系统停止运行");
	}
}
