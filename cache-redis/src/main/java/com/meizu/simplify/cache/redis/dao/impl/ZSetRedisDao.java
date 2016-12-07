package com.meizu.simplify.cache.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import com.meizu.simplify.cache.redis.CacheExecute;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
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
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean zadd(String key, Object value,int seconds) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		double score = 1.0;//待处理，先给默认值 TODO 
            long ret = jedis.zadd(SerializeUtil.serialize(key),score, SerializeUtil.serialize(value));
            if(seconds > 0){
				jedis.expire(key, seconds);
			}
            return ret > 0;
    	}, modName);
    }

    /**
     * 方法用途: 删除某个元素
     * 操作步骤: TODO<br>
     * @param key
     * @param member
     * @return
     */
    public boolean zrem(String key, Object member) {
    	return CacheExecute.execute(key, (k,jedis)->{
            long ret = jedis.zrem(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
            return ret > 0;
    	}, modName);
    }

    /**
     * 方法用途: 获取整个集合
     * 操作步骤: TODO<br>
     * @param key
     * @return
     */
    public Set<Object> zrange(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		Set<String> set = jedis.zrange(key, 0, -1);//0,-1先给默认值，查全部数据，待处理 TODO 
            Set<Object> result = null;
            if (CollectionUtil.isNotEmpty(set)) {
                result = new HashSet<Object>();
                for (String bytes : set) {
                    result.add(bytes);
                }
            }
            return result;
    	}, modName);
    }
    

    /**
     * 方法用途: 元素是否存在于集合内
     * 操作步骤: TODO<br>
     * @param key
     * @param member
     * @return
     */
   /* public boolean zismember(String key, Object member) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		return jedis.zismember(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
    	},modName);
    }*/

    /**
     * 方法用途: 集合大小
     * 操作步骤: TODO<br>
     * @param key
     * @return
     */
    public long zcard(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		long size = jedis.zcard(SerializeUtil.serialize(key));
            return size;
    	},modName);
    }
    

    /**
     * 方法用途: 移除并返回
     * 操作步骤: TODO<br>
     * @param key
     * @return
     */
    public Long zrem(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		 Long bytes = jedis.zrem(SerializeUtil.serialize(key));
             return bytes;
    	},modName);
    }

    /**
     * 方法用途: 返回集合中的一个随机元素。
     * 操作步骤: TODO<br>
     * @param key
     * @return
     */
    public Object srandmember(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		 byte bytes[] = jedis.srandmember(SerializeUtil.serialize(key));
             return SerializeUtil.unserialize(bytes);
    	},modName);
    }
    
}
