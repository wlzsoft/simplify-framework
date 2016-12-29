package com.meizu.simplify.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.meizu.simplify.ioc.Startup;
import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>引导启动</i></p>
 * <p>Desc: 使用自己实现的连接池，该连接池受到java本身api的限制，性能提升不高，但已经远远优于BioBootstrap</p>
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
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			InetSocketAddress inetSocketAddress = null;
			if(StringUtil.isBlank(host)) {
				inetSocketAddress = new InetSocketAddress(port);
			} else {
				inetSocketAddress = new InetSocketAddress(host,port);
			}
			if(backlog>0) {
				serverSocket.bind(inetSocketAddress,backlog);
			} else {
				serverSocket.bind(inetSocketAddress);
			}
			/*char c = '中';//unicode 和 utf8的区别，在java中的影响
			Charset cs = Charset.forName("GBK");
			CharBuffer cb = CharBuffer.allocate(1);
			cb.put(c);
			cb.flip();
			ByteBuffer bb = cs.encode (cb);
			System.out.println(bb.array().length);*/
			MessageHandler mh = new MessageHandler(serverSocket,1);
			for (int i=0; i<ThreadPool.getPoolSize(); i++) {
				ThreadPool.add(mh,"连接"+(i+1));
			}
			MessageHandler mh2 = new MessageHandler(serverSocket,1);
			ThreadPool.add(mh2,"连接b");
			while(isRunning) {
				try {
					TimeUnit.SECONDS.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(serverSocket != null) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public static void stop() {
		isRunning = false;
		System.out.println("已通知系统停止运行");
	}
}
