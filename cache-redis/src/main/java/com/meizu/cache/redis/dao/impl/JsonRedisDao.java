package com.meizu.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.dao.IJsonCacheDao;
import com.meizu.cache.redis.RedisPool;
import com.meizu.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.utils.JsonUtil;

import redis.clients.jedis.ShardedJedis;


/**
 * <p><b>Title:</b><i>以JSON字符缓存操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:53:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:53:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonRedisDao extends BaseRedisDao implements IJsonCacheDao{
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonRedisDao.class);
	
	public JsonRedisDao(String mod_name) {
		super(mod_name);
	}

	/**
	 * 
	 * 方法用途: <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p><br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getAndSet(String key, Object value) {
		ShardedJedis jedis = RedisPool.getConnection(mod_name);
		try {
			String str = jedis.getSet(key, JsonUtil.ObjectToJson(value));
			if(str != null && str.length() > 0){
				return JsonUtil.JsonToObject(str);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("getAndSet error!", e);
			return null;
		} finally {
			jedis.close();
		}
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		ShardedJedis jedis = RedisPool.getConnection(mod_name);
		try {
			String str =  jedis.get(key);
			if(str != null && str.length() > 0){
				return JsonUtil.JsonToObject(str);
			}
			return null;
		} catch (Exception e) {
			LOGGER.error("get error!", e);
			return null;
		} finally {
			jedis.close();
		}
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object value,int seconds) {
		ShardedJedis jedis = RedisPool.getConnection(mod_name);
		try {
			String ret = jedis.set(key, JsonUtil.ObjectToJson(value));
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			return ret.equalsIgnoreCase("OK");
		} catch (Exception e) {
			LOGGER.error("set error!", e);
			return false;
		} finally {
			jedis.close();
		}
	}
	  
    /**
     * 
     * 方法用途: <p>将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, Object value) {
        ShardedJedis jedis = RedisPool.getConnection(mod_name);
        try {
            long ret = jedis.setnx(key, JsonUtil.ObjectToJson(value));
            return ret > 0;
        } catch (Exception e) {
            LOGGER.error("setnx error!", e);
            return false;
        } finally {
            jedis.close();
        }
    }

    /**
     * 
     * 方法用途: <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, Object value) {
        ShardedJedis jedis = RedisPool.getConnection(mod_name);
        try {
            String ret = jedis.setex(key, seconds, JsonUtil.ObjectToJson(value));
            return ret.equalsIgnoreCase("OK");
        } catch (Exception e) {
            LOGGER.error("setex error!", e);
            return false;
        } finally {
            jedis.close();
        }
    }
    
}
