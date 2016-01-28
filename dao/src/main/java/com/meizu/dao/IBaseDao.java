package com.meizu.dao;

import java.util.List;


/**
 * 
 * <p><b>Title:</b><i>基础DAO类</i></p>
 * <p>Desc: 基础DAO类</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2014年12月22日 下午3:13:33</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2014年12月22日 下午3:13:33</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public interface IBaseDao<T> {
	
	/**
	 * 方法用途: 执行sql<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 */
	public void execute(String sql);

	/**
	 * 方法用途: 执行sql<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param args 参数对象
	 */
	public void execute(String sql, Object[] args);
	
	/**
	 * 方法用途: 执行sql <br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param args 参数对象
	 * @param types 参数映射类型
	 */
	public void execute(String sql, Object[] args, int[] types);
	
	/**
	 * 方法用途: 批量插入<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param batchArgs 参数对象列表
	 */
	public void executeBatch(String sql, List<Object[]> batchArgs);
	
	/**
	 * 方法用途: 批量插入<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param batchArgs 参数对象列表
	 * @param types 参数映射类型
	 */
	public void executeBatch(String sql, List<Object[]> batchArgs, int[] types);
	
	/**
	 * 方法用途: 保存记录返回主键<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @return 返回主键
	 */
	public KeyHolder saveRePrimarykey(String sql, Object[] args);
	
	/**
	 * 方法用途: 列表查询<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param rowMapper 对象结果映射
	 * @return 结果列表
	 */
	public List<T> queryForList(String sql, Object[] args, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 列表查询<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param types 参数映射类型
	 * @param rowMapper 对象结果映射
	 * @return 结果列表
	 */
	public List<T> queryForList(String sql, Object[] args, int[] types, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 查询返回单个对象<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param rowMapper 对象结果映射
	 * @return 结果对象
	 */
	public T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 查询返回单个对象<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param types 参数映射类型
	 * @param rowMapper 对象结果映射
	 * @return 结果对象
	 */
	public T queryForObject(String sql, Object[] args, int[] types, RowMapper<T> rowMapper);

	/**
	 * 
	 * 方法用途: 待实现，待测试<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @return
	 */
	public List<T> queryForList(String sql);

}
