package vip.simplify.rpc.annotations;
import static java.lang.annotation.ElementType.FIELD;
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
@Target({TYPE,FIELD})
@Retention(RUNTIME)
public @interface ClientBean {

	String value() default "";
	
	/**
	 * 
	 * 方法用途: 服务版本，与服务提供者的版本一致<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String version() default "1.0.0";
	
	/**
	 * 
	 * 方法用途: 启动时检查提供者是否存在，true报错，false忽略<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	boolean check() default false;
	
	/**
	 * 
	 * 方法用途: 直连服务路径,服务绕过注册中心，仅开发测试使用<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String url() default "";
}
