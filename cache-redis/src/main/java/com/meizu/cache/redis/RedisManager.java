package com.meizu.cache.redis;

import com.meizu.cache.ICacheDao;
import com.meizu.cache.ICacheManager;

import redis.clients.jedis.ShardedJedis;



/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午5:47:54</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午5:47:54</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class RedisManager implements ICacheManager {

	private String mod_name;
    public String getName() {
    	return mod_name;	
    } 
    private String getKey(String key) {
		return "name" + "_" + key;
	}
	
	public RedisManager(String mod_name) {
		this.mod_name = mod_name;
	}
    
    public ShardedJedis getClient(){
		return RedisPool.getJedisClient(mod_name);
    }
    
    public void returnClient(ShardedJedis shardedJedis){
    	RedisPool.returnJedisClient(mod_name, shardedJedis);
    }
    
    public void returnBrokenResource(ShardedJedis shardedJedis){
    	RedisPool.returnBrokenResource(mod_name, shardedJedis);
    }
    
   
    
    
    @Override
	public ICacheDao getCache(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
