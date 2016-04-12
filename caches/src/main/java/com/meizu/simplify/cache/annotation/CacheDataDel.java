package com.meizu.simplify.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>数据缓存删除标识</i></p>
 * <p>Desc: 可删除指定缓存，也可以清空整个缓存</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午2:44:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午2:44:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface CacheDataDel {

	/**
	 * 
	 * 方法用途: 圈定数据集范围，确定删除数据集的访问<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String value() default "";
	/**
	 * 
	 * 方法用途: 需要删除的数据的key值<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String key() default "";
	/**
	 * 
	 * 方法用途: 是否删除key指定的所有数据集<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	boolean allEntries() default false;

}
