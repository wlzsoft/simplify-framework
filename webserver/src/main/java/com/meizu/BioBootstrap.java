package com.meizu;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.meizu.simplify.ioc.Startup;
import com.meizu.simplify.net.Bootstrap;
import com.meizu.simplify.utils.StringUtil;
/**
 * <p><b>Title:</b><i>未使用连接池</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月29日 下午12:53:32</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月29日 下午12:53:32</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BioBootstrap {
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
			
			while (isRunning) {
				Socket client = serverSocket.accept();
				System.out.println("来自客户端[" + client.getRemoteSocketAddress()
						+ "] 的请求 ");
				/*byte[] data = new byte[20480];
				int len = client.getInputStream().read(data);
				String info = new String(data,0,len);
				System.out.println(info.trim());*/
				//对每一个客户端都启动一个线程处理
//					new Thread(new ServletDisPatcher(client)).start(); //TODO
				new Thread(new WebThread(client)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
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
		return true;
	}
	
	public static void stop() {
		isRunning = false;
		System.out.println("已通知系统停止运行");
	}
	
}
