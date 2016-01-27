package com.meizu.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>依附于RequestMap,对请求参数做解析</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午7:17:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午7:17:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
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
	String defaultValue();
	
	/**
	 * 
	 * 方法用途: 参数索引位置<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String param();
	/**
	 * 
	 * 方法用途: 参数名称<br>
	 * 操作步骤: 设置请求参数的名称<br>
	 * @return
	 */
	String name() default "";
}