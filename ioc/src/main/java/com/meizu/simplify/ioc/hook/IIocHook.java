package com.meizu.simplify.ioc.hook;

import java.lang.reflect.Field;

/**
  * <p><b>Title:</b><i>注入控制钩子处理</i></p>
 * <p>Desc: 用于依赖注入的定制操作</p>
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
public interface IIocHook {
	/**
	 * 
	 * 方法用途: 注入处理钩子方法<br>
	 * 操作步骤: TODO<br>
	 * @param clazz 当前待注入属性的所属的Class对象
	 * @param field 当前待注入属性元数据Field类型对象
	 * @return 注入的资源名称：一般是@Resource(name="xxxx")中的xxxx的值
	 */
	String hook(Class<?> clazz,Field field);
}
