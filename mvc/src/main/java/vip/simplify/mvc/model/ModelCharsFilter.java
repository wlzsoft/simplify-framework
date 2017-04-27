package vip.simplify.mvc.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p><b>Title:</b><i>表单model属性的字符串清除过滤</i></p>
 * <p>Desc: 标注于set方法上</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年3月22日 下午5:02:03</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年3月22日 下午5:02:03</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ModelCharsFilter {
	/**
	 * 
	 * 方法用途: 设置一个或多个过滤器，用于过滤特定格式的数据<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	Filter[] filters(); 

	public enum Filter {
		Script, Style, Html, iframe, trim
	}
}