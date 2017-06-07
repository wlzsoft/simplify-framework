package vip.simplify.dao.orm;

import java.io.Serializable;
import java.util.List;

import vip.simplify.dao.Query;
import vip.simplify.entity.IdEntity;
import vip.simplify.ioc.annotation.DefaultBean;


/**
 * 
 * <p><b>Title:</b><i>基础DAO接口 </i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月9日 下午5:29:23</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月9日 下午5:29:23</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型
 * @param <PK> PK类型 ，如：String、Long、Integer 等
 */
@DefaultBean
public interface IDao<T extends IdEntity<Serializable,Integer>, PK extends Serializable> {
	
	/**
	 * 方法用途: 执行sql<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 */
//	public void execute(String sql);

	/**
	 * 方法用途: 执行sql<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param args 参数对象
	 */
//	public void execute(String sql, Object[] args);
	
	/**
	 * 方法用途: 执行sql <br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param args 参数对象
	 * @param types 参数映射类型
	 */
//	public void execute(String sql, Object[] args, int[] types);
	
	/**
	 * 方法用途: 批量处理<br>
	 * 操作步骤: 更新，删除，修改<br>
	 * @param sql SQL语句
	 * @param batchArgs 参数对象列表
	 */
//	public void executeBatch(String sql, List<Object[]> batchArgs);
	
	/**
	 * 方法用途: 批量处理<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param batchArgs 参数对象列表
	 * @param types 参数映射类型
	 */
//	public void executeBatch(String sql, List<Object[]> batchArgs, int[] types);
	
     
	/**
	 * 
	 * 方法用途: 新增记录(会将序列生成的ID,注入)<br>
	 * 操作步骤: 保存（持久化）对象<br>
	 * @param ob  要持久化的对象
	 * @return 是否插入成功
	 */
	boolean save(T ob);

	/**
	 *
	 * 方法用途: 新增记录(会将序列生成的ID,注入)<br>
	 * 操作步骤: 保存（持久化）对象<br>
	 * @param ob  要持久化的对象
	 * @param isAllField 是否全字段
	 * @return 是否插入成功
	 */
	boolean save(T ob,boolean isAllField);

    /**
     * 方法用途: 批量新增记录(会将序列生成的ID,注入)<br>
     * 操作步骤: TODO<br>
     * @param list
     */
    void save(List<T> list);
    
	/**
	 * 
	 * 方法用途: 更新或保存<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @return
	 */
    boolean saveOrUpdate(T t);
    
    /**
     * @param t
     * @param isAllField 是否全字段更新
     * @return 
     * @return boolean返回类型 
     * @throws
     */
    boolean saveOrUpdate(T t,Boolean isAllField);
	/**
	 * 方法用途: 根据属性批量删除业务实体<br>
	 * 操作步骤: TODO<br>
	 * @param name 属性名
	 * @param value 属性值
	 */
	Integer remove(String name, Object value);
	/**
	 * 方法用途: 根据ID删除业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param id 待删除业务实体ID
	 * @return 删除的对象数量
	 */
	Integer remove(PK id);
    /**
     * 方法用途: 根据ids进行批量删除<br>
     * 操作步骤: TODO<br>
     * @param  ids 待删除的业务实体id列表
     * @return 删除记录数
     */
    Integer remove(List<PK> ids);
     
	/**
	 * 方法用途: 根据ID批量删除业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param ids 待删除业务实体ID数组 
	 * @return 删除的对象数量
	 */
	Integer remove(PK[] ids);
    /**
     * 
     * 方法用途: 更新-默认全字段更新<br>
     * 操作步骤: TODO<br>
     * @param t 要持久化的对象
     * @return 执行成功的记录个数
     */
    Integer update(T t);
     
    /**
     * 方法用途: 批量更新<br>
     * 操作步骤: TODO<br>
     * @param list
     */
    void update(List<T> list);
    
    /**
	 * 方法用途: 删除业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param entity 待删除业务实体
	 * @return  返回删除记录数
	 */
	Integer remove(T entity);
	
