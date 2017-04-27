package vip.simplify.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
  * <p><b>Title:</b><i>AIO任务工厂</i></p>
 * <p>Desc: 支持jdk1.7提供的第二版nio2.0新特性(异步非阻塞io，由linux底层的异步编程接口提供，性能到达极致</p>
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
public class AioTaskFactory implements ITaskFactory {
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param host
	 * @param port
	 * @param backlog
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
		serverChannel.bind(new InetSocketAddress(host, port), backlog);
		AioTaskFactory aioTaskFactory = this;
		serverChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, AioTaskFactory>() {
			@Override
			public void completed(AsynchronousSocketChannel result, AioTaskFactory attachment) {
				serverChannel.accept(aioTaskFactory, this);
				ByteBuffer dst = ByteBuffer.allocate(1024);
				result.read(dst, 1000, TimeUnit.SECONDS, attachment, new CompletionHandler<Integer, AioTaskFactory>() {
					@Override
					public void completed(Integer results, AioTaskFactory attachment) {
						dst.flip();
						byte[] barr = new byte[dst.remaining()];
						dst.get(barr);
						System.out.println(new String(barr));
						ByteBuffer src = ByteBuffer.allocate(1024);
						String resultStr = "hello aio";
						src.put(resultStr.getBytes());
						src.flip();
						Future<Integer> f = result.write(src);
						try {
							if (f.get()>0) {
								System.out.println("success");
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						} catch (ExecutionException e1) {
							e1.printStackTrace();
						}
						try {
							result.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					@Override
					public void failed(Throwable exc, AioTaskFactory attachment) {
						exc.printStackTrace();
						try {
							result.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
			@Override
			public void failed(Throwable exc, AioTaskFactory attachment) {
				exc.printStackTrace();
				try {
					serverChannel.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		while(ServerStatus.isRunning) {
			try {
				TimeUnit.SECONDS.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
