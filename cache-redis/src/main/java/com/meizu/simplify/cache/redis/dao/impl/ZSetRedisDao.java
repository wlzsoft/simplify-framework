package com.meizu.simplify.cache.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.ISetCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.utils.CollectionUtil;
import com.meizu.simplify.utils.SerializeUtil;

/**
 * <p><b>Title:</b><i>redis ZSET 操作集合</i></p>
 * <p>Desc: 比set集合多一个功能，有序的，可以排序</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:01:18</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:01:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class ZSetRedisDao extends BaseRedisDao<String>{
	private static final Logger LOGGER = LoggerFactory.getLogger(ZSetRedisDao.class);
    public ZSetRedisDao(String modName) {
		super(modName);
	}

    /**
     * 
     * 方法用途: 增加元素<br>
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @return
     */
    public boolean zadd(String key, Object value) {
       return zadd(key,value,-1);
    }
    
    /**
     * 方法用途: 增加元素
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean zadd(String key, Object value,int seconds) {
        double score = 1.0;//待处理，先给默认值 TODO 
        try {
            long ret = CacheExecute.getJedis(modName).zadd(SerializeUtil.serialize(key),score, SerializeUtil.serialize(value));
            if(seconds > 0){
				CacheExecute.getJedis(modName).expire(key, seconds);
			}
            return ret > 0;
        } catch (Exception e) {
            LOGGER.error("zadd error!", e);
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
    public boolean zrem(String key, Object member) {
        
        try {
            long ret = CacheExecute.getJedis(modName).zrem(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
            return ret > 0;
        } catch (Exception e) {
            LOGGER.error("zrem error!", e);
            return false;
        }
    }

    /**
     * 方法用途: 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> zrange(String key) {
        
        Set<String> set = null;
        try {
            set = CacheExecute.getJedis(modName).zrange(key, 0, -1);//0,-1先给默认值，查全部数据，待处理 TODO 
            //序列化
            Set<Object> result = null;
            if (CollectionUtil.isNotEmpty(set)) {
                result = new HashSet<Object>();
                for (String bytes : set) {
                    result.add(bytes);
                }
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("zrange error!", e);
        }
        return null;
    }
    

    /**
     * 方法用途: 元素是否存在于集合内
     *
     * @param key
     * @param member
     * @return
     */

   /* public boolean zismember(String key, Object member) {
        
        try {
            return CacheExecute.getJedis(modName).zismember(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
        } catch (Exception e) {
            LOGGER.error("zismember error!", e);
            return false;
        }
    }*/

    /**
     * 方法用途: 集合大小
     *
     * @param key
     * @return
     */
    public long zcard(String key) {
        
        try {
            long size = CacheExecute.getJedis(modName).zcard(SerializeUtil.serialize(key));
            return size;
        } catch (Exception e) {
            LOGGER.error("zcard error!", e);
            return -1;
        }
    }
    

    /**
     * 方法用途: 移除并返回
     *
     * @param key
     * @return
     */
    public Long zrem(String key) {
        try {
            Long bytes = CacheExecute.getJedis(modName).zrem(SerializeUtil.serialize(key));
            return bytes;
        } catch (Exception e) {
            LOGGER.error("zrem error!", e);
        }
        return null;
    }

    /**
     * 方法用途: 返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object srandmember(String key) {
        
        try {
            byte bytes[] = CacheExecute.getJedis(modName).srandmember(SerializeUtil.serialize(key));
            return SerializeUtil.unserialize(bytes);
        } catch (Exception e) {
            LOGGER.error("srandmember error!", e);
            return -1;
        }
    }
    
}