	/**
	 * 方法用途: 清理当前Session。<br>
	 * 操作步骤: TODO<br>
	 */
//	void clear();
	/**
	 * 方法用途: 保存业务实体。（复用已有的ID键值时使用）<br>
	 * 操作步骤: TODO<br>
	 * @param entity 待保存的业务实体
	 */
//	void replicate(T entity);
	/**
	 * 方法用途: 合并业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param entity 待更新业务实体
	 * @return 返回更新后的业务实体（持久状态的）。
	 */
//	T merge(T entity);
     
    /**
     * 方法用途:根据ID获取对象<br>
     * 操作步骤: TODO<br>
     * @param id 指定的唯一标识符
     * @return  指定的唯一标识符对应的持久化对象，如果没有对应的持久化对象，则返回null。
     */
    T findById(PK id);
    

	/**
	 * 方法用途: 通过id数组查找对于的实体对象<br>
	 * 操作步骤: TODO<br>
	 * @param idArr  id数组
	 * @return 
	 */
	List<T> findByIds(PK[] idArr);
	/**
	 * 方法用途: 通过ids[例：1,2,3]查找对于的实体对象<br>
	 * 操作步骤: TODO<br>
	 * @param ids 逗号隔开的id
	 * @return
	 */
	List<T> findByIds(String ids);
	/**
	 * 
	 * 方法用途: 通过属性值获取对象列表<br>
	 * 操作步骤: TODO<br>
	 * @param name 字段属性值
	 * @param values 逗号隔开的多个值
	 * @return
	 */
	List<T> findByMutil(String name, String values);
	/**
	 * 方法用途: 根据属性的值查找唯一的业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param name  属性名
	 * @param value 属性值
	 * @return 返回指定唯一的业务实体，如果没有找到则返回null。
	 */
	T findUnique(String name, Object value);
	/**
	 * 方法用途: 查找唯一业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param criteria 查询条件
	 * @return  返回唯一业务实体，如果没有找到返回null。
	 */
	T findUnique(Query<T> criteria);
	
	/**
	 * 方法用途: 查询返回单个对象<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param rowMapper 对象结果映射
	 * @return 结果对象
	 */
//	public T findUnique(String sql, Object[] args, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 查询返回单个对象<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param types 参数映射类型
	 * @param rowMapper 对象结果映射
	 * @return 结果对象
	 */
//	public T findUnique(String sql, Object[] args, int[] types, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 判断是否存在属性重复的业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param entity 待判断的业务实体
	 * @param propNames  属性名，可以多个属性名用","分割
	 * @return 如果存在重复的业务实体返回false，否则返回true。
	 */
//	Boolean isUnique(T entity, String propNames);
	
	/**
     * 获取所有的对象
     * @return 返回指定类型的所有业务实体。
     */
    List<T> findAll();
     
	/**
	 * 
	 * 不分页查询
	 * 
	 * @param param 查询参数
	 * @param sort 排序字段名
	 * @param isDesc 是否降序 [ 排序方式（升序(asc)或降序(desc)]
	 * @return 查询结果列表
	 */
	List<T> findBy(T param, String sort, boolean isDesc);
	/**
	 * 方法用途: 通过属性查找列表<br>
	 * 操作步骤: TODO<br>
	 * @param name 属性名
	 * @param value 属性值
	 * @return 返回属性值相符的业务实体集合，如果没有找到返回一个空的集合。
	 */
	List<T> findBy(String name,Object value);
	
	/**
	 * 方法用途: 根据属性的值查找业务实体并进行排序。<br>
	 * 操作步骤: TODO<br>
	 * @param param
	 * @return
	 */
	List<T> findBy(T param);
	/**
	 * 方法用途: 获取指定类型的所有业务实体并进行排序<br>
	 * 操作步骤: TODO<br>
	 * @param orderBy 排序的属性名
	 * @param isDesc 是否降序
	 * @return 返回排序后的指定类型的所有业务实体
	 */
//	List<T> findAll(String orderBy, Boolean isDesc);

