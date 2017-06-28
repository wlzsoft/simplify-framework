package vip.simplify.net;

import vip.simplify.ioc.BeanFactory;
import vip.simplify.ioc.Startup;
import vip.simplify.utils.PropertieUtil;
import vip.simplify.webserver.ITaskFactory;
import vip.simplify.webserver.ServerStatus;

import java.util.concurrent.TimeUnit;

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
	
	public static void main(String[] args) {
		run();
	}
	
	public static void run() {
		Startup.start();
		//配置加载开始
		PropertieUtil propertiesUtil = new PropertieUtil("web.properties");
		//配置加载结束
		boolean isStart = start(propertiesUtil.getString("host"),propertiesUtil.getInteger("port"),propertiesUtil.getInteger("backlog"));
		if(!ServerStatus.isRunning&&isStart) {
			System.out.println("服务器正常关闭");
		} else {
			System.out.println("服务启动失败,已停止服务");
			System.exit(-1);
		}
	}
	/**
	 * 方法用途: 服务启动入口<br>
	 * 操作步骤: TODO<br>
	 * @param host aio模式必须指定host
	 * @param port 
	 * @param backlog 连接等待队列
	 * @return
	 */
	public static boolean start(String host,int port,int backlog) {
		System.out.println("开始启动服务器... 主机："+host+",端口："+port+",backlog："+backlog);
		try {
			ITaskFactory factory = BeanFactory.getBean(TaskFactorySelector.class);
			factory.add(host,port,backlog);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		System.out.println("服务已启动 ");
		while (ServerStatus.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	public static void stop() {
		ServerStatus.isRunning = false;
		System.out.println("已通知系统停止运行");
	}
}
