package vip.simplify.webserver;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <p><b>Title:</b><i>服务器线程,一个请求一个线程</i></p>
 * <p>Desc: HTTP1.1相对于HTTP1.0的影响.
HTTP1.1最大的一个改变就是提供了长连接,这样HTTP不再是一次请求,一次连接的协议了,只要HTTP的connection不关闭,一次HTTP连接可以支持任意多次request/reponse处理. 当WEB Client与WEB Server建立连接时,客户端可以采用长连接,也就是说Client会一直保持对WEB Server的连接(例如:Browser对一个网站保持当连接,知道Browser关闭或最终退出该网站). 旧的WEB Server会为每一个Http连接分配一个active的Thread,这样当Client的数量增大时,Server端Thread Pool的最大容量也需要相应增大,但Thread是相当耗内存的,一个不小心就会导致Server端NotEnoughMemory...
基于HTTP1.1,大部分支持Servlet2.X的WEB容器都采用的NIO去接收和处理请求. 当Client和Server端建立连接时,Server端并不分配一个Thread给HTTP连接.直到Server端收到Client端发送的Request时, Server才开始为该Request分配Thread(注意:这里不是为HTTP连接分配Thread).
这样当大量的Client建立长连接与Server进行交互时,Server无需维持一个Thread给inactive的HTTP长连接, 每个Servlet在doReceived()时其实对应的是一个active Request,而不是HTTPConnection本身. 这样Server端所需的最大Thread数大大地减少了.
AJAX的影响
1. Request的数量爆炸性增加增加
过去WEB Browser打开一个Web page,只需要和Web Server端建立一个HTTP连接.但AJAX技术出现以后,一个Web page上可能有多个与Web Server的连接,而且Ajax request通常是十分频繁的,Server接收到的Request数量大大增长了, 这样原先NIO的技术已经不能很好的支持基于Ajax的服务了.
Servlet 3.0的异步处理就能够解决上面的问题.
Servlet3.0的solution:
当request发送到Server端时,servlet的doReceived()将request放进一个queue里,然后doReceived结束.这个时候server并没有关闭response,Client端一直在等server端response的内容. Server端维护自己的ThreadPool,当ThreadPool里有idle的Thread,就从queue里取出一个request,分配idle的Thread给request,并进行处理.
2. Server端推送信息 @WebServlet(value={"/test"}, asyncSupported=true)  
在Web2.0的应用中, Ajax可用通过不断的发送Request来获取Server端某种信息的变化,但这种实现会产生大量的Client请求. 当前推荐的方法是,让Server端自己推送信息变化给Client.
因为Servlet3.0提供了异步处理的方式, Request提交给Server以后, Server可以为Request注册一个Listener,由Listener去monitor信息的变化,当信息发生变化时,由Listener负责把信息变化发送给Cient(Listener关闭HTTP response).
 </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月1日 下午6:43:45</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月1日 下午6:43:45</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MessageRunnable implements Runnable {
	private ServerSocket serverSocket;
	public MessageRunnable(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}
	
	@Override
	public void run() {
		while(ServerStatus.isRunning) {
			HttpResponse response = null;
			try {
				//接收请求
				//线程中获取连接并通讯-开始
				Socket socket = serverSocket.accept();
				System.out.println("来自客户端["+socket.getRemoteSocketAddress()+"]的请求,由线程"+Thread.currentThread().getName());
				InputStream inputStream = socket.getInputStream();
				//线程中获取连接并通讯-结束
				response = MessageHandler.parseMessage(socket,null, inputStream);
				if(response == null) {
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
				if(response != null) {
					response.setStatusCode("500");
				}
			}
		}
	}

}
