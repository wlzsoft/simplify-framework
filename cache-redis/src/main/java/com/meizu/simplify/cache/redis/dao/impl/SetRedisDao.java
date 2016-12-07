package com.meizu.simplify.cache.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import com.meizu.simplify.cache.dao.ISetCacheDao;
import com.meizu.simplify.cache.redis.CacheExecute;
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
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public class SetRedisDao extends BaseRedisDao<String> implements ISetCacheDao{
	
    public SetRedisDao(String modName) {
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
    	return CacheExecute.execute(key, (k,jedis)->{
    		long ret = jedis.sadd(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
            if(seconds > 0){
				jedis.expire(key, seconds);
			}
            return ret > 0;
    	},modName);
    }

    /**
     * 方法用途: 删除某个元素
     *
     * @param key
     * @param member
     * @return
     */
    public boolean srem(String key, Object member) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		long ret = jedis.srem(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
    		return ret > 0;
    	},modName);
    }

    /**
     * 方法用途: 获取整个集合
     *
     * @param key
     * @return
     */
    public Set<Object> smembers(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		Set<byte[]> set = jedis.smembers(SerializeUtil.serialize(key));
            Set<Object> result = null;
            if (CollectionUtil.isNotEmpty(set)) {
                result = new HashSet<Object>();
                for (byte[] bytes : set) {
                    result.add(SerializeUtil.unserialize(bytes));
                }
            }
            return result;
    	},modName);
        	
    }
    

    /**
     * 方法用途: 元素是否存在于集合内
     *
     * @param key
     * @param member
     * @return
     */

    public boolean sismember(String key, Object member) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		return jedis.sismember(SerializeUtil.serialize(key), SerializeUtil.serialize(member));
    	},modName);
    }

    /**
     * 方法用途: 集合大小
     *
     * @param key
     * @return
     */
    public long scard(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		long size = jedis.scard(SerializeUtil.serialize(key));
            return size;
    	},modName);
    }
    

    /**
     * 方法用途: 移除并返回集合中的一个随机元素。
     *
     * @param key
     * @return
     */
    public Object spop(String key) {
    	return CacheExecute.execute(key, (k,jedis)->{
    		 byte[] bytes = jedis.spop(SerializeUtil.serialize(key));
             return SerializeUtil.unserialize(bytes);
    	},modName);
    }

    /**
     * 方法用途: 返回集合中的一个随机元素。
     *
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
