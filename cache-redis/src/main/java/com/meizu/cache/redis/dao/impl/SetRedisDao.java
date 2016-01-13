package com.meizu.cache.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.utils.CollectionUtil;

import redis.clients.jedis.ShardedJedis;

/**
 * <p><b>Title:</b><i>redis SET 操作集合</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:01:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:01:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class SetRedisDao extends BaseRedisDao {
	private static final Logger log = LoggerFactory.getLogger(SetRedisDao.class);
    public SetRedisDao(String mod_name) {
		super(mod_name);
	}

    /**
     * 
     * 方法用途: 增加元素<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @return
     */
    public boolean sadd(String key, Object value) {
       return sadd(key,value,-1);
    }
    
    /**
     * 方法用途: 增加元素
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean sadd(String key, Object value,int seconds) {
        ShardedJedis jedis = client.getClient();
        try {
            long ret = jedis.sadd(getByteKey(key), codec.encode(value));
            if(seconds > 0){
				jedis.expire(key, seconds);
			}
            return ret > 0;
        } catch (Exception e) {
            log.error("sadd error!", e);
            client.returnBrokenResource(jedis);
            return false;
        } finally {
            client.returnClient(jedis);
        }
    }

    /**
     * 方法用途: 删除某个元素
     *
     * @param key
     * @param member
     * @return
     */
    public boolean srem(String key, Object member) {
        ShardedJedis jedis = client.getClient();
        try {
            long ret = jedis.srem(getByteKey(key), codec.encode(member));
            return ret > 0;
        } catch (Exception e) {
            log.error("srem error!", e);
            client.returnBrokenResource(jedis);
            return false;
        } finally {
            client.returnClient(jedis);
        }
    }

    /**
     * 方法用途: 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(String key) {
        ShardedJedis jedis = client.getClient();
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
            client.returnBrokenResource(jedis);
            return null;
        } finally {
            client.returnClient(jedis);
        }
    }
    

    /**
     * 方法用途: 元素是否存在于集合内
     *
     * @param key
     * @param member
     * @return
     */

    public boolean sismember(String key, Object member) {
        ShardedJedis jedis = client.getClient();
        try {
            return jedis.sismember(getByteKey(key), codec.encode(member));
        } catch (Exception e) {
            log.error("sismember error!", e);
            client.returnBrokenResource(jedis);
            return false;
        } finally {
            client.returnClient(jedis);
        }
    }

    /**
     * 方法用途: 集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        ShardedJedis jedis = client.getClient();
        try {
            long size = jedis.scard(getByteKey(key));
            return size;
        } catch (Exception e) {
            log.error("scard error!", e);
            client.returnBrokenResource(jedis);
            return -1;
        } finally {
            client.returnClient(jedis);
        }
    }
    

    /**
     * 方法用途: 移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object spop(String key) {
        ShardedJedis jedis = client.getClient();
        try {
            byte[] bytes = jedis.spop(getByteKey(key));
            return codec.decode(bytes);
        } catch (Exception e) {
            log.error("spop error!", e);
            client.returnBrokenResource(jedis);
            return null;
        } finally {
            client.returnClient(jedis);
        }
    }

    /**
     * 方法用途: 返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object srandmember(String key) {
        ShardedJedis jedis = client.getClient();
        try {
            byte bytes[] = jedis.srandmember(getByteKey(key));
            return codec.decode(bytes);
        } catch (Exception e) {
            log.error("srandmember error!", e);
            client.returnBrokenResource(jedis);
            return -1;
        } finally {
            client.returnClient(jedis);
        }
    }
    
}
