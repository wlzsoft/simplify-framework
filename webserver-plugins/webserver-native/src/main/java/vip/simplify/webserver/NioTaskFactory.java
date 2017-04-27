package vip.simplify.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
  * <p><b>Title:</b><i>抽象的NIO任务工厂</i></p>
 * <p>Desc: 支持jdk1.4提供的第一版nio新特性(同步非阻塞io，在linux服务器上底层使用select和poll，虽然支持编程方便了，性能提升了，加入Selector这个io多路复用选择器，但是底层基本没有大的变化，连接数受到限制)，并支持jdk1.5的优化用于支持uninx的epoll,最大连接数得到最大解放，只要linux能支持，就可以一直创建连接的文件句柄，最高达到1万，这个和操作系统配置有关，如果达不到，需要排查是否linux的设置问题</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月29日 下午3:51:53</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月29日 下午3:51:53</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class NioTaskFactory implements ITaskFactory {
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
//		创建io多路复用选择器
		Selector selector = Selector.open();
//		打开通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8090), backlog);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while(ServerStatus.isRunning) {
//			int selecCount = selector.select(1000);
			int selecCount = selector.select();
			if(selecCount == 0) {
				continue;
			}
			Set<SelectionKey> selectKeySet = selector.selectedKeys();
			System.out.println("size:"+selectKeySet.size());
			StringBuilder sb = new StringBuilder();
			Iterator<SelectionKey> iterator = selectKeySet.iterator();
			while (iterator.hasNext()) {
				SelectionKey selectionKey = (SelectionKey) iterator.next();
				
//			}
//			for (SelectionKey selectionKey : selectKeySet) {
//				selectKeySet.remove(selectionKey);
				sb.append(selectionKey.isValid()+","+selectionKey.isAcceptable()+","+selectionKey.isReadable());
				iterator.remove();
				if(selectionKey.isValid()) {
					if(selectionKey.isAcceptable()) {
						ServerSocketChannel acceptChannel = (ServerSocketChannel)selectionKey.channel();
						System.out.println("ServerSocketChannel@"+Integer.toHexString(serverSocketChannel.hashCode()));
						System.out.println("ServerSocketChannel@"+Integer.toHexString(acceptChannel.hashCode()));
						SocketChannel socket = acceptChannel.accept();
						if(socket == null) {
							System.out.println(acceptChannel.keyFor(selector));
//							acceptChannel.close();//关闭导致服务端通道关闭,无法接收请求，并且会断开与所有客户端的连接,这里禁止关闭
							continue;
						} else {
							System.out.println("SocketChannel@"+Integer.toHexString(socket.hashCode()));
							System.out.println("socket:"+acceptChannel.keyFor(selector)+","+selectionKey);
						}
						socket.configureBlocking(false);
						socket.register(selector, SelectionKey.OP_READ);
					} else if(selectionKey.isReadable()) {
						SocketChannel readableChannel = (SocketChannel)selectionKey.channel();
						ByteBuffer byteBuffer = ByteBuffer.allocate(1024*10);
						int readCount = readableChannel.read(byteBuffer);
						if(readCount>0) {
							byteBuffer.flip();
							if(byteBuffer.hasRemaining()) {
								byte[] byteArr = new byte[byteBuffer.remaining()];
								byteBuffer.get(byteArr);
								String msg = new String(byteArr,"utf-8");
								System.out.println(msg);
							}
							HttpResponse response = new HttpResponse(null,readableChannel);
//							HttpRequest request = new HttpRequest();
							String body = "hello";
							response.setStatus(200);
							response.setReason("success");
							response.setBody(body.toCharArray());
//							HttpRoute.route(request, response);
							response.sendToClientByNio();
						} else if(readCount < 0){//浏览器关闭请求
//							readableChannel.close();
//							selectionKey.cancel();
						}
						readableChannel.close();
					}
				}
			}
			System.out.println(sb.toString());
		}
	}
}
