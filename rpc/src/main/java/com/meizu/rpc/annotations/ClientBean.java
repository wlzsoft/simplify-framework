package com.meizu.rpc.annotations;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>RPC客户端接口注解</i></p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2016</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月15日 下午4:45:25</p>
 * <p>Modified By:meizu-</p>
 * <p>Modified Date:2016年4月15日 下午4:45:25</p>
 * @author <a href="wanghaibin@meizu.com" title="邮箱地址">meizu</a>
 * @version Version 3.0
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ClientBean {

	String value() default "";
	
	String version() default "1.0.0";//服务版本，与服务提供者的版本一致
	
	boolean check() default true;//启动时检查提供者是否存在，true报错，false忽略
	
	String url() default "";//直连服务路径,服务绕过注册中心，仅开发测试使用
}
