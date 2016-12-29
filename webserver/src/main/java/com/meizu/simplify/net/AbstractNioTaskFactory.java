package com.meizu.simplify.net;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

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
public abstract class AbstractNioTaskFactory implements ITaskFactory{
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	public void add(String host,int port,int backlog) throws IOException{
//		创建io多路复用选择器
		Selector selector = Selector.open();
//		打开通道
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}
}
