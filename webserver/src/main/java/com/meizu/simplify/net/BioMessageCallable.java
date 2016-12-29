package com.meizu.simplify.net;

import java.net.Socket;
import java.util.concurrent.Callable;

import com.meizu.HttpResponse;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年12月29日 下午4:34:22</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年12月29日 下午4:34:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class BioMessageCallable implements Callable<Integer> {
	private Socket socket;
	public BioMessageCallable(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public Integer call() throws Exception {
		HttpResponse response = null;
		try {
			response = MessageHandler.parseMessage(socket, socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			if(response != null) {
				response.setStatusCode("500");
			}
		}
		return null;
	}

}
