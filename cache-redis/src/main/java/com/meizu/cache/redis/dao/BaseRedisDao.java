package com.meizu.cache.redis.dao;

/**
 * 缓存操作基类
 *
 */
public abstract class BaseRedisDao {
	
	public String mod_name;
    
	public BaseRedisDao(String mod_name) {
    	this.mod_name = mod_name;
	}
	
}