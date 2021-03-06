package vip.simplify.ioc.annotation;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import vip.simplify.ioc.enums.BeanTypeEnum;

/**
 * <p><b>Title:</b><i>类级别标识</i></p>
 * <p>Desc: 用于类中静态属性注入，区别于@Bean ，@StaticType也只能标注在Class上面，但是不会创建实例
 *          标注了StaticType的子类，那么所有父类也是相当于标注了StaticType</p>
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
@Target(TYPE)
@Retention(RUNTIME)
public @interface StaticType {

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
