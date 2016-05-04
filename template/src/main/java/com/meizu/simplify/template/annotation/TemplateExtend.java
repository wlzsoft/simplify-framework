package com.meizu.simplify.template.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>用于标识类为模版类，并指定模版扩展名</i></p>
 * <p>Desc: TemplateExtend可以独立于TemplateType注解使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 上午10:14:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 上午10:14:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface TemplateExtend {

	/**
	 * 
	 * 方法用途: 指定模版文件扩展名<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String extend() default "html";

}
