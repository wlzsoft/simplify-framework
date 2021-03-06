package vip.simplify.cache;


import vip.simplify.cache.enums.CacheExpireTimeEnum;
import vip.simplify.cache.enums.TimeEnum;


/**
 * 
 * <p><b>Title:</b><i>缓存操作接口</i></p>
 * <p>Desc: 
 *         伪分布式redis，需要自己实现一致性hash算法
                           序列化选型，序列化后的体积，压缩对象，序列化速度和性能
                           序列化支持的数据类型，是否支持复杂类型
                           被动失效，设置过期策略
                           主动更新，并发更新问题
                           成本问题：并发量估算，内存量估算，对缓存数据的优化达到最小空间占用</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午4:35:12</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午4:35:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public interface ICacheDao<K,V> {
	
	 /**
     * 缓存里面是否存在该key
     * @param key
     * @return
     */
    public boolean exists(K key);
    
    /** 
	 * 方法用途: 返回值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
	public V get(K key);
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	public void add(K key, V value);
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param expireTime 超时事件 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param value 对象值
	 */
	public void add(K key, int expireTime, V value);
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	public boolean set(K key, V value);
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param expireTime 超时事件 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param value 对象值
	 */
	public boolean set(K key, int expireTime,  V value);
	 
	
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	public boolean delete(K key);
	
    public void clear();
	 
	/** 
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 有冲突为TRUE无为FALSE
	 */
    public boolean isMutex(K key);

    /** 
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param expireTime 超时事件 单位是秒
	 * @return 有冲突为TRUE无为FALSE
	 */
    public boolean isMutex(K key, CacheExpireTimeEnum expireTime);
    
    /**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param expireTime 超时事件 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param seconds
	 * @return
	 */
	public long expire(String key, int expireTime, TimeEnum seconds);
	/**
	 * 
	 * 方法用途: 指定key设置过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param expireTime 超时事件 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param seconds
	 * @return
	 */
	public long expire(byte[] key, int expireTime, TimeEnum seconds);
	/**
	 * 
	 * 方法用途: 获取指定key的剩余过期时间<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param seconds
	 * @return
	 */
	public long getExpire(K key, TimeEnum seconds);

}
