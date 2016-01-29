package com.meizu.simplify.cache.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.ISetCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.SerializeUtil;

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
public class SetRedisDao extends BaseRedisDao implements ISetCacheDao{
	private static final Logger LOGGER = LoggerFactory.getLogger(SetRedisDao.class);
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
        
        try {
            long ret = jedis.sadd(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
            if(seconds > 0){
				jedis.expire(key, seconds);
			}
            return ret > 0;
        } catch (Exception e) {
            LOGGER.error("sadd error!", e);
            return false;
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
        
        try {
            long ret = jedis.srem(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
            return ret > 0;
        } catch (Exception e) {
            LOGGER.error("srem error!", e);
            return false;
        }
    }

    /**
     * 方法用途: 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(String key) {
        
        Set<byte[]> set = null;
        try {
            set = jedis.smembers(SerializeUtil.serialize(key));
            //序列化
            Set<Object> result = null;
            if (CollectionUtil.isNotEmpty(set)) {
                result = new HashSet<Object>();
                for (byte[] bytes : set) {
                    result.add(SerializeUtil.unserialize(bytes));
                }
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("smembers error!", e);
            return null;
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
        
        try {
            return jedis.sismember(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
        } catch (Exception e) {
            LOGGER.error("sismember error!", e);
            return false;
        }
    }

    /**
     * 方法用途: 集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key) {
        
        try {
            long size = jedis.scard(SerializeUtil.serialize(key));
            return size;
        } catch (Exception e) {
            LOGGER.error("scard error!", e);
            return -1;
        }
    }
    

    /**
     * 方法用途: 移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object spop(String key) {
        
        try {
            byte[] bytes = jedis.spop(SerializeUtil.serialize(key));
            return SerializeUtil.unserialize(bytes);
        } catch (Exception e) {
            LOGGER.error("spop error!", e);
            return null;
        }
    }

    /**
     * 方法用途: 返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object srandmember(String key) {
        
        try {
            byte bytes[] = jedis.srandmember(SerializeUtil.serialize(key));
            return SerializeUtil.unserialize(bytes);
        } catch (Exception e) {
            LOGGER.error("srandmember error!", e);
            return -1;
        }
    }
    
}
