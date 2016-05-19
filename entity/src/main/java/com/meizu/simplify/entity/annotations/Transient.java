package com.meizu.simplify.entity.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>忽略属性</i></p>
 * <p>Desc: 忽略参与sql生成解析，但可用于实体属性注入</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午6:07:21</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:07:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
@Documented
@Inherited
public @interface Transient {

	/**
	 * 
	 * 方法用途: 指定orm中实体对象不需要参与数据库表操作的属性的名称<br>
	 * 操作步骤: 只针对注解在实体上的方式有效<br>
	 * @return
	 */
	String[] value() default "";

}
