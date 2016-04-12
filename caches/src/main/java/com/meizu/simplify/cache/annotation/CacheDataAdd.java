package com.meizu.simplify.cache.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
  * <p><b>Title:</b><i>数据缓存添加标识</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午2:42:48</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午2:42:48</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface CacheDataAdd {

	/**
	 * 
	 * 方法用途: 圈定缓存的结构，或是独立的内存块，比如独立map，可以是jdk的map，也是redis等服务器的map结构数据集<br>
	 * 操作步骤: 指定value后，只会针对这个结构的分配的范围的内的数据进行操作，目前暂未实现 TODO<br>
	 * @return
	 */
	String value() default "";
	
	/**
	 * 
	 * 方法用途: 指定缓存的key值<br>
	 * 操作步骤: TODO<br>
	 * @return
	 */
	String key() default "";
	
	/**
	 * 
	 * 方法用途: 指定缓存的条件<br>
	 * 操作步骤: 满足条件的key才能被添加到缓存中<br>
	 * @return
	 */
	String condition() default "";
}
