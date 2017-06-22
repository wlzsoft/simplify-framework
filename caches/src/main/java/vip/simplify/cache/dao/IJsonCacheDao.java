package vip.simplify.cache.dao;

import com.alibaba.fastjson.TypeReference;

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
	 * 方法用途: <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * 操作步骤:<p>当key存在但不是字符串类型时，返回一个错误。 </p>
	 * 
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
	 */
	public boolean set(String key, int expireTime, VV value);
	
	  
    /**
     * 方法用途: <p>将key的值设为value，当且仅当key不存在。   </p>
     * 操作步骤:<p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p>
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, VV value);

    /**
     * 方法用途: <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * 操作步骤:<p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/>
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, VV value);
    
    /** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	public boolean delete(String key);
	
}
