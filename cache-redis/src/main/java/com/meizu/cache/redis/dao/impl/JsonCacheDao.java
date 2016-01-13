package com.meizu.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.dao.RedisCacheDao;
import com.meizu.simplify.utils.JsonUtil;

import redis.clients.jedis.ShardedJedis;


/**
 * 以JSON字符缓存操作类
 *
 */
public class JsonCacheDao extends RedisCacheDao {
	private static final Logger log = LoggerFactory.getLogger(JsonCacheDao.class);
	
	public JsonCacheDao(String mod_name) {
		super(mod_name);
	}

	public static void main(String[] args) {
		//JsonCacheClient client = new JsonCacheClient("redis_ref_hosts");
		// client.set("vip_send_mail_max_msgid", 20L);
		// long max = (Long) client.get("vip_send_mail_max_msgid");
		// System.out.println(max);
	}

	/**
	 * <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getAndSet(String key, Object value) {
		ShardedJedis jedis = this.getClient();
		try {
			String str = jedis.getSet(key, JsonUtil.ObjectToJson(value));
			if(str != null && str.length() > 0){
				JsonUtil.JsonToObject(str);
			}
			return null;
		} catch (Exception e) {
			log.error("getAndSet error!", e);
			 this.returnBrokenResource(jedis);
			return null;
		} finally {
			this.returnClient(jedis);
		}
	}

	/**
	 * get
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		ShardedJedis jedis = this.getClient();
		try {
			String str =  jedis.get(key);
			if(str != null && str.length() > 0){
				return JsonUtil.JsonToObject(str);
			}
			return null;
		} catch (Exception e) {
			log.error("get error!", e);
			 this.returnBrokenResource(jedis);
			return null;
		} finally {
			this.returnClient(jedis);
		}
	}

	/**
	 * set
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object value,int seconds) {
		ShardedJedis jedis = this.getClient();
		try {
			String ret = jedis.set(key, JsonUtil.ObjectToJson(value));
			if(seconds > 0){
				jedis.expire(key, seconds);
			}
			return ret.equalsIgnoreCase("OK");
		} catch (Exception e) {
			log.error("set error!", e);
			 this.returnBrokenResource(jedis);
			return false;
		} finally {
			this.returnClient(jedis);
		}
	}
	
	  
    /**
     * <p>将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p>
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, Object value) {
        ShardedJedis jedis = this.getClient();
        try {
            long ret = jedis.setnx(key, JsonUtil.ObjectToJson(value));
            return ret > 0;
        } catch (Exception e) {
            log.error("setnx error!", e);
            this.returnBrokenResource(jedis);
            return false;
        } finally {
            this.returnClient(jedis);
        }
    }

    /**
     * <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/>
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, Object value) {
        ShardedJedis jedis = this.getClient();
        try {
            String ret = jedis.setex(key, seconds, JsonUtil.ObjectToJson(value));
            return ret.equalsIgnoreCase("OK");
        } catch (Exception e) {
            log.error("setex error!", e);
            this.returnBrokenResource(jedis);
            return false;
        } finally {
            this.returnClient(jedis);
        }
    }
    
}
