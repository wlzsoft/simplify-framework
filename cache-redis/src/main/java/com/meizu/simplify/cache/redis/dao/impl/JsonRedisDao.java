package com.meizu.simplify.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IJsonCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;
import com.meizu.simplify.utils.JsonUtil;


/**
 * <p><b>Title:</b><i>以JSON字符缓存操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:53:52</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:53:52</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class JsonRedisDao extends BaseRedisDao<String> implements IJsonCacheDao{
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonRedisDao.class);
	
	public JsonRedisDao(String mod_name) {
		super(mod_name);
	}

	/**
	 * 
	 * 方法用途: <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p><br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getAndSet(String key, Object value) {
		Object ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Object>() {

			@Override
			public Object call(String key) {
				String str = CacheExecute.getJedis(mod_name).getSet(key, JsonUtil.ObjectToJson(value));
				if(str != null && str.length() > 0){
					return JsonUtil.JsonToObject(str);
				}
				return null;
			}
		}, mod_name);
		return ret;
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		Object ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Object>() {

			@Override
			public Object call(String key) {
				String str =  CacheExecute.getJedis(mod_name).get(key);
				if(str != null && str.length() > 0){
					return JsonUtil.JsonToObject(str);
				}
				return null;
			}
		}, mod_name);
		return ret;
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, Object value,int seconds) {
		
		Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				String ret = CacheExecute.getJedis(mod_name).set(key, JsonUtil.ObjectToJson(value));
				if(seconds > 0){
					CacheExecute.getJedis(mod_name).expire(key, seconds);
				}
				return ret.equalsIgnoreCase("OK");
			}
		}, mod_name);
		return ret;
	}
	  
    /**
     * 
     * 方法用途: <p>将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, Object value) {
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				 long ret = CacheExecute.getJedis(mod_name).setnx(key, JsonUtil.ObjectToJson(value));
		         return ret > 0;
			}
		}, mod_name);
        return ret;
    }

    /**
     * 
     * 方法用途: <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/><br>
     * 操作步骤: TODO<br>
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, Object value) {
    	Boolean ret = CacheExecute.execute(key, new ICacheExecuteCallbak<String,Boolean>() {

			@Override
			public Boolean call(String key) {
				String ret = CacheExecute.getJedis(mod_name).setex(key, seconds, JsonUtil.ObjectToJson(value));
	            return ret.equalsIgnoreCase("OK");
			}
		}, mod_name);
        return ret;
    }
    
}
