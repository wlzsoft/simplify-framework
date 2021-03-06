package vip.simplify.ioc.annotation;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import vip.simplify.ioc.enums.BeanTypeEnum;

/**
 * <p><b>Title:</b><i>实例创建标示</i></p>
 * <p>Desc: 注意：只要子类标注了Bean注解，那么所有父类都无需标注Bean注解，就会自动注入带有Resource注解的属性的值</p>
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
@Target({TYPE,METHOD,FIELD})
@Retention(RUNTIME)
public @interface Bean {

	/**
	 * 
	 * 方法用途: 指定bean的类型<br>
	 * 操作步骤: 目前有单例和多例两种方式<br>
	 * @return
	 */
	BeanTypeEnum type() default BeanTypeEnum.SINGLE;

	/**
	 * 
	 * 方法用途: 指定创建的唯一标识，bean的标识名<br>
	 * 操作步骤: 创建bean的时候标识起来，可用于获取bean对象<br>
	 * @return
	 */
	String value() default "";

}
