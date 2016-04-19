package com.meizu.simplify.ioc.hook;

import com.meizu.simplify.ioc.BeanEntity;

/**
  * <p><b>Title:</b><i>bean单例钩子处理</i></p>
 * <p>Desc: 用于bean的多实例创建定制操作</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午10:51:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午10:51:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IBeanHook {
	/**
	 * 
	 * 方法用途: bean初始化钩子方法<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @return
	 */
	BeanEntity<?> hook(Class<?> clazz);
}
