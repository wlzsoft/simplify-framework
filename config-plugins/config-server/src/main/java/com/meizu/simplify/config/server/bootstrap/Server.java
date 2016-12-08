package com.meizu.simplify.config.server.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.ioc.Startup;
import com.meizu.simplify.utils.DateUtil;


/**
 * <p><b>Title:</b><i>启动服务</i></p>
 * <p>Desc:TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年8月6日 下午1:53:05</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年8月6日 下午1:53:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class Server {
	private static Logger LOGGER = LoggerFactory.getLogger(Server.class);
	public static void main(String[] args) {
		LOGGER.info("系统服务正在开启中 ...."+args);
		Startup.start();
		LOGGER.info("系统服务已经启动 ,时间："+DateUtil.getCurrentDate().toString());
		while (true) {
			try {
				Thread.sleep(1000*60*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
