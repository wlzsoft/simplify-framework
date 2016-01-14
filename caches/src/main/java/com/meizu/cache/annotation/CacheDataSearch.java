package com.meizu.cache.annotation;
/**
  * <p><b>Title:</b><i>数据集缓存查询标识</i></p>
 * <p>Desc: 如果第一次查询发现数据未被缓存，那么直接缓存，具体CacheDataAdd注解的功能</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月12日 下午2:43:28</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月12日 下午2:43:28</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public @interface CacheDataSearch {

	String value();

	String key();

}
