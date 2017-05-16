package vip.simplify.ioc.annotation;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>基于BeanConfig来标识Bean类</i></p>
 * <p>Desc: 注意：1.只要类中具有对象返回值标注了BeanConfig注解，那么这个对象具有和Bean注解相同的地位和机制
 *                2.这个注解可标注在类方法和属性上，扫描器会扫描类上的这个注解，并进一步解析类中的方法和属性上的注解</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月5日 下午6:35:20</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月5日 下午6:35:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface BeanConfig {

	/**
	 * 
	 * 方法用途: 指定bean的类型<br>
	 * 操作步骤: 目前有单例和多例两种方式<br>
	 * @return
	 */
//	BeanTypeEnum type() default BeanTypeEnum.SINGLE;

	/**
	 * 
	 * 方法用途: 指定创建的唯一标识，bean的标识名<br>
	 * 操作步骤: 创建bean的时候标识起来，可用于获取bean对象<br>
	 * @return
	 */
//	String value() default "";

//	Class<?>[] attributes() default Object.class;

}
