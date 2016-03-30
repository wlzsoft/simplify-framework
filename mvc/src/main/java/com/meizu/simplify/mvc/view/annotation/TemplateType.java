package com.meizu.simplify.mvc.view.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>用于标识类为模版类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 上午10:14:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 上午10:14:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface TemplateType {

	/**
	 * 
	 * 方法用途: 用于指定模版唯一标识<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value();

}
