package com.meizu.mvc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>Ajax 跨域访问注解设置</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月20日 下午7:39:40</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月20日 下午7:39:40</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AjaxAccess {
	
	String allowOrigin();
	
	String allowHeaders() default "";
	
	int maxAge() default Integer.MAX_VALUE;
	
	Methods[] allowMethods() default { Methods.Get, Methods.Post };
	
	public enum Methods {
		Get, Post, Put, Delete, Xmodify
	}
}