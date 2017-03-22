package com.meizu.simplify.cache.redis.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IListCacheDao;
import com.meizu.simplify.cache.redis.CacheExecute;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.CollectionUtil;

import redis.clients.jedis.BinaryClient.LIST_POSITION;


/**
 * <p><b>Title:</b><i>list 操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:56:12</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:56:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ListRedisDao extends BaseRedisDao<String> implements IListCacheDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListRedisDao.class);
	
	public ListRedisDao(String modName) {
		super(modName);
	}

	/**
	 * 
	 * 方法用途: 将一个值value插入到列表key的表头<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean lpush(String key, String value) {
		return lpush(key,value,-1);
	}
	
	/**
	 * 
	 * 方法用途: 将一个值value插入到列表key的表头<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String key, String value,int seconds) {
		Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
				long length = jedis.lpush(k, value);
				if(seconds > 0){
					jedis.expire(k, seconds);
				}
				return length > 0;
		}, modName);
		return ret;
	}
	
	/**
	 * 
	 * 方法用途: 批量将值value插入到列表key的表头<br>
	 * 操作步骤: TODO 代码不是很规范，处理有问题，待优化<br>
	 * @param keys
	 * @param values
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String[] keys,String[] values,int seconds) {
		for(int i=0,len=keys.length;i<len;i++){
			String value = values[i];
			CacheExecute.execute(keys[i], (k,jedis) ->  {
					Long result = jedis.lpush(k, value);
					if(seconds > 0){
						jedis.expire(k, seconds);
					}
					if(result >=0) {
						return true;
					}
					return false;
			}, modName);
		}
		return true;
	}
	
	/**
	 * 
	 * 方法用途: 批量将值value插入到列表key的表头<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param values
	 * @param seconds
	 * @return
	 */
	public boolean lpush(String key,List<String> values,int seconds) {
		
		if(CollectionUtil.isEmpty(values)){
			throw new UncheckedException("插入List集合不能为空");
		}
		
		Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
			for(int i=0,len=values.size();i<len;i++){
				String value = values.get(i);
				jedis.lpush(k, value);
			}
			if(seconds > 0){
				jedis.expire(k, seconds);
			}
			return true;
		}, modName);
		return ret;
	}
	
	/**
	 * 
	 * 方法用途: 将一个 value插入到列表key的表尾<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean rpush(String key, String value) {
		return rpush(key,value,-1);
	}
	
	/**
	 * 
	 * 方法用途: 将一个 value插入到列表key的表尾<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean rpush(String key, String value,int seconds) {
		
		Boolean ret = CacheExecute.execute(key, (k,jedis) ->  {
				long length = jedis.rpush(k, value);
				if(seconds > 0){
					jedis.expire(k, seconds);
				}
				return length > 0;
		}, modName);
		return ret;
	}
	
	/**
	 * 
	 * 方法用途: 将一个 value插入到列表key的表尾<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param values
	 * @param seconds
	 * @return
	 */
	public boolean rpush(String key,List<String> values,int seconds) {
		
		if(CollectionUtil.isEmpty(values)){
			throw new UncheckedException("插入List集合不能为空");
		}
		
		try{
			CacheExecute.execute(key, (k, jedis) -> {
				for (int i = 0, len = values.size(); i < len; i++) {
					String value = values.get(i);
					jedis.rpush(k, value);
				}
				if(seconds > 0){
					jedis.expire(k, seconds);
				}
				return true;
			}, modName);
		}catch(Exception e){
			LOGGER.error("rpush error!", e);
			return false;
		}
		return true;
		
	}

	/**
	 * 
	 * 方法用途: 移除并返回列表key的头元素。 当key不存在时，返回null<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public String lpop(String key) {
		try {
			return CacheExecute.execute(key, (k, jedis) -> jedis.lpop(k), modName);
		} catch (Exception e) {
			LOGGER.error("lpop error!", e);
			return null;
		}
	}

	/**
	 * 
	 * 方法用途: 移除并返回列表key的尾元素。 当key不存在时，返回nil<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public String rpop(String key) {
		try {
			return CacheExecute.execute(key,(k,jedis)->jedis.rpop(k),modName);
		} catch (Exception e) {
			LOGGER.error("rpop error!", e);
			return null;
		}
	}

	/**
	 * 
	 * 方法用途: 返回列表key的长度。 如果key不存在，则key被解释为一个空列表，返回0. 如果key不是列表类型，返回一个错误<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public long llen(String key) {
		try {
			long length = CacheExecute.execute(key,(k,jedis)->jedis.llen(k),modName);
			return length;
		} catch (Exception e) {
			LOGGER.error("llen error!", e);
			return -1;
		}
	}

	/**
	 * 
	 * 方法用途: 返回列表key中指定区间内的元素，区间以偏移量start和stop指定<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key, int start, int end) {
		try {
			List<String> list = CacheExecute.execute(key,(k,jedis)->jedis.lrange(k, start, end),modName);
			return list;
		} catch (Exception e) {
			LOGGER.error("lrange error!", e);
			return null;
		}
	}

	/**
	 * 
	 * 方法用途: 根据参数count的值，移除列表中与参数value相等的元素。
	 * <p/>
	 * count的值可以是以下几种： count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count。 count < 0:
	 * 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值。 count = 0: 移除表中所有与value相等的值<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param count
	 * @param value
	 * @return
	 */
	public long lrem(String key, int count, String value) {
		try {
			long ret = CacheExecute.execute(key,(k,jedis)->jedis.lrem(k, count, value),modName);
			return ret;
		} catch (Exception e) {
			LOGGER.error("lrem error!", e);
			return 0;
		}
	}

	/**
	 * 
	 * 方法用途: 将列表key下标为index的元素的值设置为value<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param index
	 * @param value
	 * @return
	 */
	public boolean lset(String key, int index, String value) {
		try {
			String ret = CacheExecute.execute(key,(k,jedis)->jedis.lset(k, index, value),modName);
			return ret.equalsIgnoreCase("ok");
		} catch (Exception e) {
			LOGGER.error("lset error!", e);
			return false;
		}
	}

	/**
	 * 
	 * 方法用途: 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param start
	 * @param stop
	 * @return
	 */
	public boolean ltrim(String key, int start, int stop) {
		try {
			String ret = CacheExecute.execute(key,(k,jedis)->jedis.ltrim(k, start, stop),modName);
			return ret.equalsIgnoreCase("ok");
		} catch (Exception e) {
			LOGGER.error("ltrim error!", e);
			return false;
		}
	}

	/**
	 * 
	 * 方法用途: 返回列表key中，下标为index的元素。
	 * 下标(index)参数start和stop都以0为底，也就是说，以0表示列表的第一个元素，以1表示列表的第二个元素，以此类推。
	 * <p/>
	 * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param index
	 * @return
	 */
	public Object lindex(String key, int index) {
		try {
			return CacheExecute.execute(key,(k,jedis)->jedis.lindex(k, index),modName);
		} catch (Exception e) {
			LOGGER.error("lindex error!", e);
			return null;
		}
	}

	/**
	 * 
	 * 方法用途: 将值value插入到列表key当中，位于值pivot之前或之后
	 * <p/>
	 * 当pivot不存在于列表key时，不执行任何操作。
	 * <p/>
	 * 当key不存在时，key被视为空列表，不执行任何操作。
	 * <p/>
	 * 如果key不是列表类型，返回一个错误<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param position
	 * @param pivot
	 * @param value
	 * @return 如果命令执行成功，返回插入操作完成之后，列表的长度。 如果没有找到pivot，返回-1。 如果key不存在或为空列表，返回0
	 */
	public long linsert(String key, LIST_POSITION position, String pivot,String value) {
		try {
			long length = CacheExecute.execute(key, (k,jedis)->jedis.linsert(k, position,pivot, value), modName);
			return length;
		} catch (Exception e) {
			LOGGER.error("linsert error!", e);
			return -1;
		}
	}

}
