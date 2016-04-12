package com.meizu.simplify.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>可在controller重复标注requestMap注解</i></p>
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
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RequestMaps {
	/**
	 * 
	 * 方法用途: RequestMap集合<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	RequestMap[] value();
}