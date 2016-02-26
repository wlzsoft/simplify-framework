package com.meizu.simplify.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IStringCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;

/**
 * <p><b>Title:</b><i>redis  String 结构操作</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午6:03:13</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午6:03:13</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class StringRedisDao extends BaseRedisDao<String> implements IStringCacheDao{
	
	public StringRedisDao(String mod_name) {
		super(mod_name);
	}

	
	public String getAndSet(String key, String value) {
		String ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,String>() {

			@Override
			public String call(String key) {
				return CacheExecute.getJedis(mod_name).getSet(key, value);
			}
		}, mod_name);
		return ret;
	}

	public String get(String key) {
		String ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,String>() {

			@Override
			public String call(String key) {
				return CacheExecute.getJedis(mod_name).get(key);
			}
		}, mod_name);
		return ret;
	}

	public boolean set(String key, String value,int seconds) {
		Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				String ret = CacheExecute.getJedis(mod_name).set(key, value);
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				return ret.equalsIgnoreCase("OK");
			}
		}, mod_name);
		return ret;
	}
	
	  
    public boolean setnx(String key, String value) {
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				long ret = CacheExecute.getJedis(mod_name).setnx(key, value);
				return ret > 0;
			}
		}, mod_name);
    	return ret;
    }

    public boolean setex(String key, int seconds, String value) {
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				String ret = CacheExecute.getJedis(mod_name).setex(key,seconds, value);
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				return ret.equalsIgnoreCase("OK");
			}
		}, mod_name);
    	return ret;
    }

}
