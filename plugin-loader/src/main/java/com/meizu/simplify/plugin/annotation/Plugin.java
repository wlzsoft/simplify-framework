package com.meizu.simplify.plugin.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.meizu.simplify.plugin.enums.PluginTypeEnum;

/**
 * <p><b>Title:</b><i>插件注解</i></p>
 * <p>Desc: 用于标识插件模块</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2017年1月16日 下午2:36:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2017年1月16日 下午2:36:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface Plugin {

	/**
	 * 
	 * 方法用途: 标记插件名称<br>
	 * 操作步骤: 可选，否认为标注的class的全类名<br>
	 * @return
	 */
	String value() default "";
	
	/**
	 * 
	 * 方法用途: 插件类型<br>
	 * 操作步骤: 必填用于标记是插件的类别<br>
	 * @return
	 */
	PluginTypeEnum type();
}
