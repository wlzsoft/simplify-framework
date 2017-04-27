package vip.simplify.net;

import java.io.IOException;

import vip.simplify.exception.StartupErrorException;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.webserver.ITaskFactory;

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
public class TaskFactorySelector implements ITaskFactory {
	
	@Inject
	private ITaskFactory taskFactory;
	
	@Override
	public void add(String host, int port, int backlog) throws IOException {
		if(taskFactory == null) {
			throw new StartupErrorException("taskFactory为空，请检查是否未指定具体的webserver实现");
		}
		taskFactory.add(host, port, backlog);
	}

}
