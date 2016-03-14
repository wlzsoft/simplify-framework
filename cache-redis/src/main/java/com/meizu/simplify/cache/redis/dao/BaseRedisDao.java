package com.meizu.simplify.cache.redis.dao;

import java.io.Serializable;

import com.meizu.simplify.cache.enums.CacheExpireTimeEnum;
import com.meizu.simplify.cache.enums.TimeEnum;
import com.meizu.simplify.cache.redis.exception.RedisException;

/**
 * <p><b>Title:</b><i>缓存操作基类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月15日 上午10:05:06</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月15日 上午10:05:06</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 * @param <K>
 */
public abstract class BaseRedisDao<K extends Serializable>  {
	
	public String mod_name;
	public BaseRedisDao(String mod_name) {
		this.mod_name = mod_name;
	}
	
	/**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param export
	 * @param seconds
	 * @return
	 */
	public long expire(K key, CacheExpireTimeEnum export, TimeEnum seconds) {
		
		Long ret = CacheExecute.execute(key,k ->  {
  				if(k instanceof String) {
  					return CacheExecute.getJedis(mod_name).expire(k.toString(), export.timesanmp());
  				} else if(k instanceof byte[]){
  					return CacheExecute.getJedis(mod_name).expire((byte[])k, export.timesanmp());
  				} else {
  					throw new RedisException("无效key");
  				}
  		},mod_name);
		return ret;
	}
	/**
	 * 
	 * 方法用途: 获取指定key的剩余过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long getExpire(K key, TimeEnum seconds) {
		return 0L;
	}
}