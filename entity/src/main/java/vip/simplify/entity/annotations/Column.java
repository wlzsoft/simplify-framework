package vip.simplify.entity.annotations;

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
 * <p>Create Date:2015年3月9日 下午6:08:05</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午6:08:05</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
	
	/**
	 * 方法用途: 标记操作数据库字段的名字<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value() default "";
	/**
	 * 方法用途: 标记数据库字段是否可为空<br>
	 * 操作步骤: 默认true可以为空，false不可以为空<br>
	 * @return
	 */
	boolean nullable() default true;
	/**
	 * 方法用途: 标记数据库字段长度<br>
	 * 操作步骤: 默认255<br>
	 * @return
	 */
	int length() default 255;
	
	/**
	 * 方法用途: 指定列的自定义类型<br>
	 * 操作步骤: 默认是 Object <br>
	 * @return
	 */
	Class<?> type() default Object.class;
	
}
