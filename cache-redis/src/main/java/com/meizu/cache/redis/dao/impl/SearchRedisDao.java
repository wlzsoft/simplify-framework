package com.meizu.cache.redis.dao.impl;

import java.util.Comparator;

import redis.clients.jedis.ShardedJedis;
/**
 * <p><b>Title:</b><i>redis二分查找</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:00:41</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:00:41</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class SearchRedisDao {

	
	/**
	 * 
	 * 方法用途: 二分查找缓存下标<br>
	 * 操作步骤: TODO<br>
	 * @param key 缓存list key
	 * @param jedis
	 * @param data 需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex 结束下标
	 * @param c
	 * @return
	 */
	public static long findCacheIndex(String key, ShardedJedis jedis, long data,
			long beginIndex, long endIndex, Comparator<Long> c) {
		if (jedis == null) {
			return -1;
		}
		
		if (!jedis.exists(key)) {
			return -1;
		}

		long beginIndexValue = 0;
		long midIndexValue = 0;
		long endIndexValue = 0;
		
		String strBeginIndexValue = null;
		String strMidIndexValue = null;
		String strEndIndexValue = null;
		
		strBeginIndexValue = jedis.lindex(key, beginIndex);
		if (strBeginIndexValue == null) {
			return -1;
		}
		beginIndexValue = Long.valueOf(strBeginIndexValue);

		
		long midIndex = beginIndex + (endIndex - beginIndex) / 2;
		strMidIndexValue = jedis.lindex(key, midIndex);
		if (strMidIndexValue == null) {
			return -1;
		}
		midIndexValue = Long.valueOf(strMidIndexValue);
		
		
		strEndIndexValue = jedis.lindex(key, endIndex);
		if (strEndIndexValue == null) {
			return -1;
		}
		endIndexValue = Long.valueOf(strEndIndexValue);
		
		if(c.compare(data, beginIndexValue) > 0 || c.compare(data, endIndexValue) < 0||beginIndex > endIndex ){
			return -1;
		}
		
		if(c.compare(data, midIndexValue) < 0){
			return findCacheIndex(key,jedis,data,midIndex + 1,endIndex,c);
		}else if(c.compare(data, midIndexValue) > 0){ 
			return findCacheIndex(key,jedis,data,beginIndex,midIndex-1,c); 
		}else{
			return midIndex;
		}
	}
	
	/**
	 * 方法用途: 二分查找缓存大于目标值最近的下标<br>
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param jedis 
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public static long findCacheThanIndex(String key, ShardedJedis jedis, long data,
			long beginIndex, long endIndex, Comparator<Long> c) {
		if (jedis == null) {
			return -1;
		}
		
		if (!jedis.exists(key)) {
			return -1;
		}

		long beginIndexValue = 0;
		long midIndexValue = 0;
		long endIndexValue = 0;
		
		String strBeginIndexValue = null;
		String strMidIndexValue = null;
		String strEndIndexValue = null;
		
		strBeginIndexValue = jedis.lindex(key, beginIndex);
		if (strBeginIndexValue == null) {
			return -1;
		}
		beginIndexValue = Long.valueOf(strBeginIndexValue);

		
		long midIndex = beginIndex + (endIndex - beginIndex) / 2;
		strMidIndexValue = jedis.lindex(key, midIndex);
		if (strMidIndexValue == null) {
			return -1;
		}
		midIndexValue = Long.valueOf(strMidIndexValue);
		
		
		strEndIndexValue = jedis.lindex(key, endIndex);
		if (strEndIndexValue == null) {
			return -1;
		}
		endIndexValue = Long.valueOf(strEndIndexValue);

		
//		if (data > beginIndexValue || data < endIndexValue || beginIndex > endIndex) {
//			return -1;
//		}
		
		if(beginIndex > endIndex){
			return -1;
		}
		
		if(c.compare(beginIndexValue, data) > 0 || c.compare(endIndexValue, data) > 0){
			return endIndexValue;
		}
		
		if(c.compare(beginIndexValue, data) < 0 || c.compare(endIndexValue, data) < 0){
			return beginIndexValue;
		}
//		if(beginIndexValue > data && endIndexValue > data){
//			return endIndexValue;
//		}
//		
//		if(beginIndexValue < data && endIndexValue < data){
//			return beginIndexValue;
//		}
		
		if(c.compare(beginIndexValue, data) > 0 && c.compare(midIndexValue, data) > 0 ){
			return findCacheThanIndex(key, jedis, data, midIndex + 1, endIndex, c);
		}else if(c.compare(beginIndexValue, data) > 0 && c.compare(midIndexValue, data) < 0){
			
		}
		
		if(c.compare(data, midIndexValue) < 0){
			return findCacheIndex(key,jedis,data,midIndex + 1,endIndex,c);
		}else if(c.compare(data, midIndexValue) > 0){ 
			return findCacheIndex(key,jedis,data,beginIndex,midIndex-1,c); 
		}else{
			return midIndex;
		}
	}
	/**
	 * 方法用途: 二分查找紧挨data缓存前一个值(插入用)
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param jedis 
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public static long findCacheValueForInsert(String key, ShardedJedis jedis, long data,
			long beginIndex, long endIndex) {
		if (jedis == null) {
			return -1;
		}
		
		if (!jedis.exists(key)) {
			return -1;
		}

		long beginIndexValue = 0;
		long midIndexValue = 0;
		long endIndexValue = 0;
		
		String strBeginIndexValue = null;
		String strMidIndexValue = null;
		String strEndIndexValue = null;
		
		strBeginIndexValue = jedis.lindex(key, beginIndex);
		if (strBeginIndexValue == null) {
			return -1;
		}
		beginIndexValue = Long.valueOf(strBeginIndexValue);
		
		strEndIndexValue = jedis.lindex(key, endIndex);
		if (strEndIndexValue == null) {
			return -1;
		}
		endIndexValue = Long.valueOf(strEndIndexValue);
		
		if(beginIndexValue<=data || endIndexValue>=data){
			return -1;
		}
		
		if(endIndex-beginIndex==1){
			return beginIndexValue;
		}
		long midIndex = beginIndex + (endIndex - beginIndex) / 2;
		strMidIndexValue = jedis.lindex(key, midIndex);
		if (strMidIndexValue == null) {
			return -1;
		}
		midIndexValue = Long.valueOf(strMidIndexValue);
		
		if(midIndexValue == data){
			return -1;
		}
		
		if(midIndexValue>data){
			return findCacheValueForInsert(key, jedis, data, midIndex, endIndex);
		}else{
			return findCacheValueForInsert(key, jedis, data, beginIndex, midIndex);
		}
	}

}
