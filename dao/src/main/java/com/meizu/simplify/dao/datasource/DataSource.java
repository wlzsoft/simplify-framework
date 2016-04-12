package com.meizu.simplify.dao.datasource;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p><b>Title:</b><i>sql数据库数据源设定</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年4月28日 下午3:17:17</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年4月28日 下午3:17:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DataSource {

	/**
	 * 
	 * 方法用途: 数据源指定，默认指定主数据源<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value() default "master";

}
