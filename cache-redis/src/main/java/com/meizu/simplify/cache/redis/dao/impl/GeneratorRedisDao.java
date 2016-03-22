package com.meizu.simplify.cache.redis.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.cache.dao.IGeneratorCacheDao;
import com.meizu.simplify.cache.redis.dao.BaseRedisDao;
import com.meizu.simplify.cache.redis.dao.CacheExecute;
import com.meizu.simplify.cache.redis.dao.ICacheExecuteCallbak;
import com.meizu.simplify.utils.SerializeUtil;

/**
 * <p><b>Title:</b><i>redis整型数值递增器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:47:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:47:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class GeneratorRedisDao extends  BaseRedisDao<String> implements IGeneratorCacheDao<String> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(StringRedisDao.class);

	public GeneratorRedisDao() {
		super("redis_index_hosts");
	}
	
	/**
	 * 方法用途:将 key 中储存的数字值增一<br>
	 * 操作步骤: <p>如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。</p>
	 * <p>如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。</p>
	 * <p>本操作的值限制在 64 位(bit)有符号数字表示之内。</p> <br>
	 * 
	 * @param key
	 * @return
	 */
	public long incr(String key) {
		Long ret = CacheExecute.execute(key, (k,jedis) -> {
				return jedis.incr(k);
		}, mod_name);
		return ret;
	}

	/**
	 * 方法用途: 将 key 所储存的值加上增量 increment <br>
	 * 操作步骤: TODO <br>
	 * @param key
	 * @param value
	 * @return
	 */
	public long incrBy(String key, long value) {
		Long ret = CacheExecute.execute(key, (k,jedis) -> {
				return jedis.incrBy(SerializeUtil.serialize(k), value);
		}, mod_name);
		return ret;
	}
}
