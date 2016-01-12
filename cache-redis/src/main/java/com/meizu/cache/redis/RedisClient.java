package com.meizu.cache.redis;

import java.io.Serializable;

import com.meizu.simplify.utils.ByteUtil;
import com.meizu.simplify.utils.ReflectionUtil;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
//@Component
public class RedisClient<T extends Serializable> {

    private ShardedJedisPool shardedJedisPool;
    protected Class<? extends RedisClient> entityClass;

    public RedisClient(){
        this.entityClass = ReflectionUtil.getSuperClassGenricType(getClass());
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        this.shardedJedisPool = shardedJedisPool;
    }

    public ShardedJedis getConnection() {
        ShardedJedis jedis=null;
        try {
            jedis=shardedJedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    public void closeConnection(ShardedJedis jedis) {
        if (null != jedis) {
            try {
                shardedJedisPool.returnResource(jedis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void put(String key,T entity){
    	 ShardedJedis jedis = null;
        try {
            jedis=shardedJedisPool.getResource();
            jedis.set(key.getBytes(), ByteUtil.ObjectToByte(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	 shardedJedisPool.returnResource(jedis);
        }
    }

    public T get(String key){
        byte[] entityByte = null;
        ShardedJedis jedis = null;
        try {
        	jedis =shardedJedisPool.getResource();
            entityByte =jedis.get(key.getBytes());
            return (T) ByteUtil.ByteToObject(entityByte);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	shardedJedisPool.returnResource(jedis);
        }
        return null;
    }

    public void remove(String key){
    	 ShardedJedis jedis = null;
        try {
            jedis=shardedJedisPool.getResource();
            jedis.del(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	shardedJedisPool.returnResource(jedis);
        }
    }

	public void put(Object key, Object value) {
		// TODO Auto-generated method stub
		
	}
}
