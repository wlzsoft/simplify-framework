package com.meizu.cache.redis.client.impl.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.client.BaseCacheClient;

import redis.clients.jedis.ShardedJedis;

/**
 * String 结构操作
 */
public class StringCacheClient extends BaseCacheClient {
	private static final Logger log = LoggerFactory
			.getLogger(StringCacheClient.class);

	public StringCacheClient(String mod_name) {
		super(mod_name);
	}

	public static void main(String[] args) {
		StringCacheClient client = new StringCacheClient("redis_ref_hosts");
		// client.set("vip_send_mail_max_msgid", 20L);
		// long max = (Long) client.get("vip_send_mail_max_msgid");
		// System.out.println(max);
	}

	/**
	 * <p>将 key 中储存的数字值增一。</p>
	 * <p>如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。</p>
	 * <p>如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。</p>
	 * <p>本操作的值限制在 64 位(bit)有符号数字表示之内。</p>
	 * 
	 * @param key
	 * @return
	 */
	public Long incr(String key) {
		ShardedJedis jedis = this.getClient();
		try {
			return jedis.incr(key);
		} catch (Exception e) {
			log.error("incr error!", e);
			 this.returnBrokenResource(jedis);
			return 0L;
		} finally {
			this.returnClient(jedis);
		}
	}

	/**
	 * <p>将 key 所储存的值加上增量 increment </p>
	 * @param key
	 * @param value
	 * @return
	 */
	public Long incrBy(String key, long value) {
		ShardedJedis jedis = this.getClient();
		try {
			return jedis.incrBy(getByteKey(key), value);
		} catch (Exception e) {
			log.error("incrBy error!", e);
			 this.returnBrokenResource(jedis);
			return 0L;
		} finally {
			this.returnClient(jedis);
		}
	}

	/**
	 * <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String getAndSet(String key, String value) {
		ShardedJedis jedis = this.getClient();
		try {
			return jedis.getSet(key, value);
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
	public String get(String key) {
		ShardedJedis jedis = this.getClient();
		try {
			return jedis.get(key);
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
	public boolean set(String key, String value,int seconds) {
		ShardedJedis jedis = this.getClient();
		try {
			String ret = jedis.set(key, value);
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
    public boolean setnx(String key, String value) {
        ShardedJedis jedis = this.getClient();
        try {
            long ret = jedis.setnx(key, value);
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
    public boolean setex(String key, int seconds, String value) {
        ShardedJedis jedis = this.getClient();
        try {
            String ret = jedis.setex(key, seconds, value);
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
