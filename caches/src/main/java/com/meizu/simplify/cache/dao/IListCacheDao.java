package com.meizu.simplify.cache.dao;

import java.util.List;

/**
 * <p><b>Title:</b><i>list 操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:15:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:15:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IListCacheDao  {

	/**
	 * 将一个值value插入到列表key的表头。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lpush(String key, String value);
	
	/**
	 * 将一个值value插入到列表key的表头。
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String key, String value,int seconds);
	
	/**
	 * 批量将值value插入到列表key的表头。
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String[] keys,String[] values,int seconds);
	
	/**
	 * 批量将值value插入到列表key的表头。
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String key,List<String> values,int seconds);
	

	/**
	 * 将一个 value插入到列表key的表尾。
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean rpush(String key, String value);
	
	/**
	 * 将一个 value插入到列表key的表尾。
	 * 
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean rpush(String key, String value,int seconds);
	
	public boolean rpush(String key,List<String> values,int seconds);

	/**
	 * 移除并返回列表key的头元素。 当key不存在时，返回null。
	 * 
	 * @param key
	 * @return
	 */
	public String lpop(String key);

	/**
	 * 移除并返回列表key的尾元素。 当key不存在时，返回nil。
	 * 
	 * @param key
	 * @return
	 */
	public Object rpop(String key);

	/**
	 * 返回列表key的长度。 如果key不存在，则key被解释为一个空列表，返回0. 如果key不是列表类型，返回一个错误。
	 * 
	 * @param key
	 * @return
	 */
	public long llen(String key);

	/**
	 * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定。
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, int start, int end);


	/**
	 * 根据参数count的值，移除列表中与参数value相等的元素。
	 * <p/>
	 * count的值可以是以下几种： count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count。 count < 0:
	 * 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值。 count = 0: 移除表中所有与value相等的值。
	 * 
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public long lrem(String key, int count, String value);

	/**
	 * 将列表key下标为index的元素的值设置为value。
	 * 
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean lset(String key, int index, String value);

	/**
	 * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
	 * 
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public boolean ltrim(String key, int start, int stop);

	/**
	 * 返回列表key中，下标为index的元素。
	 * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
	 * <p/>
	 * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public Object lindex(String key, int index);

}
