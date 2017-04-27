package vip.simplify.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>标注于接口上，在bean容器中，用于指定接口有多个实现类bean的情况下的默认bean</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 下午1:44:21</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 下午1:44:21</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface DefaultBean {

	/**
	 * 
	 * 方法用途: bean类型<br>
	 * 操作步骤: 可选值，如果不指定情况下，那么就需要解析程序解析指定具体实现类，否则以这个值指定的实现类为准<br>
	 * @return
	 */
	Class<?> value() default Object.class;

}
