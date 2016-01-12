package com.meizu.cache;


import com.meizu.simplify.exception.UncheckedException;


/**
 * 
 * <p><b>Title:</b><i>缓存DAO类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月12日 下午4:35:12</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月12日 下午4:35:12</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public interface ICacheDao extends java.io.Serializable{
	
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	void add(String key, Object value) throws UncheckedException;
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过ADD添加数据时不允许相同键值<br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */	
	void add(String key, int export,  Object value) throws UncheckedException;
	/** 
	 * 方法用途: 替换
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param value 对象值
	 */
	void replace(String key, Object value) throws UncheckedException;
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	void replace(String key, int export, Object value) throws UncheckedException;
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param value 对象值
	 */
//	<T> T  set(String key, T value) throws BusinessException;
	/** 
	 * 方法用途: 添加值
	 * 操作步骤: 通过SET添加数据时会替换掉以前的键对应的值<br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @param value 对象值
	 */
	<T> T set(String key, int export,  T value) throws UncheckedException;
	/** 
	 * 方法用途: 返回值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 缓存保存的对象
	 */
	Object get(String key) throws UncheckedException;
	/** 
	 * 方法用途: 删除值
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 删除成功为TRUE失败为FALSE
	 */
	boolean delete(String key) throws UncheckedException;
	/** 
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @return 有冲突为TRUE无为FALSE
	 */
	boolean isMutex(String key) throws UncheckedException;
	 /** 
	 * 方法用途: 冲突判定
	 * 操作步骤: <br>
	 * @param key 保存键
	 * @param export 超时时间
	 * @return 有冲突为TRUE无为FALSE
	 */
	boolean isMutex(String key, int export) throws UncheckedException;
	

	Object get(Object key);

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param key
	 * @param type
	 * @return
	 */
	<T> T get(Object key, Class<T> type);

	void put(Object key, Object value);

	void evict(Object key);

	void clear();

}
