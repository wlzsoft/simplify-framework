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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.Constant;
import com.meizu.simplify.dao.Criteria;
import com.meizu.simplify.dao.Restrictions;
import com.meizu.simplify.dao.ResultHandler;
import com.meizu.simplify.dao.RowBounds;
import com.meizu.simplify.dao.RowMapper;
import com.meizu.simplify.dao.annotations.Column;
import com.meizu.simplify.dao.annotations.Key;
import com.meizu.simplify.dao.annotations.Table;
import com.meizu.simplify.dao.annotations.Transient;
import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.dao.dto.BaseDTO;
import com.meizu.simplify.dao.dto.BaseDTO.LinkType;
import com.meizu.simplify.dao.dto.SaveDTO;
import com.meizu.simplify.dao.dto.WhereDTO;
import com.meizu.simplify.dao.util.Page;
import com.meizu.simplify.entity.IdEntity;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.ioc.enums.BeanTypeEnum;
import com.meizu.simplify.utils.ObjectUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;
//import com.meizu.exception.BaseDaoException;
//import com.meizu.util.BeanUtils;
/**
 * 
 * <p><b>Title:</b><i> 基础泛型DAO实现类</i></p>
 * <p>Desc:1. IBaseDaoTemplate的实现方式
 *         2. 注意sql注入的问题</p>
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
	
//	@Resource
//	private BuildInfo<T> buildInfo;
	
	private Class<T> entityClass;
	
	//主键列名
	private String pkName;

	private Map<String, String> currentColumnFieldNames = new LinkedHashMap<String, String>();

	private SQLBuilder<T> sqlBuilder;
	
	/**
	 * 
	 * 方法用途: 获取默认SqlMapping命名空间<br>
	 * 操作步骤: 使用泛型参数中业务实体类型的全限定名作为默认的命名空间。
	 * 如果实际应用中需要特殊的命名空间，可由子类重写该方法实现自己的命名空间规则<br>
	 * @param entityClass
	 * @return 返回命名空间字符串
	 */
	protected String getDefaultSqlNamespace(Class<?> entityClass) {
		//Class<T> clazz = ReflectGeneric.getClassGenricType(entityClass);
		String nameSpace = entityClass.getName();//注意：正式启用时，需要确保不同dao使用不用的namespace
		nameSpace = "com.meizu.data.mybatis.BaseDao";
		return nameSpace;
	}
	
	/**
	 * 
	 * 方法用途: 生成主键值<br>
	 * 操作步骤: 默认情况下什么也不做；
	 * 如果需要生成主键，需要由子类重写此方法根据需要的方式生成主键值<br>
	 * @param t 要持久化的对象
	 */
	@Deprecated
	protected void generateId(T t) {
//		buildInfo.buildId(t);
	}
	
	/**
	 * 构造方法
	 * @param clazz  业务实体类
	 */
	public Dao(Class<T> entityClass) {
		this.entityClass = entityClass;
		DaoInit(entityClass);
	}

	/**
	 * 方法用途: 无论有多少超类，能递归判断和提取<br>
	 * 操作步骤: TODO<br>
	 * @param class1
	 * @param trans 
	 */
	private void buildFieldInfo(Class<? super T> class1, Transient trans) {
		Field[] fields = class1.getDeclaredFields();
		getFieldInfo(fields,trans);
		if(class1.getSuperclass() != Object.class && class1.getSuperclass() != null) {
			buildFieldInfo(class1.getSuperclass(),trans);
		}
	}
	
	/**
	 * 
	 * 方法用途: 初始化dao信息<br>
	 * 操作步骤: TODO<br>
	 * @param entityClass
	 */
	private void DaoInit(Class<T> entityClass) {

		Transient trans = entityClass.getAnnotation(Transient.class);
		buildFieldInfo(entityClass,trans);
		
		Table table = entityClass.getAnnotation(Table.class);
		if (null == table) {
			throw new RuntimeException("类" + entityClass
					+ "需要指定@Table注解!");
		}
		sqlBuilder = new SQLBuilder<T>(currentColumnFieldNames.keySet(),
				table.name(), pkName);
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
			}
		}
	}
	
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
	 * 方法用途: map转实体<br>
	 * 操作步骤: TODO<br>
	 * @param resultMap
	 * @param tClazz
	 * @return
	 */
	private T MapToEntity(Map<String, Object> resultMap, Class<T> tClazz) {
		if(resultMap == null) {
			return null;
		}
		T t = null;
		try {
			t = tClazz.newInstance();
		} catch (InstantiationException e) {
			logger.error("封装查询结果时，实例化对象(" + this.entityClass + ")时，出现异常!"
					+ e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error("封装查询结果时，实例化对象(" + this.entityClass + ")时，出现异常!"
					+ e.getMessage());
		}
		for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
			String key = entry.getKey();
			key = currentColumnFieldNames.get(key);
			Object val = entry.getValue();
			try {
				Class<?> valClazz = mapperOrmType(val);
				ReflectionUtil.invokeSetterMethod(t, key, val,valClazz);
			} catch(IllegalArgumentException ex) {
				throw new IllegalArgumentException("请检查是否数据库类型和实体类型不匹配，或是字段名和属性名不匹配==>>"+ex.getMessage());
			}
		}
		return t;
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
	
	/**
	 * 
	 * 方法用途: map转list<br>
	 * 操作步骤: TODO<br>
	 * @param listMap
	 * @return
	 */
	public List<T> MapToList(List<Map<String, Object>> listMap) {
		List<T> list= new ArrayList<T>();
		for (Map<String, Object> map : listMap) {
			list.add(MapToEntity(map, this.entityClass));
		}
		return list;
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
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @param mapKey 数据mapKey
	 * @param rowBounds 用于分页查询的记录范围
	 * @return 查询结果Map
	 */
	protected Map<Object,Object> selectMap(
			String statement, Object parameter, String mapKey,
			RowBounds rowBounds) {
		return selectMap(
				
				parameter, mapKey, rowBounds);
	}

	/**
	 * 
	 * 方法用途: TODO<br>
	 * 操作步骤: TODO<br>
	 * @param statement 映射的语句ID
	 * @param parameter 参数
	 * @param rowBounds 用于分页查询的记录范围
	 * @param handler 结果集处理器
	 */
	protected void select(
			String statement, Object parameter, RowBounds rowBounds,
			ResultHandler handler) {
		select(parameter, rowBounds, handler);
	}
	
	/**
	 * 未测试
	 * 方法用途: 获取实体类的主键值。<br>
	 * 操作步骤: TODO<br>
	 * @param entity 业务实体
	 * @return 返回实体类的主键值。
	 */
	//@Override
	public Serializable getId(T entity) {
//		return (Serializable) BeanUtils.getField(entity, getIdName());
		return null;
	}
	
	/**
	 * 未实现
	 * 方法用途: 获取实体类的主键名。<br>
	 * 操作步骤: TODO<br>
	 * @return 返回实体类的主键名。
	 */
	private String getIdName() {
		return null;
//		ClassMetadata meta = sessionFactory.getClassMetadata(clazz);
//		return meta.getIdentifierPropertyName();
	}
	
	@Override
	public Integer create(T t) {
		SaveDTO dto = sqlBuilder.create(t, currentColumnFieldNames);
		
		Integer res = insert(dto);
		t.setId(dto.getId());
		return res;
	}
	
	@Override
	public void create(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		// 获取列表的第一个对象的pk的value
		Object pkVal = null;
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			if (i == 0) {
//				pkVal = ReflectionUtil.invokeGetterMethod(t, idName);
			}

			temp.add(t);
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
//				SaveDTO dto = sqlBuilder.create(t, currentColumnFieldNames);
				insert(sqlBuilder.createOfBatch(temp,currentColumnFieldNames, pkVal));
				flushStatements();
				temp = new ArrayList<T>();
			}
		}
		insert(sqlBuilder.createOfBatch(temp, currentColumnFieldNames, pkVal));
	}
	
	@Override
	public void createByMycat(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		// 获取列表的第一个对象的pk的value
		Object pkVal = null;
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			if (i == 0) {
//				pkVal = ReflectionUtil.invokeGetterMethod(t, idName);
			}

			temp.add(t);
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
//				SaveDTO dto = sqlBuilder.create(t, currentColumnFieldNames);
				insert(sqlBuilder.createOfBatchByMycat(temp,currentColumnFieldNames, pkVal));
				flushStatements();
				temp = new ArrayList<T>();
			}
		}
		insert(sqlBuilder.createOfBatchByMycat(temp, currentColumnFieldNames, pkVal));
	}
	
	public void createForOracle(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		List<T> temp = new ArrayList<T>();
		// 获取列表的第一个对象的pk的value
		Object pkVal = null;
		for (int i=0; i < list.size(); i++) {
			T t = list.get(i);
			if (i == 0) {
//				        BeanMapUtil.bean2Map(t); //用法类似
//				pkVal = ReflectionUtil.invokeGetterMethod(t, idName);
			}

			temp.add(t);
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
				insert(sqlBuilder.createOfBatch(temp,currentColumnFieldNames, pkVal));
				flushStatements();
				temp = new ArrayList<T>();
			}
		}
		insert(sqlBuilder.createOfBatch(temp, currentColumnFieldNames, pkVal));
	}
	
	@Override
	public Integer save(T t) {
		generateId(t);
		return create(t);
	}

	
	@Deprecated//TODO 考虑buildInfo移除后的影响
	@Override
	public Integer saveOrUpdate(T t) {
//		buildInfo.buildId(t);
		if(t.getId()!=null) {
			return updateMeta(t);
		} else {
			return create(t);
		}
	}
	
	@Override
	public void save(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
			this.create(list);
			return;
	}
	
	@Override
	public void saveByMycat(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
			this.createByMycat(list);
			return;
	}
	
	@Override
	public Integer remove(PK id) {
		return delete(sqlBuilder.removeById(id));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer remove(T entity) {
		return remove((PK) entity.getId());
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
		List<PK> temp = new ArrayList<PK>();
		for (int  i = 0; i < ids.size(); i++) {
			temp.add(ids.get(i));
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
				delete(sqlBuilder.removeOfBatch(temp));
				flushStatements();
				temp = new ArrayList<PK>();
			}
		}
		return  delete(sqlBuilder.removeOfBatch(temp));
	}
	
	@Override
	public Integer removeAll() {
		return delete(sqlBuilder.removeAll());
	}
	
	@Override
	public Integer remove(String name, Object value) {
//		Query query = createQuery("delete from " + clazz.getName() + " where "
//				+ name + "=?", value);
//		query.executeUpdate();
		
		Integer count = delete(sqlBuilder.remove(name,value));
		return count;
	}

	@Override
	public Integer update(T t) {
		generateId(t);
		return updateMeta(t);

	}

	public Integer updateMeta(T t) {
		//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
		return update(sqlBuilder.update(t, currentColumnFieldNames));
	}
	
	@Override
	public void update(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		for (int i=0; i <  list.size(); i++) {
			this.update(list.get(i));
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
				flushStatements();
			}
		}
	}

	@Override
	public T load(PK id) {
		return findById(id);
	}

	@Override
	public T get(PK id) {
		return findById(id);
	}
	
	@Override
	public void persist(T entity) {
		save(entity);
	}
	
	@Override
	public T findById(PK id) {
		Map<String, Object> resultMap = selectOne(sqlBuilder.findById(id));
		return MapToEntity(resultMap, this.entityClass);
	}
	
	@Override
	public List<T> findByIds(PK[] idArr) {
		List<Map<String, Object>> listMap = selectList(sqlBuilder.findByIds(idArr));
		List<T> list = MapToList(listMap);
		return list;
	}
	@Override
	public List<T> findByMutil(String name, String values) {
		List<Map<String, Object>> listMap = selectList( sqlBuilder.findByMutil(name,values));
		List<T> list = MapToList(listMap);
		return list;
	}

	@Override
	public List<T> findBy(T param){
		return findBy(param, null, null);
	}
	
	@Override
	public List<T> findBy(T param, String sort, String orderBy) {
		/** 
		 * 不能用于SQL中的非法字符（主要用于排序字段名） 
		 */
		final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
		final String SORT_NAME = "SORT";
		final String DIR_NAME = "DIR";
		Map<String, Object> paramMap = null;
//		try{
//			paramMap = ReflectionUtil.bean2Map(param);
//		}catch(Exception e){
//			throw new BaseDaoException("获取参数失败", e);
//		}
		// Where过滤条件
//		paramMap.put("param", param);
		// 排序条件
		if (sort != null) {
			// 排序字段不为空，过滤其中可能存在的非法字符
			sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
		}
		if (StringUtil.isEmpty(sort) || StringUtil.isEmpty(orderBy)) {
//			paramMap.put("sort", null);
//			paramMap.put("orderBy", null);
		} else {
			paramMap.put(SORT_NAME, sort);
			paramMap.put(DIR_NAME, orderBy);
		}
		List<T> lst = selectList(
				 paramMap);
		return lst;
	}
	
	@Override
	public List<T> findBy(String sqlName, Object param,int pageNo, int pageSize,String sort,String orderBy){
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("param", param);
		if (StringUtil.isEmpty(sort) || StringUtil.isEmpty(orderBy))
		{
			paramMap.put("sort", null);
			paramMap.put("orderBy", null);
		}
		int start =0;
		if (pageNo > -1)
			start = (pageNo - 1) * pageSize;
		RowBounds rowBound = new RowBounds(start,pageSize);
		List<T> lst = selectList( paramMap,rowBound);
		return lst;
	}
	
	@Override
	public List<T> findBy(String name, Object value, String orderBy,boolean isAsc) {
		Criteria criteria = new Criteria();
		if (value == null) {
			criteria.add(Restrictions.isNull(name));
		} else {
			criteria.add(Restrictions.eq(name, value));
		}
		return criteria.list();
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

	@Override
	public List<T> findBy(String name,Object value) {
		
		List<Map<String, Object>> listMap = selectList(
				 sqlBuilder.findBy(name,value));
		List<T> list = MapToList(listMap);
		return list;
	}
	
	@Override
	public List<T> findAll() {
		List<Map<String, Object>> resultMapList = selectList( sqlBuilder.findAll());
		List<T> list = new ArrayList<T>(resultMapList.size());
		for (Map<String, Object> resultMap : resultMapList) {
			if(resultMap == null) {
				continue;
			}
			T t = MapToEntity(resultMap, this.entityClass);
			list.add(t);
		}
		return list;
	}

	@Override
	public Integer findAllCount() {
		Integer count = selectOne(sqlBuilder.findAllCount());
		return count;
	}
	
	@Override
	public Integer selectOne(String findAllCount) {
		return 0;
	}
	
	@Override
	public Integer count() {
		return findAllCount();
	}
	
	
	@Override
	public Integer count(Page<T> page) {
		List<WhereDTO> listParam = new ArrayList<WhereDTO>();
		
		//其他地方有冗余，需重构start
		Object params = page.getParams().get("where");
		if(params != null) {
			listParam = (List<WhereDTO>) params;
		}
		BaseDTO dto = sqlBuilder.count(listParam.toArray(new WhereDTO[listParam.size()]));
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
		//其他地方有冗余，需重构end
		
//		java.util.HashMap<String,Object> a = selectOne(dto);
//		Integer count = DataUtil.parseInt(a.get("count(1)"));
		//bugs fix by lcy 2015/05/26
//		Integer count = selectOne(dto);
//		return count;
		return 0;
	}
	
	
	@Override
	public Integer count(T param) {
		Map<String, Object> paramMap = null;
//		try{
//			paramMap = ReflectionUtil.bean2Map(param);
//		}catch(Exception e){
//			throw new BaseDaoException("获取参数失败", e);
//		}
//		paramMap.put("param", param);
		return (Integer)selectOne(paramMap);
	}
	//-----------------------------------------------------------------------以下方法待实现
	
	
	@Override
	public T findUnique(Criteria criteria) {
//		return findUnique(name,value);
		return criteria.uniqueResult();
	}
	
	@Override
	public T findUnique(String name, Object value) {
		Map<String, Object> resultMap = selectOne(sqlBuilder.findByProperties(name, value));
		return MapToEntity(resultMap, this.entityClass);
	}
	
	@Override
	public List<T> find(Page<T> page,Object... values) {
		
		List<WhereDTO> listParam = new ArrayList<WhereDTO>();
		Object params = page.getParams().get("where");
		if(params != null) {
			listParam = (List<WhereDTO>) params;
		}
		BaseDTO dto = sqlBuilder.findPage(listParam.toArray(new WhereDTO[listParam.size()]));
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
		List<Map<String, Object>> listMap = selectList(
				 dto);
		List<T> list = MapToList(listMap);
		//end		
		
		return list;
	}
	
	@Override
	public Page<T> findPage(Page<T> page,Object... values) {

		//方案一：start 推荐使用方式方案 
//		Query query = createQuery("sql", values);
//		List<T> list = query.setFirstResult((page.getNumber() - 1) * page.getPageSize())
//				.setMaxResults(page.getPageSize())
//				.getResultList();
		//end
		
//		方案二：start 不推荐方案，后期替换

		page.setTotalRecord(count(page));
		List<WhereDTO> listParam = new ArrayList<WhereDTO>();
		Object params = page.getParams().get("where");
		if(params != null) {
			listParam = (List<WhereDTO>) params;
		}
//		 WhereDTO where = new WhereDTO();
//	        where.setKey(pkName);
//	        where.setOperator(" = ");
//	        where.setValue("");
//	     listParam.add(where);
		BaseDTO dto = sqlBuilder.findPage(listParam.toArray(new WhereDTO[listParam.size()]));
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
//		SelectDTO selectDto = new SelectDTO(dto,page);
		List<Map<String, Object>> listMap = selectList(
				 dto);
		List<T> list = MapToList(listMap);
		//end		
		
		page.setResults(list);
		return page;
	}
	
	@Override
	public Page<T> findPage(String sort, String orderBy, int pageNo,
			int pageSize, T param) {
		
			/** 
			 * 不能用于SQL中的非法字符（主要用于排序字段名） 
			 */
			final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
			final String SORT_NAME = "SORT";
			final String DIR_NAME = "DIR";
		
		    // 获取满足条件的记录总数，没有记录时返回空页数据
			int count = count(param);
			if (count < 1) {
				return Page.emptyPage();
			}

			Map<String, Object> paramMap = null;
//			try{
//				paramMap = ReflectionUtil.bean2Map(param);
//			}catch(Exception e){
//				throw new BaseDaoException("获取参数失败", e);
//			}
			// Where过滤条件
//						paramMap.put("param", param);
			// 排序条件
			if (sort != null) {
				// 排序字段不为空，过滤其中可能存在的非法字符
				sort = filterIllegalChars(sort, ILLEGAL_CHARS_FOR_SQL);
			}
			if (StringUtil.isEmpty(sort) || StringUtil.isEmpty(orderBy)) {
//							paramMap.put("sort", null);
//							paramMap.put("orderBy", null);
			} else {
				paramMap.put(SORT_NAME, sort);
				paramMap.put(DIR_NAME, orderBy);
			}
			// 分页条件
			int start = Page.getStartOfPage(
					pageNo, pageSize) - 1;
			RowBounds rowBound = new RowBounds(start, pageSize);

			List<T> lst = selectList(
					
					paramMap, rowBound);

			return new Page<T>(pageNo, pageSize, lst, count);
	}
	


	@Override
	public Integer insert(SaveDTO dto) {
		int i = -1;
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(dto.getSql());
			i = prepareStatement.executeUpdate();
			System.out.println("插入："+i);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}



	@Override
	public Map<String, Object> selectOne(BaseDTO dto) {
		Map<String,Object> map = new HashMap<>();
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(dto.getSql());
			ResultSet rs = prepareStatement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while(rs.next()) {
				for(int i=1; i <= metaData.getColumnCount(); i++) {
					String columnLabel = metaData.getColumnLabel(i);
					map.put(columnLabel, rs.getObject(columnLabel));
//					String columnClassName = metaData.getColumnClassName(i);
					
				/*
				System.out.println("请求sql的列名ColumnLabel:"+columnLabel);
				System.out.println("java中列的类型ColumnClassName:"+columnClassName);
				System.out.println("数据库中的列名ColumnName:"+metaData.getColumnName(i));
				System.out.println("数据库中列的类型ColumnType:"+metaData.getColumnType(i));
				System.out.println("数据库中列的类型的名字ColumnTypeName:"+metaData.getColumnTypeName(i));
				System.out.println("整个数值长度ColumnDisplaySize:"+metaData.getColumnDisplaySize(i));
				System.out.println("整数长度Precision:"+metaData.getPrecision(i));
				
				System.out.println("表名TableName:"+metaData.getTableName(i));
				System.out.println("数据库名CatalogName:"+metaData.getCatalogName(i));
				
				System.out.println("小数长度Scale:"+metaData.getScale(i));
				
				System.out.println("列的模式SchemaName:"+metaData.getSchemaName(i)+"==》end");
				System.out.println("========================");
				*/
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer update(String update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> selectList(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(String createOfBatch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer delete(String removeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(BaseDTO removeById) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(String findBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> selectList(Map<String, Object> paramMap, RowBounds rowBound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void select(Object parameter, RowBounds rowBounds, ResultHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Object, Object> selectMap(Object parameter, String mapKey, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectOne(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}
}
