package com.meizu.cache.redis;


import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.cache.impl.Cache;


/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月13日 下午5:31:27</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月13日 下午5:31:27</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class RedisCache implements Cache {
	

    private String name;
    
    private static final long serialVersionUID = 4133511643649980250L;
	private static Logger log = LoggerFactory.getLogger(RedisCache.class);


	/**
	 * 缓存客户端
	 */
	@Resource
	private RedisClient redisClient;
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 缓存客户端
	 */
	@Resource
	private RedisClient slaveredisClient;

	private Set<String> keySet = new HashSet<String>();
	private final int expire;

    public RedisCache(String name, int expire, RedisClient redisClient) {
      this.name = name;
      this.expire = expire;
		this.redisClient = redisClient;
    }
	
  
    @Override  
    public String getName() {  
    	return this.name;
//        return this.cache.getName();  
    }  
  
   
	
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setName(String name) {
		this.name = name;
	}


	@Override
	public Object getNativeCache() {
		return this.redisTemplate;
//      return this.cache;
	}
}

