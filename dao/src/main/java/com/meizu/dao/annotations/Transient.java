package com.meizu.dao.annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午6:07:21</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:07:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
public @interface Transient {

	String[] value() default "";

}
