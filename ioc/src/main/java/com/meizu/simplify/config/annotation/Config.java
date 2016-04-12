package com.meizu.simplify.config.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>配置信息依赖注入标记</i></p>
 * <p>Desc: TODO:后续迁移到其他位置</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月28日 下午5:17:11</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月28日 下午5:17:11</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Config {

	/**
	 * 
	 * 方法用途: 指定具体配置文件中的属性名，用于注入供bean中使用<br>
	 * 操作步骤: 需要依赖ioc模块
	 *           注意：默认有空值，如果是空值，那么以被注解的属性的名称为配置项的值来注入<br>
	 * @return
	 */
	String value() default "";

}
