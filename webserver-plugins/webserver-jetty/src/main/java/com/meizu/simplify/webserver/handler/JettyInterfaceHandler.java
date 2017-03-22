package com.meizu.simplify.webserver.handler;

import com.meizu.simplify.ioc.IInterfaceHandler;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.HandleInterface;
import com.meizu.simplify.webserver.ITaskFactory;
import com.meizu.simplify.webserver.JettyTaskFactory;

/**
  * <p><b>Title:</b><i>jetty的实现类选择器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年11月14日 上午10:58:04</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年11月14日 上午10:58:04</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
@HandleInterface(ITaskFactory.class)
public class JettyInterfaceHandler implements IInterfaceHandler{

	@Override
	public Class<?> handle() {
		return JettyTaskFactory.class;
	}
	
}