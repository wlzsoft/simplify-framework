package vip.simplify.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>钩子函数标识-用于依赖注入</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月11日 上午11:00:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月11日 上午11:00:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IocHook {

	/**
	 * 
	 * 方法用途: 指定需要钩子函数处理的Class<br>
	 * 操作步骤: 此处指定依赖注入的属性的类型<br>
	 * @return
	 */
	Class<?> value();

}
