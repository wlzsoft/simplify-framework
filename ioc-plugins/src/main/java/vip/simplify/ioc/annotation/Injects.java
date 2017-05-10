package vip.simplify.ioc.annotation;

import java.lang.annotation.*;

/**
 * <p><b>Title:</b><i>用于指定多个依赖注入属性Inject的配置注解</i></p>
 * <p>Desc: </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月14日 下午5:33:30</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月14日 下午5:33:30</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Injects {
	/**
	 * 
	 * 方法用途: Inject集合<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	Inject[] value();
}