package com.meizu.cache.dao;

/**
 * 简单缓存操作类
 *
 */
public interface ISimpleCacheDao {
	
	
    /**
     * <p>将给定key的值设为value，并返回key的旧值。   </p>
     * <p>当key存在但不是字符串类型时，返回一个错误。    </p>
     *
     * @param key
     * @param value
     * @return
     */
    public Object getAndSet(String key, Object value);

    /**
     * 
     * get
     * @param key
     * @return
     */ 
    public Object get(String key);
    
    /**
     * set
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(String key, Object value);
    
   
    /**
     * set
     *
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public boolean set(String key, Object value,int seconds);
    
    /**
     * <p> 将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p>
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, Object value);

    /**
     * <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。</p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作<p/>
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, Object value);

}
