package com.meizu.simplify.net;

import java.io.IOException;

import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.webserver.ITaskFactory;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年3月1日 下午5:25:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年3月1日 下午5:25:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class TaskFactorySelector implements ITaskFactory{
	
	@Resource
	private ITaskFactory taskFactory;
	
	@Override
	public void add(String host, int port, int backlog) throws IOException {
		taskFactory.add(host, port, backlog);
	}

}
