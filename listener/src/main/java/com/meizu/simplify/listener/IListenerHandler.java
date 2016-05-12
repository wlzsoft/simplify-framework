package com.meizu.simplify.listener;

import javax.servlet.ServletContextEvent;

/**
  * <p><b>Title:</b><i>基于servlet3.0容器的监听器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年5月11日 下午2:36:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年5月11日 下午2:36:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IListenerHandler {
	public void handle(ServletContextEvent sce);
}
