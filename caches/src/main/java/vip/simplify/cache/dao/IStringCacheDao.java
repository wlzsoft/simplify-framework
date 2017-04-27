package vip.simplify.cache.dao;

/**
 * <p><b>Title:</b><i>String 结构操作</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月13日 下午5:15:56</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月13日 下午5:15:56</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
public interface IStringCacheDao  {
	
	/**
	 * 
	 * 方法用途: 将给定key的值设为value，并返回key的旧值<br>
	 * 操作步骤: 当key存在但不是字符串类型时，返回一个错误<br>
	 * @param key
	 * @param value
	 * @return
	 */
	public String getAndSet(String key, String value) ;

	public String get(String key);

	public boolean set(String key, String value,int seconds);
	
	  
    /**
     * 
     * 方法用途: 将key的值设为value，当且仅当key不存在<br>
     * 操作步骤: 若给定的key已经存在，则SETNX不做任何动作
     * SETNX是”SET if Not eXists”(如果不存在，则SET)的简写<br>
     * @param key
     * @param value
     * @return
     */
    public boolean setnx(String key, String value);

    /**
     * 
     * 方法用途: 将值value关联到key，并将key的生存时间设为seconds(以秒为单位)<br>
     * 操作步骤: 如果key 已经存在，SETEX命令将覆写旧值。   原子性(atomic)操作 <br>
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public boolean setex(String key, int seconds, String value);

}
