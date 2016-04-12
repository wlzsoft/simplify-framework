package com.meizu.simplify.cache.dao;

import java.util.Comparator;
/**
 * <p><b>Title:</b><i>redis二分查找</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:00:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:00:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface ISearchCacheDao {

	
	/**
	 * 
	 * 方法用途: 二分查找缓存下标<br>
	 * 操作步骤: TODO<br>
	 * @param key 缓存list key
	 * @param data 需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex 结束下标
	 * @param c
	 * @return
	 */
	public  long findCacheIndex(String key, long data,long beginIndex, long endIndex, Comparator<Long> c);
	
	/**
	 * 方法用途: 二分查找缓存大于目标值最近的下标<br>
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public  long findCacheThanIndex(String key,  long data,long beginIndex, long endIndex, Comparator<Long> c);
	/**
	 * 方法用途: 二分查找紧挨data缓存前一个值(插入用)
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public  long findCacheValueForInsert(String key, long data,long beginIndex, long endIndex);

}
