package vip.simplify.dao.orm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.simplify.config.annotation.Config;
import vip.simplify.dao.Query;
import vip.simplify.dao.datasource.ConnectionManager;
import vip.simplify.dao.dto.SqlDTO;
import vip.simplify.dao.dto.WhereDTO;
import vip.simplify.dao.enums.BatchOperator;
import vip.simplify.dao.exception.BaseDaoException;
import vip.simplify.dao.invoke.ISqlMethodSelector;
import vip.simplify.dao.orm.base.CommonSqlBuilder;
import vip.simplify.dao.orm.base.ISqlDataCallback;
import vip.simplify.dao.orm.base.SQLExecute;
import vip.simplify.entity.IdEntity;
import vip.simplify.entity.annotations.Column;
import vip.simplify.entity.annotations.Key;
import vip.simplify.entity.annotations.Table;
import vip.simplify.entity.annotations.Transient;
import vip.simplify.entity.page.Page;
import vip.simplify.ioc.annotation.Bean;
import vip.simplify.ioc.annotation.Inject;
import vip.simplify.ioc.enums.BeanTypeEnum;
import vip.simplify.utils.ReflectionUtil;
import vip.simplify.utils.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * 
 * <p><b>Title:</b><i> 基础泛型DAO实现类</i></p>
 * <p>Desc:1. 注意sql注入的问题
 *         2.考虑jdbc4.0规范，或是直接使用特定mysql二进制协议，不考虑跨数据库</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午1:42:18</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午1:42:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型 
 * @param <PK>  PK类型 ，如：String、Long、Integer 等
 */
