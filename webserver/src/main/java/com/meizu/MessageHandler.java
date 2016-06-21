package com.meizu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.charset.Charset;

import com.meizu.simplify.utils.StringUtil;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年6月15日 下午5:50:15</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年6月15日 下午5:50:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class MessageHandler implements Runnable{
	private Socket socket;
	public MessageHandler(Socket socket) {
		this.socket = socket;
	}
	@Override
	public void run() {
		try {
			InputStream inputStream = socket.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream,Charset.forName("utf-8"));
			BufferedReader br = new BufferedReader(isr);
			String content = null;
			while(StringUtil.isNotBlank(content = br.readLine())) {
				System.out.println(content);
			}
			System.out.println("============"+socket.isClosed());
			isr.close();
			br.close();
			socket.close();
//			socket.shutdownInput();
//			socket.shutdownOutput();//使用这个方法，可以避免关闭socket，但是可以关闭输出流，这样，可以复用socket,待确认
			System.out.println("============"+socket.isClosed()+"|"+socket);
			/*while(true) {
				try {
					Thread.sleep(100000000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
