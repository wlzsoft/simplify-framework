package com.meizu.simplify.dao.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.BatchOperator;
import com.meizu.simplify.dao.Criteria;
import com.meizu.simplify.dao.Restrictions;
import com.meizu.simplify.dao.RowBounds;
import com.meizu.simplify.dao.annotations.Column;
import com.meizu.simplify.dao.annotations.Key;
import com.meizu.simplify.dao.annotations.Table;
import com.meizu.simplify.dao.annotations.Transient;
import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.dao.dto.BaseDTO;
import com.meizu.simplify.dao.dto.BaseDTO.LinkType;
import com.meizu.simplify.dao.dto.SqlDTO;
import com.meizu.simplify.dao.dto.WhereDTO;
import com.meizu.simplify.dao.exception.BaseDaoException;
import com.meizu.simplify.dao.util.Page;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;
//import com.meizu.util.BeanUtils;
/**
 * 
 * <p><b>Title:</b><i> 基础泛型DAO实现类</i></p>
 * <p>Desc:1. IBaseDaoTemplate的实现方式
 *         2. 注意sql注入的问题
 *         3.考虑jdbc4.0规范，或是直接使用特定mysql二进制协议，不考虑跨数据库</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午1:42:18</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午1:42:18</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T> 业务实体类型 
 * @param <PK>  PK类型 ，如：String、Long、Integer 等
 */
@Bean(type=BeanTypeEnum.PROTOTYPE)
public class Dao<T extends IdEntity<Serializable,Integer>, PK extends Serializable>   implements IDao<T, PK> {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Class<T> entityClass;
	
	//主键列名
	private String pkName;

	//columnName=FieldName
	private Map<String, String> currentColumnFieldNames = new LinkedHashMap<String, String>();

	private SQLBuilder<T> sqlBuilder;

	
	/**
	 * @param clazz  业务实体类
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
			throw new RuntimeException("类" + entityClass
					+ "需要指定@Table注解!");
		}
		
		List<String> columnArr = new ArrayList<>(currentColumnFieldNames.keySet());
		List<String> otherIdColumn = new ArrayList<>();
		for (String columnName : columnArr) {
			if(!columnName.equals(pkName)) {
				otherIdColumn.add(columnName);
			}
		}
		
		sqlBuilder = new SQLBuilder<T>(otherIdColumn,columnArr,
				table.name(), pkName);
	}
	
	/**
	 * 方法用途: 无论有多少超类，能递归判断和提取<br>
	 * 操作步骤: TODO<br>
	 * @param class1
	 * @param trans 
	 */
	private void buildFieldInfo(Class<? super T> class1) {
		Field[] fields = class1.getDeclaredFields();
		Transient trans = entityClass.getAnnotation(Transient.class);
		getFieldInfo(fields,trans);
		if(class1.getSuperclass() != Object.class && class1.getSuperclass() != null) {
			buildFieldInfo(class1.getSuperclass());
		}
	}
	
