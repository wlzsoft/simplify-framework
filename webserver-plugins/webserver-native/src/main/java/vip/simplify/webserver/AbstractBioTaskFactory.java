package vip.simplify.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

import vip.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>抽象的BIO任务工厂</i></p>
 * <p>Desc: TODO</p>
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
public abstract class AbstractBioTaskFactory implements ITaskFactory {
	
	protected ServerSocket serverSocket;
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param host
	 * @param port
	 * @param backlog
	 */
	@Override
	public void add(String host,int port,int backlog) throws IOException {
		ServerSocket serverSocket = null;
			serverSocket = new ServerSocket();
			this.serverSocket = serverSocket;
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
	//		ITaskFactory factory = new TaskFactory();
	}
}
