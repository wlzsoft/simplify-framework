package com.meizu.simplify.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>数据集缓存查询标识</i></p>
 * <p>Desc: 如果第一次查询发现数据未被缓存，那么直接缓存，具体CacheDataAdd注解的功能</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午2:43:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午2:43:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface CacheDataSearch {

	/**
	 * 
	 * 方法用途: 圈定数据集访问<br>
	 * 操作步骤: 若指定value值，那么可到指定value的访问内的内存块查找数据，比如map中<br>
	 * @return
	 */
	String value() default "";
	/**
	 * 
	 * 方法用途: 需要查找的数据或是数据集的key<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String key() default "";
}
