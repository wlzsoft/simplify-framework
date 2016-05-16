package com.meizu.simplify.ioc.hook;

import java.util.List;

import com.meizu.simplify.ioc.BeanEntity;

/**
  * <p><b>Title:</b><i>bean多例钩子处理</i></p>
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
 * @param <T>实例类型
 */
public interface IBeanPrototypeHook<T> {
	/**
	 * 
	 * 方法用途: bean初始化钩子方法<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 * @return
	 */
	List<BeanEntity<T>> hook(Class<T> clazz);
}
