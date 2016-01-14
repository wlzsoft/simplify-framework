package com.meizu.cache.annotation;
/**
  * <p><b>Title:</b><i>数据缓存添加标识</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午2:42:48</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午2:42:48</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public @interface CacheDataAdd {

	String key();

	String condition() default "";

	String value();

}
