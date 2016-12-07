package com.meizu.simplify.cache.redis.dao.impl;

import java.util.Comparator;

import com.meizu.simplify.cache.dao.ISearchCacheDao;
import com.meizu.simplify.cache.redis.CacheExecute;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
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
public class SearchRedisDao extends BaseRedisDao<String> implements ISearchCacheDao{

	
	public SearchRedisDao(String modName) {
		super(modName);
	}

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
	public long findCacheIndex(String key,  long data,	long beginIndex, long endIndex, Comparator<Long> c) {
		return CacheExecute.execute(key, (k,jedis)->{
			if (jedis == null) {
				return -1L;
			}
			
			if (!jedis.exists(key)) {
				return -1L;
			}

			long beginIndexValue = 0;
			long midIndexValue = 0;
			long endIndexValue = 0;
			
			String strBeginIndexValue = null;
			String strMidIndexValue = null;
			String strEndIndexValue = null;
			
			strBeginIndexValue = jedis.lindex(key, beginIndex);
			if (strBeginIndexValue == null) {
				return -1L;
			}
			beginIndexValue = Long.valueOf(strBeginIndexValue);

			
			long midIndex = beginIndex + (endIndex - beginIndex) / 2;
			strMidIndexValue = jedis.lindex(key, midIndex);
			if (strMidIndexValue == null) {
				return -1L;
			}
			midIndexValue = Long.valueOf(strMidIndexValue);
			
			
			strEndIndexValue = jedis.lindex(key, endIndex);
			if (strEndIndexValue == null) {
				return -1L;
			}
			endIndexValue = Long.valueOf(strEndIndexValue);
			
			if(c.compare(data, beginIndexValue) > 0 || c.compare(data, endIndexValue) < 0||beginIndex > endIndex ){
				return -1L;
			}
			
			if(c.compare(data, midIndexValue) < 0){
				return findCacheIndex(key,data,midIndex + 1,endIndex,c);
			}else if(c.compare(data, midIndexValue) > 0){ 
				return findCacheIndex(key,data,beginIndex,midIndex-1,c); 
			}else{
				return midIndex;
			}
		}, modName);
		
	}
	
	/**
	 * 方法用途: 二分查找缓存大于目标值最近的下标<br>
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public  long findCacheThanIndex(String key, long data,long beginIndex, long endIndex, Comparator<Long> c) {
		
		return CacheExecute.execute(key, (k,jedis)->{
			if (jedis == null) {
				return -1L;
			}
			
			if (!jedis.exists(key)) {
				return -1L;
			}

			long beginIndexValue = 0;
			long midIndexValue = 0;
			long endIndexValue = 0;
			
			String strBeginIndexValue = null;
			String strMidIndexValue = null;
			String strEndIndexValue = null;
			
			strBeginIndexValue = jedis.lindex(key, beginIndex);
			if (strBeginIndexValue == null) {
				return -1L;
			}
			beginIndexValue = Long.valueOf(strBeginIndexValue);

			
			long midIndex = beginIndex + (endIndex - beginIndex) / 2;
			strMidIndexValue = jedis.lindex(key, midIndex);
			if (strMidIndexValue == null) {
				return -1L;
			}
			midIndexValue = Long.valueOf(strMidIndexValue);
			
			
			strEndIndexValue = jedis.lindex(key, endIndex);
			if (strEndIndexValue == null) {
				return -1L;
			}
			endIndexValue = Long.valueOf(strEndIndexValue);

			
//			if (data > beginIndexValue || data < endIndexValue || beginIndex > endIndex) {
//				return -1L;
//			}
			
			if(beginIndex > endIndex){
				return -1L;
			}
			
			if(c.compare(beginIndexValue, data) > 0 || c.compare(endIndexValue, data) > 0){
				return endIndexValue;
			}
			
			if(c.compare(beginIndexValue, data) < 0 || c.compare(endIndexValue, data) < 0){
				return beginIndexValue;
			}
//			if(beginIndexValue > data && endIndexValue > data){
//				return endIndexValue;
//			}
//			
//			if(beginIndexValue < data && endIndexValue < data){
//				return beginIndexValue;
//			}
			
			if(c.compare(beginIndexValue, data) > 0 && c.compare(midIndexValue, data) > 0 ){
				return findCacheThanIndex(key, data, midIndex + 1, endIndex, c);
			}else if(c.compare(beginIndexValue, data) > 0 && c.compare(midIndexValue, data) < 0){
				
			}
			
			if(c.compare(data, midIndexValue) < 0){
				return findCacheIndex(key,data,midIndex + 1,endIndex,c);
			}else if(c.compare(data, midIndexValue) > 0){ 
				return findCacheIndex(key,data,beginIndex,midIndex-1,c); 
			}else{
				return midIndex;
			}
		},modName);
		
		
	}
	/**
	 * 方法用途: 二分查找紧挨data缓存前一个值(插入用)
	 * 操作步骤: TODO<br>  
	 * @param key   缓存list key
	 * @param data  需要查找的数据
	 * @param beginIndex 开始下标
	 * @param endIndex   结束下标
	 * @return
	 */
	public  long findCacheValueForInsert(String key, long data,	long beginIndex, long endIndex) {
		return CacheExecute.execute(key, (k,jedis)->{
			if (jedis == null) {
				return -1L;
			}
			
			if (!jedis.exists(key)) {
				return -1L;
			}

			long beginIndexValue = 0;
			long midIndexValue = 0;
			long endIndexValue = 0;
			
			String strBeginIndexValue = null;
			String strMidIndexValue = null;
			String strEndIndexValue = null;
			
			strBeginIndexValue = jedis.lindex(key, beginIndex);
			if (strBeginIndexValue == null) {
				return -1L;
			}
			beginIndexValue = Long.valueOf(strBeginIndexValue);
			
			strEndIndexValue = jedis.lindex(key, endIndex);
			if (strEndIndexValue == null) {
				return -1L;
			}
			endIndexValue = Long.valueOf(strEndIndexValue);
			
			if(beginIndexValue<=data || endIndexValue>=data){
				return -1L;
			}
			
			if(endIndex-beginIndex==1){
				return beginIndexValue;
			}
			long midIndex = beginIndex + (endIndex - beginIndex) / 2;
			strMidIndexValue = jedis.lindex(key, midIndex);
			if (strMidIndexValue == null) {
				return -1L;
			}
			midIndexValue = Long.valueOf(strMidIndexValue);
			
			if(midIndexValue == data){
				return -1L;
			}
			
			if(midIndexValue>data){
				return findCacheValueForInsert(key, data, midIndex, endIndex);
			}else{
				return findCacheValueForInsert(key, data, beginIndex, midIndex);
			}
		},modName);
	}
}
