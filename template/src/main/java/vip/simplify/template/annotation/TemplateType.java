package vip.simplify.template.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>用于标识类为模版类,并指定唯一标识</i></p>
 * <p>Desc: TemplateType需要配合TemplateExtend一起使用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月30日 上午10:14:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月30日 上午10:14:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface TemplateType {

	/**
	 * 
	 * 方法用途: 用于指定模版唯一标识<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value();
}
