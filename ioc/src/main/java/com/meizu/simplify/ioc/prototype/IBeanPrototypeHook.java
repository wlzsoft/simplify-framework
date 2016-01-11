package com.meizu.simplify.ioc.prototype;

import java.util.List;

/**
  * <p><b>Title:</b><i>bean多例钩子处理</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IBeanPrototypeHook {
	/**
	 * 
	 * 方法用途: bean初始化钩子方法<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @return
	 */
	List<?> hook(Class<?> clazz);
}
