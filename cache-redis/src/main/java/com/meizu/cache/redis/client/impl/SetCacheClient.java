package com.meizu.cache.redis.client.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.client.BaseCacheClient;
import com.meizu.simplify.utils.CollectionUtil;

import redis.clients.jedis.ShardedJedis;

/**
 * SET 操作集合
 *
 */
public class SetCacheClient extends BaseCacheClient {
	private static final Logger log = LoggerFactory.getLogger(SetCacheClient.class);
    public SetCacheClient(String mod_name) {
		super(mod_name);
	}

    /**
     * 增加元素
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sadd(String key, Object value) {
       return sadd(key,value,-1);
    }
    
    /**
     * 增加元素
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean sadd(String key, Object value,int seconds) {
        ShardedJedis jedis = this.getClient();
        try {
            long ret = jedis.sadd(getByteKey(key), codec.encode(value));
            if(seconds > 0){
				jedis.expire(key, seconds);
			}
            return ret > 0;
        } catch (Exception e) {
            log.error("sadd error!", e);
            this.returnBrokenResource(jedis);
            return false;
        } finally {
            this.returnClient(jedis);
        }
    }

    /**
     * 删除某个元素
     *
     * @param key
     * @param member
     * @return
     */
    public boolean srem(String key, Object member) {
        ShardedJedis jedis = this.getClient();
        try {
            long ret = jedis.srem(getByteKey(key), codec.encode(member));
            return ret > 0;
        } catch (Exception e) {
            log.error("srem error!", e);
            this.returnBrokenResource(jedis);
            return false;
        } finally {
            this.returnClient(jedis);
        }
    }

    /**
     * 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(String key) {
        ShardedJedis jedis = this.getClient();
        Set<byte[]> set = null;
        try {
            set = jedis.smembers(getByteKey(key));
            //序列化
            Set<Object> result = null;
            if (CollectionUtil.isNotEmpty(set)) {
                result = new HashSet<Object>();
                for (byte[] bytes : set) {
                    result.add(codec.decode(bytes));
                }
            }
            return result;
        } catch (Exception e) {
            log.error("smembers error!", e);
            this.returnBrokenResource(jedis);
            return null;
        } finally {
            this.returnClient(jedis);
        }
    }
    

    /**
     * 元素是否存在于集合内
     *
     * @param key
     * @param member
     * @return
     */

    public boolean sismember(String key, Object member) {
        ShardedJedis jedis = this.getClient();
        try {
            return jedis.sismember(getByteKey(key), codec.encode(member));
        } catch (Exception e) {
            log.error("sismember error!", e);
            this.returnBrokenResource(jedis);
            return false;
        } finally {
            this.returnClient(jedis);
        }
    }

    /**
     * 集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        ShardedJedis jedis = this.getClient();
        try {
            long size = jedis.scard(getByteKey(key));
            return size;
        } catch (Exception e) {
            log.error("scard error!", e);
            this.returnBrokenResource(jedis);
            return -1;
        } finally {
            this.returnClient(jedis);
        }
    }
    

    /**
     * 移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object spop(String key) {
        ShardedJedis jedis = this.getClient();
        try {
            byte[] bytes = jedis.spop(getByteKey(key));
            return codec.decode(bytes);
        } catch (Exception e) {
            log.error("spop error!", e);
            this.returnBrokenResource(jedis);
            return null;
        } finally {
            this.returnClient(jedis);
        }
    }

    /**
     * 返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object srandmember(String key) {
        ShardedJedis jedis = this.getClient();
        try {
            byte bytes[] = jedis.srandmember(getByteKey(key));
            return codec.decode(bytes);
        } catch (Exception e) {
            log.error("srandmember error!", e);
            this.returnBrokenResource(jedis);
            return -1;
        } finally {
            this.returnClient(jedis);
        }
    }
    
}
