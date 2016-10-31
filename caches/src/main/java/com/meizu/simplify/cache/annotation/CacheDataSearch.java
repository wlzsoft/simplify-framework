package com.meizu.simplify.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.enums.CacheFormatEnum;

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
	
	/**
	 * 
	 * 方法用途: 查不到缓存记录，是否缓存当前查询结果集<br>
	 * 操作步骤: 默认缓存,设置为false不缓存<br>
	 * @return
	 */
	boolean isAdd() default true;
	
	/**
	 * 
	 * 方法用途: 缓存数据存储格式<br>
	 * 操作步骤: TODO 是否可以剥离和CacheDataAdd注解合并，不要多出编写这个方法<br>
	 * @return
	 */
	CacheFormatEnum format() default CacheFormatEnum.BINARY;
	
	/**
	 * 
	 * 方法用途: 缓存有效时间设置<br>
	 * 操作步骤: TODO 是否可以剥离和CacheDataAdd注解合并，不要多出编写这个方法<br>
	 * @return
	 */
	CacheExpireTimeEnum expireTime() default CacheExpireTimeEnum.CACHE_EXP_FOREVER;
}
