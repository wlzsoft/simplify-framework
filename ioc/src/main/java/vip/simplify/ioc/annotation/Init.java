package vip.simplify.ioc.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import vip.simplify.ioc.enums.InitTypeEnum;

/**
  * <p><b>Title:</b><i>指定容器启动时初始化顺序</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月7日 下午6:39:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月7日 下午6:39:50</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface Init {

	/**
	 * 
	 * 方法用途: 指定初始化的顺序<br>
	 * 操作步骤: 按照设定的init索引值在启动过程中执行<br>
	 * @return
	 */
	InitTypeEnum value();

}
