package com.meizu.cache.dao;

/**
 * <p><b>Title:</b><i>String 结构操作</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:15:56</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:15:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IStringCacheDao  {
	

	

	/**
	 * <p>将给定key的值设为value，并返回key的旧值。 </p>
	 * <p>当key存在但不是字符串类型时，返回一个错误。 </p>
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public String getAndSet(String key, String value) ;

	/**
	 * get
	 * @param key
	 * @return
	 */
	public String get(String key);

	/**
	 * set
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public boolean set(String key, String value,int seconds);
	
	  
    /**
     * <p>将key的值设为value，当且仅当key不存在。   </p>
     * <p>若给定的key已经存在，则SETNX不做任何动作。    </p>
     * <p>SETNX是”SET if Not eXists”(如果不存在，则SET)的简写。</p>
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, String value);

    /**
     * <p>将值value关联到key，并将key的生存时间设为seconds(以秒为单位) </p>
     * <p>如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <p/>
     *
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, String value);

}
