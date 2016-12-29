package com.meizu.simplify.net;

import java.io.IOException;

/**
  * <p><b>Title:</b><i>抽象的AIO任务工厂</i></p>
 * <p>Desc: 支持jdk1.7提供的第二版nio2.0新特性(异步非阻塞io，由linux底层的异步编程接口提供，性能到达极致</p>
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
public abstract class AbstractAioTaskFactory implements ITaskFactory{
	
	/**
	 * 方法用途: 添加一个任务<br>
	 * 操作步骤: TODO<br>
	 * @param socket
	 */
	public void add(String host,int port,int backlog) throws IOException{
	}
}
