package com.meizu.simplify.webserver;
/**
  * <p><b>Title:</b><i>服务状态</i></p>
 * <p>Desc: 标记服务是否存活等各个状态</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年1月16日 下午3:48:22</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年1月16日 下午3:48:22</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class ServerStatus {
	public volatile static boolean isRunning = true;
}
