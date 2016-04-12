package com.meizu.simplify.dao.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>主键策略</i></p>
 * <p>Desc: 注意，seq和auto不能同时起作用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午6:09:11</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:09:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface Key {

	/**
	 * 方法用途: 使用序列生成id<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String seq() default "";

	/**
	 * 方法用途: 自动递增，数据库自身机制<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	boolean auto() default true;

}