	/**
	 * 方法用途: 根据查询条件进行分页查询<br>
	 * 操作步骤: TODO<br>
	 * @param pageNo 要查询的页号
	 * @param pageSize 每页数据个数
	 * @param criteria 查询条件
	 * @return 返回查询得到的分页对象
	 */
//	Page<T> findPage(int pageNo, int pageSize,Criteria criteria);
	
	/**
	 * 方法用途: 根据查询条件进行分页查询。<br>
	 * 操作步骤: TODO<br>
	 * @param sort 排序字段名
	 * @param orderBy 排序方式（升序(asc)或降序(desc)
	 * @param pageNo 要查询的页号
	 * @param pageSize 每页数据个数
	 * @param criteria 查询条件
	 * @return 返回查询得到的分页对象。
	 */
//	Page<T> findPage( String sort, String orderBy,int pageNo, int pageSize,Criteria criteria);
	

	/**
	 * 方法用途: 列表查询<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param rowMapper 对象结果映射
	 * @return 结果列表
	 */
//	public List<T> find(String sql, Object[] args, RowMapper<T> rowMapper);
	
	/**
	 * 方法用途: 列表查询<br>
	 * 操作步骤: <br>
	 * @param sql SQL语句
	 * @param objs 参数对象
	 * @param types 参数映射类型
	 * @param rowMapper 对象结果映射
	 * @return 结果列表
	 */
//	public List<T> find(String sql, Object[] args, int[] types, RowMapper<T> rowMapper);
	/**
	 * 
	 * 方法用途: 待实现，待测试<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @return
	 */
//	public List<T> find(String sql);
	
	/**
	 * 获取满足查询参数条件的数据总数
	 * 
	 * @param param 查询参数
	 * @return 数据总数
	 */
	Integer count(T param);
	/**
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param criteria
	 * @return
	 */
//	Integer count(Criteria criteria);
	
	/**
	 * 方法用途: 获取查询所能获得的对象总数。<br/>
	 * 本函数只能自动处理简单的sql语句,复杂的sql查询请另行编写count语句查询。<br>
	 * 操作步骤: TODO<br>
	 * @param sql 查询语句 
	 * @param values 查询参数
	 * @return 返回查询结果总数。
	 */
//	Integer count(String sql, Object... values);

	
	/**
	 * 方法用途: 执行count查询获得记录总数。<br>
	 * 操作步骤: TODO<br>
	 * @param page
	 * @return 返回记录总数。
	 */
//	Integer count(Page<T> page);

	public void flushStatements();
	
	
  
	
////全文检索功能start--------------------------未实现---------------------------------------------
	
	
	/**
	 * 
	 * 方法用途: 获取mybatis的全文搜索Session。<br>
	 * 操作步骤: TODO<br>
	 * @return 返回mybatis的全文搜索Session。
	 */
//	public FullTextSession getFullTextSession() {
//		return Search.getFullTextSession(getSession());
//	}
	
	/**
	 * 
	 * 方法用途: 创建全文搜索查询条件<br>
	 * 操作步骤: TODO<br>
	 * @return 返回全文搜索查询条件。
	 */
//	@Override
//	public FullTextCriteria createFullTextCriteria() {
//	}

	/**
	 * 
	 * 方法用途: 根据全文搜索查询条件进行全文搜索。<br>
	 * 操作步骤: TODO<br>
	 * @param criteria 全文搜索查询条件
	 * @return 返回符合查询条件的业务实体列表。
	 */
//	@Override
//	public List<T> searchBy(FullTextCriteria criteria) {
//	}

	/**
	 * 
	 * 方法用途: 全文搜索指定类型的所有业务实体。<br>
	 * 操作步骤: TODO<br> 
	 * @return 返回指定类型的所有业务实体。
	 */
//	@Override
//	public List<T> searchAll() {
//	}

