package vip.simplify.rpc.annotations;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>RPC服务实例注解</i></p>
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
public @interface ServerBean {

	String value() default "";
	
	/**
	 * 
	 * 方法用途: 服务版本，建议使用两位或以上数字版本<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String version() default "1.0.0";
	
	/**
	 * 
	 * 方法用途: 远程服务调用超时时间(毫秒)<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	int timeout() default 5000;
	
	/**
	 * 
	 * 方法用途: 对每个提供者的最大连接数<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	int connections() default 100;
	
	//TODO 配置文件获取
//	LoadbalanceEnum loadbalance() default LoadbalanceEnum.RANDOM;//负载均衡策略
	
}
