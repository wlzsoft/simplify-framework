package com.meizu.rpc.annotations;
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
	
	String version() default "1.0.0";//服务版本，建议使用两位或以上数字版本
	
	int timeout() default 1000;//远程服务调用超时时间(毫秒)
	
	int connections() default 100;//对每个提供者的最大连接数
	
	String loadbalance() default "random";//负载均衡策略，可选值：random,roundrobin,leastactive、consistenthash，分别表示：随机，轮循，最少活跃调用，一致性Hash
	
}
