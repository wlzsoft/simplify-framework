package com.meizu.simplify.cache.dao;

import java.io.Serializable;

/**
 * <p><b>Title:</b><i>整型数值递增器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年8月6日 下午7:43:20</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年8月6日 下午7:43:20</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface IGeneratorCacheDao<K extends Serializable> {
	/**
	 * 
	 * 方法用途: key中存储的值每次递增1<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public long incr(K key); 
	
	/**
	 * 
	 * 方法用途: key中存储的值每次递增数值value<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public long incrBy(K key, long value);
}
