package com.meizu.simplify.mvc.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>表单model属性的作用域，生命周期</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月22日 下午5:10:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月22日 下午5:10:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ModelScope {
	/**
	 * 
	 * 方法用途: 表单属性字符集设置<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String charset() default ""; 

	/**
	 * 
	 * 方法用途: 表单数据生命周期和作用域范围设置<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	Scope scope() default Scope.page; 

	public enum Scope {
		page, cookie, session, application
	}
}