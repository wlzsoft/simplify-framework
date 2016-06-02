package com.meizu;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.meizu.simplify.ioc.Startup;
public class WebServer {
	private volatile boolean isShutDowm = false;
	public static Map<String, String> config = new HashMap<String, String>();
	public static Map<String, HttpSessionImpl> sessions = new HashMap<String, HttpSessionImpl>();
	private ServerSocket server; 
	public static void main(String[] args) {
		try {
			Startup.start();
			init();
			new WebServer().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void start() {
		System.out.println("开始启动服务器 !");
		try (
				
			//1、创建http server //服务器连接
			ServerSocket server = new ServerSocket(Integer.parseInt(config.get("port")))) {
			this.server = server;
			//2、等待客户端连接，由于使用TCP协议，所以这里的客户端就是浏览器
			while (!isShutDowm) {
				
				//获取连接信息
				Socket client = server.accept();
				System.out.println("来自客户端[" + client.getRemoteSocketAddress()
						+ "] 的请求 ");
				try {
					
					/*byte[] data = new byte[20480];
					int len = client.getInputStream().read(data);
					String info = new String(data,0,len);
					System.out.println(info.trim());*/
					//对每一个客户端都启动一个线程处理
//					new Thread(new ServletDisPatcher(client)).start(); //TODO
					new Thread(new WebThread(client)).start();
				} catch (IOException e) {
					e.printStackTrace();
					stop();
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("发生异常 ， 服务器停止 ！");
			stop();
		}
	}

	public static void init() throws Exception {
		Properties props = new Properties();
		InputStream is = WebServer.class.getClassLoader().getResourceAsStream("web.properties");
		props.load(is);
		for (Object key : props.keySet()) {
			config.put((String) key, (String) props.get(key));
		}

	}
	public void stop()  {
		//应用级框架代码，不能写死依赖，后续修改成插件形式
		System.out.println("系统停止运行");
		//end
		isShutDowm = true;
		try {
			server.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