	/**
	 * 
	 * 方法用途: 全文搜索指定类型的所有业务实体并进行排序。<br>
	 * 操作步骤: TODO<br>
	 * @param orderBy 排序的属性名
	 * @param isAsc  是否升序
	 * @param type 类型
	 * @return 返回排序后的指定类型的所有业务实体。
	 */
//	@Override
//	public List<T> searchAll(String orderBy, Boolean isAsc, org.apache.lucene.search.SortField.Type type) {
//	}
//	@Deprecated
//	public List<T> searchAll(String orderBy, Boolean isAsc, Integer type) {
//	}

	/**
	 * 
	 * 方法用途: 全文搜索唯一业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param criteria   全文搜索查询条件
	 * @return 返回唯一业务实体，如果没有找到返回null。
	 */
//	@Override
//	public T searchUnique(FullTextCriteria criteria) {
//	}

	/**
	 * 
	 * 方法用途: 根据属性的值全文搜索唯一的业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param name 属性名
	 * @param value  属性值
	 * @return 返回唯一业务实体，如果没有找到则返回null。
	 */
//	@Override
//	public T searchUnique(String name, Object value) {
//	}

	/**
	 * 
	 * 方法用途: 根据全文搜索查询条件进行分页全文搜索。<br>
	 * 操作步骤: TODO<br> 
	 * @param criteria  全文搜索查询条件
	 * @param pageNo 待获取的页数
	 * @param pageSize 每页的记录数
	 * @return 返回搜索得到的分页对象。
	 */
//	@Override
//	public Page<T> searchPage(FullTextCriteria criteria, Integer pageNo,
//			Integer pageSize) {
//	}

	/**
	 * 
	 * 方法用途: 重建全文索引。<br>
	 * 操作步骤: TODO<br> 
	 * @param sync 是否同步创建
	 */
//	@Override
//	public void rebuildIndex(Boolean sync) {
//	}

	/**
	 * 
	 * 方法用途: 获取查询所能获得的对象总数。<br>
	 * 操作步骤: TODO<br>  
	 * @param criteria  全文搜索查询对象
	 * @return 返回查询结果总数。
	 */
//	@Override
//	public Integer count(FullTextCriteria criteria) {
//	}

	////全文检索功能end--------------------------未实现---------------------------------------------
	
	  /*public IDao<T,PK> setMaxResults(int maxResult);
	
		public int getMaxResults();

		public IDao<T,PK> setFirstResult(int startPosition);

		public int getFirstResult();

		public IDao<T,PK> setHint(String hintName, Object value);
		
		public IDao<T,PK> setParameter(String name, Object value); 
		
		public IDao<T,PK> setParameter(int position, Object value);
		
		public Object getParameterValue(String name);
		
		public Object getParameterValue(int position);
		
		public <TT> TT unwrap(Class<TT> cls);*/
		/*public Map<String, Object> getHints();
		public <T> Query setParameter(Parameter<T> param, T value);
		public Query setParameter(Parameter<Calendar> param, Calendar value,TemporalType temporalType);
		public Query setParameter(Parameter<Date> param, Date value,TemporalType temporalType);
		public Query setParameter(String name, Calendar value,TemporalType temporalType); 
		public Query setParameter(String name, Date value, TemporalType temporalType);
		public Query setParameter(int position, Calendar value,	TemporalType temporalType);
		public Query setParameter(int position, Date value,	TemporalType temporalType);
		public Set<Parameter<?>> getParameters();
		public Parameter<?> getParameter(String name);
		public <T> Parameter<T> getParameter(String name, Class<T> type);
		public Parameter<?> getParameter(int position);
		public <T> Parameter<T> getParameter(int position, Class<T> type);
		public boolean isBound(Parameter<?> param);
		public <T> T getParameterValue(Parameter<T> param);
		public Query setFlushMode(FlushModeType flushMode);
		public FlushModeType getFlushMode();
		public Query setLockMode(LockModeType lockMode);
		public LockModeType getLockMode();*/

}
