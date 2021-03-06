package vip.simplify.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>依附于RequestMap,对请求参数做解析</i></p>
 * <p>Desc: TODO:注意目前的param和name是二选一的状态，后续会独立使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午7:17:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午7:17:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestParam {
	
	/**
	 * 
	 * 方法用途: 默认参数值<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String defaultValue() default "";
	
	/**
	 * 
	 * 方法用途: 参数索引位置-一般情况下，路径的参数索引值从1开始<br>
	 * 操作步骤: 注意：默认参数的索引位置从0开始，而真正的路径参数的索引位置从1开始
	 *                位置为0的参数值其实是请求的路径值<br>
	 * @return
	 */
	int index() default 0;
	/**
	 * 
	 * 方法用途: 参数名称<br>
	 * 操作步骤: 设置请求参数的名称<br>
	 * @return
	 */
	String name() default "";
}