package vip.simplify.cache.dao;

import com.alibaba.fastjson.TypeReference;

import java.util.Set;

/**
 * <p><b>Title:</b><i>以JSON字符缓存操作类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:15:19</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:15:19</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IJsonCacheDao<VV>  {

	/**
	 * 方法用途: 将给定key的值设为value，并返回key的旧值 <br>
	 * 操作步骤: 当key存在但不是字符串类型时，返回一个错误 <br>
	 * @param key
	 * @param value
	 * @return
	 */
	public VV getAndSet(String key, VV value);
	public <V> V getAndSet(String key, V value, Class<V> clazz);

	public VV get(String key);
	
	/**
	 * 
	 * 方法用途: 支持泛型对象转换转换<br>
	 * 操作步骤: 多个Class类型指定<br>
	 * @param key
	 * @param typeReference
	 * @return
	 */
	public <V> V get(String key, TypeReference<V> typeReference);
	
	/**
	 * 
	 * 方法用途: 不支持泛型的指定Class对象转换<br>
	 * 操作步骤: 单个Class类型指定<br>
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <V> V get(String key, Class<V> clazz);

	public boolean set(String key, VV value);

	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param expireTime 超时时间 单位是秒,出自定义失效事件外，可通过 CacheExpireTimeEnum.timesanmp() 来获取常用的枚举的时间
	 * @param value 对象值
	 *
	 */
	public boolean set(String key, int expireTime, VV value);
	
	  
    /**
     * 方法用途: 将key的值设为value，当且仅当key不存在 <br>
     * 操作步骤: 若给定的key已经存在，则SETNX不做任何动作<br>
     *  SETNX是”SET if Not eXists”(如果不存在，则SET)的简写<br>
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, VV value);

    /** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 指定要删除的键，只能指定一个
	 * @return 删除成功为TRUE失败为FALSE
	 */
	public boolean delete(String key);
	
	/** 
	 * 方法用途: 删除多个值
	 * 操作步骤: 注意：这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现<br>
	 * @param keys 指定要删除的多个键,这里指定字符串数组形式
	 * @return 删除成功,返回删除的记录数
	 */
	public Long delete(String[] keys);
	
	/** 
	 * 方法用途: 删除多个值
	 * 操作步骤: 注意：这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现<br><br>
	 * @param keys 指定要删除的多个键
	 * @return 删除成功,返回删除的记录数
	 */
	public Long delete(Set<String> keys);
	
	/**
	 * 
	 * 方法用途: 左边前缀模糊匹配key<br>
	 * 操作步骤: 注意：1.这里会查询所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现
	 *           2.这里对keys命令做了限制，只能左边前缀匹配，为了避免误删和影响性能
	 *           另外，尽管这样，也要尽量避免调用这个方法，特别数据量大的情况下，可以使用list结构或set结构代替，直接通过key完全删除list或set<br>
	 * @param key 待查询key前缀
	 * @return
	 */
	public Set<String> keys(String key);
	
	/** 
	 * 方法用途: 删除查询到的所有结果
	 * 操作步骤: 注意：1.这里会删除所有切片上的值，需要遍历所有切片，无法路由到有数据的切片，尽管部分切片上没有需要的数据，考虑是否需要调优，使用jedis自带的路由api，哈希一致性算法来实现
	 *                2.这里查询和删除分两步操作redis
	 *                3.这个方法和CommonRedisDao的delete方法重复，后续要做整合 TODO<br>
	 * @param key 待查询key前缀
	 * @return 删除成功,返回删除的记录数
	 */
	public Long searchAndDelete(String key);
	
}
