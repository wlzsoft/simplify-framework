package com.meizu.dao;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月28日 下午3:17:17</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月28日 下午3:17:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

	String value() default "master";

}