@Bean(type= BeanTypeEnum.PROTOTYPE)
public class Dao<T extends IdEntity<Serializable,Integer>, PK extends Serializable>   implements IDao<T, PK> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected Class<T> entityClass;
	
	/**
	 * 主键列名
	 */
	private String pkName;
	
	/**
	 * 是否自增主键
	 */
	private boolean isAutoPk = false;

	/**
	 * columnName=FieldName
	 */
	protected Map<String, String> currentColumnFieldNames = new LinkedHashMap<String, String>();

	private Map<String, String> columnsMeta = new LinkedHashMap<String, String>();//create dll sql 使用
	
	protected SQLBuilder<T> sqlBuilder;
	
	@Config("system.isMycat")
    private boolean isMycat = false;

	@Inject
	protected ISqlMethodSelector selector;
	
	@Inject
	private ConnectionManager connectionManager;
	
	/**
	 * @param entityClass  业务实体类
	 */
	public Dao(Class<T> entityClass) {
		this.entityClass = entityClass;
		DaoInit(entityClass);
	}
	
	/**
	 * 
	 * 方法用途: 初始化dao信息<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 */
	private void DaoInit(Class<T> entityClass) {
		buildFieldInfo(entityClass);
		
		Table table = entityClass.getAnnotation(Table.class);
		if (null == table) {
			throw new RuntimeException("类" + entityClass + "需要指定@Table注解!");
		}
		List<String> columnArr = new ArrayList<>(currentColumnFieldNames.keySet());
		List<String> otherIdColumn = new ArrayList<>();
//		for (String columnName : columnArr) { //TODO 不使用迭代器的方式访问ArrayList，可以带来更好的遍历性能
		for (int i = 0; i < columnArr.size(); i++) {
			String columnName = columnArr.get(i);
			if(!columnName.equals(pkName)) {
				otherIdColumn.add(columnName);
			}
		}
		
/*		Stream.of(columnArr)
//		.parallel()//并行,这里不可以使用并行处理，要保证顺序
		.filter(columnName -> !columnName.equals(pkName)).forEach(columnNames -> otherIdColumn.addAll(columnNames));
*/		
		/*Collections.sort(otherIdColumn,String::compareToIgnoreCase);
		String [] copyListToArray = Stream.of(otherIdColumn).toArray(String[]::new); 
		//类似hadoop中的map 和 reduce，用于并行计算
		Stream.of(otherIdColumn).map(ArrayList<String>::new).peek(System.out::println).findFirst();*/
		
		sqlBuilder = new SQLBuilder<T>(otherIdColumn,columnArr,
				table.name(), pkName,columnsMeta );
	}
	
	private void buildClassTransientInfo(Class<? super T> entityClass,List<Transient> list) {
		Transient trans = entityClass.getAnnotation(Transient.class);
		if(trans != null) {
			list.add(trans);
		}
		if(entityClass.getSuperclass() != Object.class && entityClass.getSuperclass() != null) {
			buildClassTransientInfo(entityClass.getSuperclass(),list);
		}
	}
	
	/**
	 * 方法用途: 无论有多少超类，能递归判断和提取<br>
	 * 操作步骤: TODO<br>
	 * @param clazz
	 */
	private void buildFieldInfo(Class<? super T> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		List<Transient> trans = new ArrayList<>();
		buildClassTransientInfo(entityClass,trans);
		getFieldInfo(fields,trans);
		if(clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
			buildFieldInfo(clazz.getSuperclass());
		}
	}
	
	/**
	 * 
	 * 方法用途: 抽取字段和属性信息<br>
	 * 操作步骤: TODO<br>
	 * @param fields
	 * @param transAnno
	 */
	private void getFieldInfo(Field[] fields, List<Transient> transAnno) {
		List<String> transArr = new ArrayList<String>();
		if(transAnno != null) {
			for (Transient trans : transAnno) {
				transArr.addAll(Arrays.asList(trans.value()));
			}
		}
		for (Field field : fields) {
			boolean isTransient = validTransient(transArr.toArray(new String[transArr.size()]),field);
			if(isTransient) {
				continue;
			}
			
			String columnName = null;
			Column tableColumn = field.getAnnotation(Column.class);
//			如果没标示Column注解或是Column.value的值是空的，那么默认使用全大写属性名，否知使用注解指定的值
			if (null != tableColumn&& StringUtil.isNotBlank(tableColumn.value())) {
				columnName = tableColumn.value();
//				currentColumnFieldNames.put(field.getName(), field.getName());//fix bug:  需要考虑未转大写之前的属性名添加到元数据中
			} else {
				columnName = field.getName();
			}
			//@a map集合中判断是否Entity的实现类中已经有字段，有就continue,不去找父级的字段 start
			//父类同名的字段全部失效，以实现类为准
			if(currentColumnFieldNames.get(columnName)!=null) {
				continue;
			}
			//@a end
			//create dll start
			columnsMeta.put(columnName,field.getType().getName());
			//create dll end
			currentColumnFieldNames.put(columnName, field.getName());
			if (field.isAnnotationPresent(Key.class)) {
				// 取得ID的列名
				pkName = columnName;
				Key primaryKey = field.getAnnotation(Key.class);
				if(primaryKey != null&&primaryKey.auto()) {//决定是否启用手动设置的值
					isAutoPk = true;
				}
			}
		}
	}
	
	/**
	 * 
	 * 方法用途: 验证Transient注解<br>
	 * 操作步骤: TODO<br>
	 * @param transArr
	 * @param field
	 * @return
	 */
	public boolean validTransient(String[] transArr,Field field) {
		
		String preField = Modifier.toString(field.getModifiers());
		if(preField.contains("final")) {
			return true;
		}
		if (field.isAnnotationPresent(Transient.class)) {
			return true;
		}
		if(transArr != null) {
			for (int i = 0; i < transArr.length; i++) {
				if(field.getName().equals(transArr[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 方法用途: 获取实体类的主键值<br>
	 * 操作步骤: 注意-不建议在运行时使用，由于使用了反射，性能低下<br>
	 * @param entity 业务实体
	 * @return 返回实体类的主键值
	 */
	@Deprecated
	public Serializable getId(T entity) {
		return (Serializable) ReflectionUtil.invokeGetterMethod(entity, getIdName());
	}
	
	/**
	 * 方法用途: 获取实体类的主键名<br>
	 * 操作步骤: TODO<br>
	 * @return 
	 */
	public String getIdName() {
		return currentColumnFieldNames.get(pkName);
	}
	
	//--------------------------------保存操作-----------------------------------------------------------
	
	private Integer preSave(String sql,List<T> tList,boolean isAllField) {
		Integer key = SQLExecute.executeInsert(connectionManager,sql, new ISqlDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement, Object... params) throws SQLException {
				List<String> cList = null;
				if(isAutoPk) {
					cList = sqlBuilder.getOtherIdColumns();
				} else {
					cList = sqlBuilder.getColumns();
				}
				for (int j = 0; j < tList.size(); j++) {
					T t = tList.get(j);
					int index =1;
					for (int i = 1; i <= cList.size(); i++) {

						String columnName = cList.get(i - 1);
						Object value = selector.invoke(t, columnName);
						if (isAllField || value != null) {
							prepareStatement.setObject(index + cList.size() * j, value);
//							prepareStatement.setDate(i,new Date(System.nanoTime()));//这里通用处理，无需单独处理日期
							LOGGER.info("[参数索引:" + i + ",值:" + value + "]");
							index++;
						}
					}
				}
				return null;
			}
		});
		return key;
	}
	@Override
	public  boolean save(T t){
		return save(t,true);
	}
	
	@Override
	public boolean save(T t,boolean isAllField) {
//		buildInfo.buildId(t);//TODO 慎重考虑createId，等字段的值的自动设置
		String sql = sqlBuilder.preCreate(isAutoPk,t,currentColumnFieldNames,isAllField);
		List<T> tList = new ArrayList<>();
		tList.add(t);
		Integer key = preSave(sql,tList,isAllField);
		if(key == null || key<1) {
			return false;
		}
		t.setFid(key);
		return true;
	}

	@Override
	public boolean saveOrUpdate(T t) {
//		buildInfo.buildId(t);//TODO 慎重考虑createId，等字段的值的自动设置,并考虑更新和保存的设置差异
		if(t.getFid()!=null) {
			//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
			Integer res = update(t);
			if(res >0) {
				return true;
			} else {
				return true;
			}
		} else {
			return save(t);
		}
	}
	@Override
	public boolean saveOrUpdate(T t,Boolean isAllField) {
//		buildInfo.buildId(t);//TODO 慎重考虑createId，等字段的值的自动设置,并考虑更新和保存的设置差异
		if(t.getFid()!=null) {
			//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
			Integer res = update(t,isAllField);
			if(res >0) {
				return true;
			} else {
				return true;
			}
		} else {
			return save(t,isAllField);
		}
	}
	
	@Override
	public void save(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			temp.add(t);
			if (i > 0 && i % BatchOperator.FLUSH_CRITICAL_VAL.getSize() == 0) {
				int maxkey = preSave(sqlBuilder.createOfBatch(temp.size(),isAutoPk, isMycat),temp,true);
				for (T t2 : temp) {
					t2.setFid(maxkey++);//此处需要严格并发测试TODO
				}
				flushStatements();
				temp = new ArrayList<T>();
			}
		}
		if(temp.size()>0) {
			int maxkey = preSave(sqlBuilder.createOfBatch(temp.size(),isAutoPk, isMycat),temp,true);
			for (T t2 : temp) {
				t2.setFid(maxkey++);//此处需要严格并发测试TODO
			}
		}
	}
	
	
	//--------------------------------更新操作-----------------------------------------------------------
	
	
	@Override
	public Integer update(T t) {
		return update(t,true);
	}
	
	/**
	 * 方法用途: 更新<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @param whereColumn	以这个字段为条件来更新数据
	 * @return
	 */
	public Integer update(T t,String whereColumn) {
		return update(t,true,whereColumn);
	}
	
	/**
	 * 
	 * 方法用途: 更新<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @param isAllField 是否全字段更新
	 * @return
	 */
	public Integer update(T t,Boolean isAllField) {
		return update(t,isAllField,"fid");
	}
	
	/**
	 * 
	 * 方法用途: 更新<br>
	 * 操作步骤: TODO<br>
	 * @param t
	 * @param isAllField 是否全字段更新
	 * @param whereColumn	以这个字段为条件来更新数据
	 * @return
	 */
	public Integer update(T t,Boolean isAllField,String whereColumn) {
//		buildInfo.buildId(t);
		
		//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
		if(t == null) {
			return null;
		}
		String sql = sqlBuilder.update(t, currentColumnFieldNames,whereColumn,isAllField);
		logger.info(sql);
		return SQLExecute.executeUpdate(connectionManager,sql,new ISqlDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				List<String> cList = sqlBuilder.getOtherIdColumns();
				int count = 0;
				for (int i=0; i < cList.size(); i++) {
					String columnName = cList.get(i);
					Object value = selector.invoke(t, columnName);
					if(((isAllField == null || isAllField)||value != null)&&columnName!=whereColumn) {
						prepareStatement.setObject(count+1,value);
						LOGGER.info("[值参数索引:" + (count+1) + ",值:" + value + "]");
						count++;
					}
				}
				Object value = selector.invoke(t, whereColumn);
				prepareStatement.setObject(count+1,value);
				LOGGER.info("[条件参数索引:" + (count+1) + ",值:" + value + "]");
				return null;
			}
			
		});

	}

	@Override
	public void update(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		for (int i=0; i <  list.size(); i++) {
			update(list.get(i));
			if (i > 0 && i % BatchOperator.FLUSH_CRITICAL_VAL.getSize() == 0) {
				flushStatements();
			}
		}
	}
	//--------------------------------删除操作-----------------------------------------------------------
	
	
	@Override
	public Integer remove(PK id) {
		return SQLExecute.executeUpdate(connectionManager,sqlBuilder.removeById(),id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer remove(T entity) {
		return remove((PK) entity.getFid());
	}
	
	/* 
	 * value为中文，会无法正常删除TODO
	 */
	@Override
	public Integer remove(String name, Object value) {
		Integer count = SQLExecute.executeUpdate(connectionManager,sqlBuilder.remove(name),value);
		return count;
	}
	
	@Override
	public Integer remove(PK[] ids) {
		if(null == ids || ids.length<1) {
			return -1;
		} 
		if(ids.length == 1) {
			return remove(ids[0]);
		}
		
		return remove(Arrays.asList(ids));
	}
	
	@Override
	public Integer remove(List<PK> ids) {
		if (null == ids || ids.isEmpty()) {
			return -1;
		}
		int resultcount = 0;
		List<PK> temp = new ArrayList<PK>();
		for (int  i = 0; i < ids.size(); i++) {
			temp.add(ids.get(i));
			if (i > 0 && i % BatchOperator.FLUSH_CRITICAL_VAL.getSize() == 0) {
				resultcount += SQLExecute.executeUpdate(connectionManager,sqlBuilder.removeOfBatch(temp.size()), temp.toArray());
				flushStatements();
				temp = new ArrayList<PK>();
			}
		}
		if(temp.size()>0) {
			resultcount += SQLExecute.executeUpdate(connectionManager,sqlBuilder.removeOfBatch(temp.size()), temp.toArray());
		}
		return resultcount;
	}
	
	
//--------------------------------查询操作-----------------------------------------------------------
	
	public T setResult(String columnLabel, Object val, T t) {
		String key = currentColumnFieldNames.get(columnLabel);
		if(key == null) {
			return t;
		}
		try {
			if(val != null) {
				selector.invokeSet(t, key, val,false);
			}
		} catch(IllegalArgumentException ex) {
			throw new IllegalArgumentException("请检查是否数据库类型和实体类型不匹配，或是字段名和属性名不匹配==>>"+ex.getMessage());
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return t;
	}
	
	public List<T> find(String sql,Object... params) {
		logger.info(sql);
		List<T> tList = SQLExecute.executeQuery(connectionManager,sql, new ISqlDataCallback<T>() {
			@Override
			public T paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return ISqlDataCallback.super.paramCall(prepareStatement,params);
			}
			@Override
			public T resultCall(String columnLabel, Object val,T t) {
				return setResult(columnLabel, val, t);
			}
			
		},this.entityClass);
		return tList;
	}

	public T findOne(String sql,Object params) {
		return findOne(sql,true,params);
	}

	public T findOne(String sql,Object... params) {
		return findOne(sql,true,params);
	}

	/**
	 *
	 * 方法用途: 查询唯一记录结果集<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @param isMutilLineExcpetion 是否抛出异常的形式：如果数据集中有多行记录
	 * @param params
	 * @return
	 */
	public T findOne(String sql,boolean isMutilLineExcpetion,Object params) {
		return findOne(sql,isMutilLineExcpetion,new Object[] {params});
	}
	
	/**
	 * 
	 * 方法用途: 查询唯一记录结果集<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @param isMutilLineExcpetion 是否抛出异常的形式：如果数据集中有多行记录
	 * @param params
	 * @return
	 */
	public T findOne(String sql,boolean isMutilLineExcpetion,Object[] params) {
		List<T> list = find(sql,params);
		if(list != null && list.size()==1) {
			return list.get(0);
		} else if(list != null && list.size()>1) {
			if(isMutilLineExcpetion) {
				throw new BaseDaoException("返回的数据集有多条记录["+list.size()+"条]，确认是否出现脏数据");
			}
			logger.warn("返回的数据集有多条记录["+list.size()+"条]，确认是否出现脏数据");
			return list.get(0);
		} else {
			return null;
		}
	}
	
	@Override
	public T findUnique(String name, boolean isMutilLineExcpetion, Object value) {
		String sql = sqlBuilder.findByProperties(name, "?");
		return findOne(sql,isMutilLineExcpetion, value);
	}

	@Override
	public T findUnique(String name, Object value) {
		String sql = sqlBuilder.findByProperties(name, "?");
		return findOne(sql, value);
	}
	
	/*
	 *考虑是否提供这个功能 TODO 
	 */
	@Override
	public T findUnique(Query<T> criteria) {
//		return findUnique(name,value);
		return criteria.uniqueResult();
	}

	@Override
	public T findById(PK id) {
		return findOne(sqlBuilder.findById(), id);
	}
	
	@Override
	public List<T> findByIds(PK[] idArr) {
		return find(sqlBuilder.findByIds(idArr.length),(Object[])idArr);
	}
	@Override
	public List<T> findByIds(String ids) {
		@SuppressWarnings("unchecked")
		PK[] idArr = (PK[]) ids.split(",");
		return find(sqlBuilder.findByIds(idArr.length),(Object[])idArr);
	}
	@Override
	public List<T> findByMutil(String name, String values) {
		String[] valueArr = values.split(",");
		return find(sqlBuilder.findByMutil(name,valueArr.length),(Object[])valueArr);
	}

	@Override
	public List<T> findBy(String name,Object value) {
		return find(sqlBuilder.findBy(name + " = ?"),value);
	}
	
	@Override
	public List<T> findAll() {
		return find(sqlBuilder.findAll());
	}
	
	@Override
	public List<T> findBy(T param){
		return findBy(param, null, true);
	}
	
	/**
	 * @param param where条件参数
	 */
	@Override
	public List<T> findBy(T param, String sortName, boolean isDesc) {
		SqlDTO dto = sqlBuilder.whereValue(param, currentColumnFieldNames);
		String endSearchSql = CommonSqlBuilder.buildEndSearchSql(null, null, sortName, isDesc).toString();
		return find(sqlBuilder.findBy(dto.getWhereName())+endSearchSql,dto.getWhereValues());
	}
	
	/**
	 * 方法用途: where条件设置<br>
	 * 操作步骤: TODO暂未实现<br>
	 * @param name
	 * @param value
	 * @return
	 */
	public Dao<T,PK> where(String name, Object value) {
//		createQuery(sql, params);
		return this;
	}
	
	
	//--------------------------------查询分页操作-----------------------------------------------------------
	
	/**
	 * 
	 * 方法用途: TODO 会频繁创建query对象，目前待选，暂不使用，需要测试query对内存的消耗，再考虑是否启用<br>
	 * 操作步骤: TODO<br>
	 * @param currentRecord
	 * @param pageSize
	 * @param sortName
	 * @param isDesc
	 * @param params
	 * @return
	 */
	public List<? extends IdEntity<Serializable,Integer>> query(int currentRecord,int pageSize,String sortName, boolean isDesc,T params) {
		SqlDTO dto = sqlBuilder.whereValue(params, currentColumnFieldNames);
		String sql = sqlBuilder.findBy(dto.getWhereName());
		Query<T> query = createQuery(sql, dto.getWhereValues());
		query.add(WhereDTO.eq("1", "1"));//
		
		List<? extends IdEntity<Serializable,Integer>> list = query.setSortName(sortName).setSortMethod(isDesc).setCurrentRecord(currentRecord).setPageSize(pageSize).list();
		return list;
		
	}
	
	private Query<T> createQuery(String sql, Object... params) {
		@SuppressWarnings("unchecked")
		Dao<T, Serializable> dao =  (Dao<T, Serializable>) this;
		Query<T> query = new Query<>(dao,sql,params);
		return query;
	}
	
	/**
	 * 
	 * 方法用途: 分页基础方法<br>
	 * 操作步骤: TODO<br>
	 * @param currentRecord
	 * @param pageSize
	 * @param sortName
	 * @param isDesc
	 * @param params
	 * @return
	 */
	public List<T> find(Integer currentRecord,Integer pageSize,String sortName, Boolean isDesc,T params) {
		if(params == null) {//FIXED lcy 2016/5/27 风险提醒：这个分支会导致拖死数据库，但是又有这样的业务场景(考虑说服用户-企业用户)-如果是针对个人的系统，不建议使用,需要考虑更好的方案,如果有提供分页，可以考虑放开  TODO 整合块3
			return find(currentRecord,pageSize,sortName,isDesc,sqlBuilder.findAll());
		}
		SqlDTO dto = sqlBuilder.whereValue(params, currentColumnFieldNames);
		String sql = sqlBuilder.findBy(dto.getWhereName());
		List<T> list = find(currentRecord,pageSize,sortName,isDesc,sql,dto.getWhereValues());
		return list;
	}
	
	public List<T> find(Integer currentRecord,Integer pageSize,String sortName, Boolean isDesc,String sql,Object... params) {
		
		StringBuilder type = CommonSqlBuilder.buildEndSearchSql(currentRecord, pageSize, sortName, isDesc);
		
		List<T> list = find(sql +type,params);
		return list;
	}

	/**
	 * 
	 * 方法用途: 分页-不知排序<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page<T> findPage(int currentPage,int pageSize,boolean isReturnLastPage,T params) {
		Page<T> page = findPage(currentPage,pageSize,null,null, isReturnLastPage,params);
		return page;
	}
	
	/**
	 * 
	 * 方法用途: 分页-不知排序<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage
	 * @param pageSize
	 * @param params
	 * @return
	 */
	public Page<T> findPage(int currentPage,int pageSize,T params) {
		return findPage(currentPage, pageSize, true, params);
	}
	
	/**
	 * 方法用途: 分页查询-支持排序<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage 当前页码
	 * @param pageSize 每页数据个数
	 * @param sortName 排序字段名
	 * @param isDesc 是否降序 [排序方式（升序(asc)或降序(desc)]
	 * @param params 查询参数
	 * @param isReturnLastPage 是否返回最后一页
	 * @return 查询结果分页数据
	 */
	public Page<T> findPage(Integer currentPage,Integer pageSize,String sortName, Boolean isDesc,boolean isReturnLastPage,T params) {
		if(pageSize == null || pageSize == 0) {//bug修复，避免忘传pageSize导致的错误
			pageSize = 20;
		}
		Page<T> page = new Page<T>(currentPage,pageSize,count(params), isReturnLastPage);
		List<T> list = find(page.getCurrentRecord(),pageSize,sortName,isDesc,params);
		page.setResults(list);
		return page;
	}
	
	/**
	 * 方法用途: 分页查询-支持排序<br>
	 * 操作步骤: TODO<br>
	 * @param currentPage 当前页码
	 * @param pageSize 每页数据个数
	 * @param sortName 排序字段名
	 * @param isDesc 是否降序 [排序方式（升序(asc)或降序(desc)]
	 * @param params 查询参数
	 * @return 查询结果分页数据
	 */
	public Page<T> findPage(Integer currentPage,Integer pageSize,String sortName, Boolean isDesc,T params) {
		return findPage(currentPage, pageSize, sortName, isDesc, true, params);
	}
	
	/**
	 * 
	 * 方法用途: 基于这个方法，再次封装，提供更简便的多表分页查询 <br>
	 * 操作步骤: sql通过 druid sqlparser 来解析 
	 * 注意：方法不是很灵活<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sortName
	 * @param isDesc
	 * @param sql
	 * @param params
	 * @return
	 */
	@Deprecated
	public Page<T> findPage(Integer currentPage,Integer pageSize,String sortName, Boolean isDesc,String sql,Object... params) {
		Page<T> page = new Page<T>(currentPage,pageSize,CountDao.count(connectionManager,sql.replace("select * from", "select count(1) from"), params), true);
		List<T> list = find(page.getCurrentRecord(),pageSize,sortName,isDesc,sql,params);
		page.setResults(list);
		return page;
	}
	
	/**
	 * 
	 * 方法用途: 基于这个方法，再次封装，提供更简便的多表分页查询<br>
	 * 操作步骤: sql通过 druid sqlparser 来解析<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sql
	 * @param isReturnLastPage 是否返回第一页
	 * @param params
	 * @return 分页数据实体
	 */
	public Page<T> findPage(Integer currentPage,Integer pageSize,String sql,boolean isReturnLastPage,Object... params) {
		Integer count = CountDao.buildCountSql(connectionManager,sql, params);
		Page<T> page = new Page<>(currentPage,pageSize,count,isReturnLastPage);
		List<T> list = find(page.getCurrentRecord(),pageSize,null,null,sql,params);
		page.setResults(list);
		return page;
	}
	
	/**
	 * 
	 * 方法用途: 基于这个方法，再次封装，提供更简便的多表分页查询<br>
	 * 操作步骤: sql通过 druid sqlparser 来解析<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sql
	 * @param params
	 * @return 分页数据实体
	 */
	public Page<T> findPage(Integer currentPage,Integer pageSize,String sql,Object... params) {
		return findPage(currentPage, pageSize, sql, true, params);
	}
	
	/**
	 * 
	 * 方法用途: 基于这个方法，再次封装，提供更简便的多表分页查询，sql中带有group by的话，调用此方法 <br>
	 * 操作步骤: sql通过 druid sqlparser 来解析<br>
	 * @param currentPage
	 * @param pageSize
	 * @param sql
	 * @param params
	 * @return 分页数据实体
	 * @author yhb 2016/4/6
	 */
	public Page<T> findPageForGroup(Integer currentPage,Integer pageSize,String sql,Object... params) {
		String countSql = sql.substring(sql.indexOf("from"));
		countSql = "select count(1) from (" + sql + ") t";
		Page<T> page = new Page<T>(currentPage,pageSize,CountDao.count(connectionManager,countSql,params),true);
		List<T> list = find(page.getCurrentRecord(),pageSize,null,null,sql,params);
		
		page.setResults(list);
		return page;
	}
	
	
	//--------------------------------统计记录数操作-----------------------------------------------------------
	
	@Override
	public Integer count(T param) {
		if(param == null) {//FIXED lcy 2016/5/27 风险提醒：这个分支会导致拖死数据库，但是又有这样的业务场景(考虑说服用户-企业用户)-如果是针对个人的系统，不建议使用,需要考虑更好的方案  TODO 整合块4
			return CountDao.count(connectionManager,sqlBuilder.count());
		}
		SqlDTO dto = sqlBuilder.whereValue(param, currentColumnFieldNames);
		return CountDao.count(connectionManager,sqlBuilder.count(dto.getWhereName()),dto.getWhereValues());
	}
	
	/**
	 * 
	 * 方法用途: 设置表的索引值，指定操作的具体分表<br>
	 * 操作步骤: TODO<br>
	 * @param index
	 * @return
	 */
	public Dao<T,PK> setIndex(Integer index) {
		if(index != null) {
			//检测下分表是否存在，不存在的话，创建一个分表 待实现 TODO
			sqlBuilder.setTableIndexLocal(index);
		} else {
			sqlBuilder.getTableIndexLocal().remove();
		}
		return this;
	}
	//------------------------------------DDL语句的实现--------------------------------------
	public int createTable(Class<T> t) {
		String sql = sqlBuilder.createTable();
		return SQLExecute.executeUpdate(connectionManager,"create table if not exists "+t.getAnnotation(Table.class).name()+" ("+sql+") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
	}
	//--------------------------------未处理和实现的功能-----------------------------------------------------------
	
	@Override
	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}
	
//	@Inject
//	private BuildInfo<T> buildInfo;

}
