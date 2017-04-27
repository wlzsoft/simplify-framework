package vip.simplify.config.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>指定绑定的类型的文件或目录可以重新热加载</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午5:15:43</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午5:15:43</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface ReloadableResource {
	/**
	 * 
	 * 方法用途: 配置文件路径<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value();

	/**
	 * 
	 * 方法用途: 配置文件中属性值的前缀<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String prefix() default "";
	
	/**
	 * 方法用途: 配置文件是否存在检查<br>
	 * 操作步骤: 如果开启检查，那么如果配置文件不存在，就会抛致命错误，那么服务无法启动<br>
	 * @return
	 */
	boolean exsisCheck() default false;
}
