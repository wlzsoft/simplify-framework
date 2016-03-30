package com.meizu.simplify.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>bean的多个接口实现类的接口解析</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 下午3:02:58</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 下午3:02:58</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface HandleInterface {

	/**
	 * 
	 * 方法用途: 指定需要解析的接口的类型<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	Class<?> value();

}