	/**
	 * 
	 * 方法用途: 抽取字段和属性信息<br>
	 * 操作步骤: TODO<br>
	 * @param fields
	 * @param trans
	 */
	private void getFieldInfo(Field[] fields, Transient trans) {
		String[] transArr = null;
		if(trans != null) {
			transArr = trans.value();
		}
		for (Field field : fields) {
			boolean isTransient = validTransient(transArr,field);
			if(isTransient) {
				continue;
			}
			
			String columnName = null;
			Column tableColumn = field.getAnnotation(Column.class);
//			如果没标示Column注解，那么默认使用全大写属性名，否知使用注解指定的值
			if (null != tableColumn&&StringUtil.isNotBlank(tableColumn.value())) {
				columnName = tableColumn.value();
			} else {
				columnName = field.getName().toUpperCase();
			}
			currentColumnFieldNames.put(columnName, field.getName());
			if (field.isAnnotationPresent(Key.class)) {
				// 取得ID的列名
				pkName = columnName;
				Key primaryKey = field.getAnnotation(Key.class);
				if(primaryKey != null&&primaryKey.auto()) {
					
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
	 * 方法用途: 获取实体类的主键值。<br>
	 * 操作步骤: TODO<br>
	 * @param entity 业务实体
	 * @return 返回实体类的主键值。
	 */
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
	
	/**
	 * 
	 * 方法用途: 执行delete，update 根据param的where条件来更新或是删除<br>
	 * 操作步骤: TODO<br>
	 * @param param
	 * @return
	 */
	public Integer executeUpdate(String sql,Object... params) {
		if(params == null) {
			return null;
		}
		return executeUpdate(sql,new IDataCallback<Integer>() {

			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}
			
		});
	}
	
	/**
	 * 方法用途: 可执行insert和delete，update语句,支持预处理<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @param callback
	 * @return
	 */
	public Integer executeUpdate(String sql,IDataCallback<Integer> callback) {
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			callback.paramCall(prepareStatement);
			Integer rs = prepareStatement.executeUpdate();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseDaoException("执行sql异常", e);
		}
	}
	
	/**
	 * 未测试
	 * 方法用途: 可执行insert和delete，update语句,不支持预处理<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @return
	 */
	public Integer executeUpdate(String sql) {
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			Integer rs = prepareStatement.executeUpdate();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//--------------------------------保存操作-----------------------------------------------------------
	
	/**
	 * 
	 * 方法用途: 可执行insert,支持预处理<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @param callback
	 * @return
	 */
	private Integer executeInsert(String sql,IDataCallback<Integer> callback) {
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			callback.paramCall(prepareStatement);
			prepareStatement.executeUpdate();
			ResultSet rs = prepareStatement.getGeneratedKeys();
			if(rs.next()) {
				int key=rs.getInt(1);
				return key;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private Integer preSave(String sql,List<T> tList) {
		Integer key = executeInsert(sql,new IDataCallback<Integer>(){
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... params) throws SQLException {
				for(int j=0; j<tList.size();j++) {
					T t = tList.get(j);
					List<Object> values = sqlBuilder.obtainFieldValues(t, currentColumnFieldNames);
					for (int i=1; i <= values.size();i++) {
						Object obj = values.get(i-1);
						prepareStatement.setObject(i+values.size()*j, obj);
					}
				}
				return null;
			}});
		return key;
	}
	
	@Override
	public boolean save(T t) {
//		buildInfo.buildId(t);//TODO 慎重考虑createId，等字段的值的自动设置
		String sql = sqlBuilder.preCreate();
		List<T> tList = new ArrayList<>();
		tList.add(t);
		Integer key = preSave(sql,tList);
		if(key<1) {
			return false;
		}
		t.setId(key);
		return true;
	}

	@Override
	public boolean saveOrUpdate(T t) {
//		buildInfo.buildId(t);//TODO 慎重考虑createId，等字段的值的自动设置,并考虑更新和保存的设置差异
		if(t.getId()!=null) {
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
	public void save(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			temp.add(t);
			if (i > 0 && i % BatchOperator.FLUSH_CRITICAL_VAL.getSize() == 0) {
				int maxkey = preSave(sqlBuilder.createOfBatch(temp.size(),currentColumnFieldNames),temp);
				for (T t2 : temp) {
					t2.setId(maxkey++);//此处需要严格并发测试TODO
				}
				flushStatements();
				temp = new ArrayList<T>();
			}
		}
		if(temp.size()>0) {
			int maxkey = preSave(sqlBuilder.createOfBatch(temp.size(), currentColumnFieldNames),temp);
			for (T t2 : temp) {
				t2.setId(maxkey++);//此处需要严格并发测试TODO
			}
		}
	}
	
	
	//--------------------------------更新操作-----------------------------------------------------------
	
	
	@Override
	public Integer update(T t) {
//		buildInfo.buildId(t);
		
		//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
		if(t == null) {
			return null;
		}
		String sql = sqlBuilder.update(t, currentColumnFieldNames);
		return executeUpdate(sql,new IDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				List<String> cList = sqlBuilder.getOtherIdColumns();
				for (int i=0; i < cList.size(); i++) {
					String columnName = cList.get(i);
					prepareStatement.setObject(i,ReflectionUtil.invokeGetterMethod(t, columnName));
				}
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
		return executeUpdate(sqlBuilder.removeById(),id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer remove(T entity) {
		return remove((PK) entity.getId());
	}
	
	/* 
	 * value为中文，会无法正常删除TODO
	 */
	@Override
	public Integer remove(String name, Object value) {
		Integer count = executeUpdate(sqlBuilder.remove(name),value);
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
				resultcount += executeUpdate(sqlBuilder.removeOfBatch(temp.size()), temp.toArray());
				flushStatements();
				temp = new ArrayList<PK>();
			}
		}
		if(temp.size()>0) {
			resultcount += executeUpdate(sqlBuilder.removeOfBatch(temp.size()), temp.toArray());
		}
		return resultcount;
	}
	
	
//--------------------------------查询操作-----------------------------------------------------------
	
	
	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: <pre>
	System.out.println("请求sql的列名ColumnLabel:"+metaData.getColumnLabel(i));
	System.out.println("java中列的类型ColumnClassName:"+metaData.getColumnClassName(i));
	System.out.println("数据库中的列名ColumnName:"+metaData.getColumnName(i));
	System.out.println("数据库中列的类型ColumnType:"+metaData.getColumnType(i));
	System.out.println("数据库中列的类型的名字ColumnTypeName:"+metaData.getColumnTypeName(i));
	System.out.println("整个数值长度ColumnDisplaySize:"+metaData.getColumnDisplaySize(i));
	System.out.println("整数长度Precision:"+metaData.getPrecision(i));
	
	System.out.println("表名TableName:"+metaData.getTableName(i));
	System.out.println("数据库名CatalogName:"+metaData.getCatalogName(i));
	
	System.out.println("小数长度Scale:"+metaData.getScale(i));
	
	System.out.println("列的模式SchemaName:"+metaData.getSchemaName(i)+"==》end");
	System.out.println("========================");</pre><br>
	 * @param sql
	 * @param callback
	 * @return
	 */
	public <B> List<B> executeQuery(String sql,IDataCallback<B> callback) {
		List<B> bList= new ArrayList<B>();
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			callback.paramCall(prepareStatement);
			ResultSet rs = prepareStatement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while(rs.next()) {
				B b = callback.resultCall(rs,this.entityClass);
				for(int i=1; i <= metaData.getColumnCount(); i++) {
					String columnLabel = metaData.getColumnLabel(i);
					callback.resultCall(columnLabel,rs.getObject(columnLabel),b);
				}
				bList.add(b);
			}
//			查询无需事务处理
//			DruidPoolFactory.startTransaction();
//			DruidPoolFactory.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			DruidPoolFactory.rollback();
		} finally {
			DruidPoolFactory.close();//TODO 测试是否需要关闭，关闭是否回收到连接池，还是真正的关闭
		}
		return bList;
	}
	
	/**
	 * 
	 * 方法用途: 匹配数据库字段类型和实体属性类型，并转换成实体类型<br>
	 * 操作步骤: TODO<br>
	 * @param val
	 * @return
	 */
	private Class<?> mapperOrmType(Object val) {
		Class<?> valClazz = val.getClass();
		if(valClazz == Timestamp.class) {
			valClazz = Date.class;
		}
		return valClazz;
	}
	
	public List<T> find(String sql,Object... params) {
		logger.info(sql);
		List<T> tList = executeQuery(sql, new IDataCallback<T>() {
			@Override
			public T paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}
			@Override
			public T resultCall(String columnLabel, Object val,T t) {
				String key = currentColumnFieldNames.get(columnLabel);
				try {
					Class<?> valClazz = mapperOrmType(val);
					ReflectionUtil.invokeSetterMethod(t, key, val,valClazz);
				} catch(IllegalArgumentException ex) {
					throw new IllegalArgumentException("请检查是否数据库类型和实体类型不匹配，或是字段名和属性名不匹配==>>"+ex.getMessage());
				}
				return t;
			}
		});
		return tList;
	}
	
	
	public T findOne(String sql,Object... params) {
		return find(sql,params).get(0);
	}
	
	@Override
	public T findUnique(String name, Object value) {
		return findOne(sqlBuilder.findByProperties(name, "?"),value);
	}
	
	/*
	 *考虑是否提供这个功能 TODO 
	 */
	@Override
	public T findUnique(Criteria criteria) {
//		return findUnique(name,value);
		return criteria.uniqueResult();
	}

	@Override
	public T findById(PK id) {
		return findOne(sqlBuilder.findById(),id);
	}
	
	@Override
	public List<T> findByIds(PK[] idArr) {
		return find(sqlBuilder.findByIds(idArr.length),idArr);
	}
	@Override
	public List<T> findByMutil(String name, String values) {
		String[] valueArr = values.split(",");
		return find(sqlBuilder.findByMutil(name,valueArr.length),valueArr);
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
	 * 
	 * 方法用途: 从给定字符串中将指定的非法字符串数组中各字符串过滤掉。<br>
	 * 操作步骤: TODO<br>
	 * @param str 待过滤的字符串
	 * @param filterChars 指定的非法字符串数组
	 * @return 过滤后的字符串
	 */
	private String filterIllegalChars(String str, String[] filterChars) {
		String rs = str;
		if (rs != null && filterChars != null) {
			for (String fc : filterChars) {
				if (fc != null && fc.length() > 0) {
					str = str.replaceAll(fc, ""); 
				}
			}
		}
		return rs;
	}
	/**
	 * @param param where条件参数
	 */
	@Override
	public List<T> findBy(T param, String sort, boolean isDesc) {
		/** 
		 * 不能用于SQL中的非法字符（主要用于排序字段名） 
		 */
		final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
		// 排序条件
		if (sort != null) {
			// 排序字段不为空，过滤其中可能存在的非法字符
			sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
		}
		SqlDTO dto = sqlBuilder.whereValue(param, currentColumnFieldNames);
		return find(sqlBuilder.findBy(dto.getWhereName()),dto.getWhereValues());
	}
	
	@Override
	public List<T> findBy(String name, Object value, String sort,boolean isDesc) {
		Criteria criteria = new Criteria();
		if (value == null) {
			criteria.add(Restrictions.isNull(name));
		} else {
			criteria.add(Restrictions.eq(name, value));
		}
		return criteria.list();
	}
	
	//--------------------------------查询分页操作-----------------------------------------------------------
	
	@Override
	public Page<T> findPage(Page<T> page,Object... params) {

		//方案一：start 推荐使用方式方案 
		/*Query query = createQuery("sql", params);
		List<T> list = query.setFirstResult((page.getNumber() - 1) * page.getPageSize())
				.setMaxResults(page.getPageSize())
				.getResultList();*/
		
//		方案二：start 考虑是否可以喝方案一共存
//		page.setTotalRecord(count(params));
		WhereDTO[] listParam = new WhereDTO[]{};
		if(params != null) {
			listParam = (WhereDTO[]) params;
		}
		BaseDTO dto = null;//sqlBuilder.findPage(listParam.toArray(new WhereDTO[listParam.size()]));
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
//		SelectDTO selectDto = new SelectDTO(dto,page);
		
		List<T> list = find(dto.getSql());
		page.setResults(list);
		return page;
	}
	
	@Override
	public Page<T> findPage(T param, int pageNo,int pageSize,String sort, boolean isDesc) {
		/** 
		 * 不能用于SQL中的非法字符（主要用于排序字段名） 
		 */
		final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
	    // 获取满足条件的记录总数，没有记录时返回空页数据
		int count = count(param);
		if (count < 1) {
			return Page.emptyPage();
		}
		
		// 排序条件
		if (sort != null) {
			// 排序字段不为空，过滤其中可能存在的非法字符
			sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
		}
		// 分页条件
		int start = Page.getStartOfPage(pageNo, pageSize) - 1;
		
		RowBounds rowBound = new RowBounds(start, pageSize);
		List<T> lst = null;//selectList(paramMap, rowBound);
		return new Page<T>(pageNo, pageSize, lst, count);
	}
	//--------------------------------统计记录数操作-----------------------------------------------------------
	
	public Integer count(String sql,Object... params) {
		List<Integer> list = executeQuery(sql, new IDataCallback<Integer>() {
			@Override
			public Integer paramCall(PreparedStatement prepareStatement,Object... obj) throws SQLException {
				return IDataCallback.super.paramCall(prepareStatement,params);
			}

			@Override
			public Integer resultCall(String columnLabel, Object object,Integer t) {
				return DataUtil.parseInt(object);
			}
		});
		return list.get(0);
	}
	
	@Override
	public Integer count(T param) {
		SqlDTO dto = sqlBuilder.whereValue(param, currentColumnFieldNames);
		return count(sqlBuilder.count(dto.getWhereName()),dto.getWhereValues());
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
	
	//--------------------------------未处理和实现的功能-----------------------------------------------------------
	
	@Override
	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}
	
	
//	@Resource
//	private BuildInfo<T> buildInfo;

}
