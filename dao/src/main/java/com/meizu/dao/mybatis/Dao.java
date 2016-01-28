package com.meizu.dao.mybatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.dao.Constant;
import com.meizu.dao.Query;
import com.meizu.dao.annotations.Column;
import com.meizu.dao.annotations.Key;
import com.meizu.dao.annotations.Table;
import com.meizu.dao.annotations.Transient;
import com.meizu.dao.mybatis.base.BaseDTO;
import com.meizu.dao.mybatis.base.BaseDTO.LinkType;
import com.meizu.dao.mybatis.base.SaveDTO;
import com.meizu.dao.mybatis.base.WhereDTO;
import com.meizu.dao.util.BuildInfo;
import com.meizu.dao.util.Page;
import com.meizu.entity.IdEntity;
import com.meizu.simplify.ioc.annotation.Resource;
import com.meizu.simplify.utils.StringUtil;
//import com.meizu.exception.BaseDaoException;
//import com.meizu.util.BeanUtils;

/**
 * 
 * <p><b>Title:</b><i> 基于Mybatis的基础泛型DAO实现类</i></p>
 * <p>Desc:1. SqlSessionTemplate的实现方式
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
//@Repository//不建议这样创建实例，因为是未定义类型的泛型类
public class Dao<T extends IdEntity<Serializable,Integer>, PK extends Serializable>   implements IBaseDao<T, PK> {
	//需要考虑是使用abstract或不是
	//public abstract class Dao<T extends BaseEntity<T>, PK extends Serializable>  implements IBaseDao<T, PK> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 作cache 结构{T类的镜像,{数据库列名,实体字段名}}
	 */
	private static final Map<Class<?>, Map<String, String>> classFieldMap = new HashMap<Class<?>, Map<String, String>>();
	public static final String SQLNAME_SEPARATOR = ".";
	/** 
	 * 不能用于SQL中的非法字符（主要用于排序字段名） 
	 */
	public static final String[] ILLEGAL_CHARS_FOR_SQL = {",", ";", " ", "\"", "%"};
	private static final String SORT_NAME = "SORT";
	private static final String DIR_NAME = "DIR";
	
	public static final String SQL_CREATE = "create";   
//	public static final String SQL_FINDBYID = "findById";
	public static final String SQL_FINDBYWHERE = "findByWhere";
	private static final String SQL_FINDBY = "findBy";
	private static final String SQL_CREATEOFBATCH = "createOfBatch";
	private static final String SQL_FETCHSEQNEXTVAL = "fetchSeqNextval";
	private static final String SQL_REMOVEBY = "removeBy";
	private static final String SQL_REMOVEALL = "removeAll";
	private static final String SQL_MODIFY = "modify";
	private static final String SQL_FINDALL = "findAll";
	private static final String SQL_FINDALLCOUNT = "findAllCount";
	public static final String SQL_FINDBYPAGE = "findByPage";   
	public static final String SQL_FINDLISTBY = "findListBy";
	public static final String SQL_COUNT = "count";
	
	@Resource
	private BuildInfo<T> buildInfo;
	/**
	 * 配置文件中，有bean的注入
	 */
	@Resource
    private SqlSession sqlSession; 
	
	/**
	 * 配置文件中，有bean的注入,和上面共用,下面的是对SqlSession经过了一层的封装--推荐使用
	 */
	@Resource(name = "sqlSession")
	private SqlSessionTemplate sqlSessionTemplate;
	
	private Class<T> entityClass;
	/**
	 * 实体类主键名称
	 */
	private String pkName;
	/**
	 * 实体类ID字段名称
	 */
	private String idName;
	/**
	 * 主键的序列
	 */
	private String seq;
	/**
	 * 表名
	 */
	private String tableName;

	private Map<String, String> currentColumnFieldNames;

	private SQLBuilder<T> sqlBuilder;
	/**
	 * SqlMapping命名空间
	 */
	private String sqlNamespace;
	
	
	public SqlSession getSqlSession() {
		return sqlSession;
	}

	/**
	 * 
	 * 方法用途: 暂时不可用，不会注入sqlSession，后期可以喝sqlSessionTemplate做关联<br>
	 * 操作步骤: TODO<br>
	 * @param sqlSession
	 */
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public void setSqlSession(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
	}

	public SqlSessionTemplate getSqlSessionTemplate() {
		return sqlSessionTemplate;
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSessionTemplate = sqlSessionTemplate;
	}
	
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
	 * 方法用途: 将SqlMapping命名空间与给定的SqlMapping名组合在一起<br>
	 * 操作步骤: sqlName SqlMapping名<br>
	 * @return 组合了SqlMapping命名空间后的完整SqlMapping名
	 */
	protected String getSqlName(String sqlName) {
		return sqlNamespace + SQLNAME_SEPARATOR + sqlName;
	}

	
	/**
	 * 
	 * 方法用途: 获取SqlMapping命名空间<br>
	 * 操作步骤: TODO<br>
	 * @return SqlMapping命名空间
	 */
	public String getSqlNamespace() {
		return sqlNamespace;
	}

	/**
	 * 
	 * 方法用途: 设置SqlMapping命名空间<br>
	 * 操作步骤: 此方法只用于注入SqlMapping命名空间，以改变默认的SqlMapping命名空间，
	 * 不能滥用此方法随意改变SqlMapping命名空间<br>
	 * @param sqlNamespace sqlNamespace SqlMapping命名空间
	 */
	public void setSqlNamespace(String sqlNamespace) {
		this.sqlNamespace = sqlNamespace;
	}
	
	/**
	 * 
	 * 方法用途: 生成主键值<br>
	 * 操作步骤: 默认情况下什么也不做；
	 * 如果需要生成主键，需要由子类重写此方法根据需要的方式生成主键值<br>
	 * @param t 要持久化的对象
	 */
	protected void generateId(T t) {
		buildInfo.buildId(t);
	}
	
	/**
	 * 构造方法
	 * @param clazz  业务实体类
	 */
	public Dao(Class<T> entityClass) {
		this.entityClass = entityClass;
		sqlNamespace = getDefaultSqlNamespace(entityClass);
		DaoInit(entityClass);
	}
	/**
	 * 方法用途: 获取mybatis的Session<br>
	 * 操作步骤: TODO<br>
	 * @return  返回mybatis的Session。
	 */
	public SqlSession getSession() {
		return sqlSessionTemplate;
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

		currentColumnFieldNames = classFieldMap.get(entityClass);
		if (null == currentColumnFieldNames) {
			currentColumnFieldNames = new LinkedHashMap<String, String>();
			classFieldMap.put(entityClass, currentColumnFieldNames);
		}
		Transient trans = entityClass.getAnnotation(Transient.class);
		buildFieldInfo(entityClass,trans);
		
		Table table = entityClass.getAnnotation(Table.class);
		if (null == table) {
			throw new RuntimeException("类" + entityClass
					+ "需要指定@Table注解!");
		}
		tableName = table.name();

		sqlBuilder = new SQLBuilder<T>(currentColumnFieldNames.keySet(),
				tableName, pkName, seq);
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
		String fieldName = null;
		String columnName = null;
		for (Field field : fields) {
			
			String preField = Modifier.toString(field.getModifiers());
			if(preField.contains("final")) {
				continue;
			}
			
//			Annotation[] anno = field.getAnnotations();
//			for (int i = 0; i < anno.length; i++) {
//				System.out.println(anno+"----test");
//			}
			if (field.isAnnotationPresent(Transient.class)) {
				continue;
			}
			if(transArr != null) {
				boolean isIgnore = false;
				for (int i = 0; i < transArr.length; i++) {
					if(field.getName().equals(transArr[i])) {
						isIgnore = true;
						break;
					}
				}
				if(isIgnore) {
					continue;
				}
			}
			fieldName = field.getName();
			Column tableColumn = field.getAnnotation(Column.class);
			if (null != tableColumn) {
				columnName = tableColumn.value();
			} else {
				columnName = null;
			}
			// 如果未标识特殊的列名，默认取字段名
			columnName = (StringUtil.isEmpty(columnName) ? fieldName.toUpperCase() : columnName);
			currentColumnFieldNames.put(columnName, fieldName);
			if (field.isAnnotationPresent(Key.class)) {
				// 取得ID的列名
				idName = fieldName;
				pkName = columnName;
				Key primaryKey = field.getAnnotation(Key.class);
				seq = primaryKey.seq();
			}
		}
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
//			ReflectionUtil.invokeSetterMethod(t, key, val);
		}
		return t;
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
		return sqlSessionTemplate.selectMap(
				getSqlName(statement),
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
		sqlSessionTemplate.select(
				getSqlName(statement),
				parameter, rowBounds, handler);
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
	
		
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#create(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public Integer create(T t) {
		SaveDTO dto = sqlBuilder.create(t, currentColumnFieldNames);
		
		Integer res = sqlSessionTemplate.insert(getSqlName(SQL_CREATE),
				dto);
		t.setId(dto.getId());
		return res;
	}
	

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#create(java.util.List)
	 */
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
				sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH),sqlBuilder.createOfBatch(temp,currentColumnFieldNames, pkVal));
				sqlSessionTemplate.flushStatements();
				temp = new ArrayList<T>();
			}
		}
		sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH), sqlBuilder.createOfBatch(temp, currentColumnFieldNames, pkVal));
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#create(java.util.List)
	 */
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
				sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH),sqlBuilder.createOfBatchByMycat(temp,currentColumnFieldNames, pkVal));
				sqlSessionTemplate.flushStatements();
				temp = new ArrayList<T>();
			}
		}
		sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH), sqlBuilder.createOfBatchByMycat(temp, currentColumnFieldNames, pkVal));
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
				sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH),
						sqlBuilder.createOfBatch(temp,
								currentColumnFieldNames, pkVal));
				sqlSessionTemplate.flushStatements();
				temp = new ArrayList<T>();
			}
		}
		sqlSessionTemplate.insert(getSqlName(SQL_CREATEOFBATCH), sqlBuilder
				.createOfBatch(temp, currentColumnFieldNames, pkVal));
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#save(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public Integer save(T t) {
		generateId(t);
		return saveMeta(t);
	}

	public Integer saveMeta(T t) {
		if (StringUtil.isEmpty(seq)) {
			return this.create(t);
		}
		//生成序列:oracle的seq，或是mysql等数据库模拟的序列的生成,暂时不用，采用数据库自增
//		Long nextval = sqlSessionTemplate.selectOne(getSqlName(SQL_FETCHSEQNEXTVAL),"SELECT ".concat(seq).concat(" FROM DUAL"));
//		ReflectionUtil.invokeSetterMethod(t, idName, nextval);
		return this.create(t);
	}
	
	@Override
	public Integer saveOrUpdate(T t) {
		buildInfo.buildId(t);
		if(t.getId()!=null) {
			return updateMeta(t);
		} else {
			return saveMeta(t);
		}
	}
	

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#save(java.util.List)
	 */
	@Override
	public void save(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		if (StringUtil.isEmpty(seq)) {
			this.create(list);
			return;
		}
//		logger.info("生成序列开始:----------start----------");
//		for (T t : list) {
//			Long nextval = sqlSessionTemplate.selectOne(getSqlName(SQL_FETCHSEQNEXTVAL),
//					"SELECT ".concat(seq).concat(" FROM DUAL"));
//			ReflectionUtil.invokeSetterMethod(t, idName, nextval);
//		}
//		logger.info("生成序列结束:---------- end ----------");

//		this.create(list);
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#save(java.util.List)
	 */
	@Override
	public void saveByMycat(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		if (StringUtil.isEmpty(seq)) {
			this.createByMycat(list);
			return;
		}
	}
	

	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#remove(java.io.Serializable)
	 */
	@Override
	public Integer remove(PK id) {
		return sqlSessionTemplate.delete(getSqlName(SQL_REMOVEBY),
				sqlBuilder.removeById(id));
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#remove(com.meizu.entity.baseEntity.IdEntity)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer remove(T entity) {
		return remove((PK) entity.getId());
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#remove(java.io.Serializable[])
	 */
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
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#remove(java.util.List)
	 */
	@Override
	public Integer remove(List<PK> ids) {
		if (null == ids || ids.isEmpty()) {
			return -1;
		}
		List<PK> temp = new ArrayList<PK>();
		for (int  i = 0; i < ids.size(); i++) {
			temp.add(ids.get(i));
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
				sqlSessionTemplate.delete(getSqlName(SQL_REMOVEBY),sqlBuilder.removeOfBatch(temp));
				sqlSessionTemplate.flushStatements();
				temp = new ArrayList<PK>();
			}
		}
		return  sqlSessionTemplate.delete(getSqlName(SQL_REMOVEBY),sqlBuilder.removeOfBatch(temp));
	}

	
	

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#removeAll()
	 */
	@Override
	public Integer removeAll() {
		return sqlSessionTemplate.delete(getSqlName(SQL_REMOVEALL), sqlBuilder.removeAll());
	}
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#remove(java.lang.String, java.lang.Object)
	 */
	@Override
	public Integer remove(String name, Object value) {
//		Query query = createQuery("delete from " + clazz.getName() + " where "
//				+ name + "=?", value);
//		query.executeUpdate();
		
		Integer count = sqlSessionTemplate.delete(
				getSqlName(SQL_REMOVEBY), sqlBuilder.remove(name,value));
		return count;
	}
	

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#update(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public Integer update(T t) {
		generateId(t);
		return updateMeta(t);

	}

	public Integer updateMeta(T t) {
		//TODO 待实现， 可以抽取成公用字段过来模块，针对单个方法的，比如通过currentColumnFieldNames来过滤掉不需要执行的字段
		return sqlSessionTemplate.update(getSqlName(SQL_MODIFY),
				sqlBuilder.update(t, currentColumnFieldNames));
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#update(java.util.List)
	 */
	@Override
	public void update(List<T> list) {
		if (null == list || list.isEmpty()) {
			return;
		}
		for (int i=0; i <  list.size(); i++) {
			this.update(list.get(i));
			if (i > 0 && i % Constant.FLUSH_CRITICAL_VAL == 0) {
				sqlSessionTemplate.flushStatements();
			}
		}
	}
	
	
	


	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#load(java.io.Serializable)
	 */
	@Override
	public T load(PK id) {
		return findById(id);
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#get(java.io.Serializable)
	 */
	@Override
	public T get(PK id) {
		return findById(id);
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#persist(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public void persist(T entity) {
		save(entity);
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findById(java.io.Serializable)
	 */
	@Override
	public T findById(PK id) {
		Map<String, Object> resultMap = sqlSessionTemplate.selectOne(
				getSqlName(SQL_FINDBYWHERE), sqlBuilder.findById(id));
		return MapToEntity(resultMap, this.entityClass);
//		T t = sqlSessionTemplate.selectOne(
//				getSqlName(SQL_FINDBYID), sqlBuilder.findById(id));
//		return t;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findById(java.io.Serializable, java.lang.Class)
	 */
	@Deprecated
	@Override
	public T findById(PK id, Class<T> t) {
		return findById(id);
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findByIds(java.io.Serializable[])
	 */
	@Override
	public List<T> findByIds(PK[] idArr) {
		List<Map<String, Object>> listMap = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDBY), sqlBuilder.findByIds(idArr));
		List<T> list = MapToList(listMap);
		return list;
	}
	@Override
	public List<T> findByMutil(String name, String values) {
		List<Map<String, Object>> listMap = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDBY), sqlBuilder.findByMutil(name,values));
		List<T> list = MapToList(listMap);
		return list;
	}
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findBy(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public List<T> findBy(T param){
		return findBy(param, null, null);
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findBy(com.meizu.entity.baseEntity.IdEntity, java.lang.String, java.lang.String)
	 */
	@Override
	public List<T> findBy(T param, String sort, String orderBy) {
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
		List<T> lst = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDLISTBY), paramMap);
		return lst;
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findBy(java.lang.String, java.lang.Object, int, int, java.lang.String, java.lang.String)
	 */
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
		List<T> lst = sqlSessionTemplate.selectList(getSqlName(sqlName), paramMap,rowBound);
		return lst;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findBy(java.lang.String, java.lang.Object, java.lang.String, boolean)
	 */
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
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findBy(java.lang.String, java.lang.Object)
	 */
	@Override
	public List<T> findBy(String name,Object value) {
		
		List<Map<String, Object>> listMap = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDBY), sqlBuilder.findBy(name,value));
		List<T> list = MapToList(listMap);
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findAll(java.lang.String, java.lang.Boolean)
	 */
	@Override
	public List<T> findAll(String orderBy, Boolean isAsc) {
		return null;
	}
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findAll()
	 */
	@Override
	public List<T> findAll() {
		List<Map<String, Object>> resultMapList = sqlSessionTemplate
				.selectList(getSqlName(SQL_FINDALL), sqlBuilder.findAll());
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

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findAllCount()
	 */
	@Override
	public Integer findAllCount() {
		Integer count = sqlSessionTemplate.selectOne(getSqlName(SQL_FINDALLCOUNT),
				sqlBuilder.findAllCount());
		return count;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#count()
	 */
	@Override
	public Integer count() {
		return findAllCount();
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#count(com.meizu.data.mybatis.Criteria)
	 */
	@Override
	public Integer count(Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#count(com.meizu.data.mybatis.Criteria)
	 */
	@Override
	public Integer count(Page<T> page) {
		List<WhereDTO> listParam = new ArrayList<WhereDTO>();
		
		//其他地方有冗余，需重构start
		Object params = page.getParams().get("where");
		if(params != null) {
			listParam = (List<WhereDTO>) params;
		}
		BaseDTO dto = sqlBuilder.count(listParam);
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
		//其他地方有冗余，需重构end
		
//		java.util.HashMap<String,Object> a = sqlSessionTemplate.selectOne(getSqlName(SQL_FINDBYPAGE),dto);
//		Integer count = DataUtil.parseInt(a.get("count(1)"));
		//bugs fix by lcy 2015/05/26
//		Integer count = sqlSessionTemplate.selectOne(getSqlName(SQL_COUNT),dto);
//		return count;
		return 0;
	}
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#count(java.lang.String, java.lang.Object[])
	 */
	@Override
	public Integer count(String sql, Object... values) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#count(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public Integer count(T param) {
		Map<String, Object> paramMap = null;
//		try{
//			paramMap = ReflectionUtil.bean2Map(param);
//		}catch(Exception e){
//			throw new BaseDaoException("获取参数失败", e);
//		}
//		paramMap.put("param", param);
		return (Integer)sqlSessionTemplate.selectOne(
				getSqlName(SQL_COUNT), paramMap);
	}

	
	
	//-----------------------------------------------------------------------以下方法待实现
	
	
	
	
	
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#merge(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public T merge(T entity) {
		return null;
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#replicate(com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public void replicate(T entity) {
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#clear()
	 */
	@Override
	public void clear() {
	}


	

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#isUnique(com.meizu.entity.baseEntity.IdEntity, java.lang.String)
	 */
	@Override
	public Boolean isUnique(T entity, String propNames) {
		return false;
	}


	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findUnique(com.meizu.data.mybatis.Criteria)
	 */
	@Override
	public T findUnique(Criteria criteria) {
//		return findUnique(name,value);
		return criteria.uniqueResult();
	}

	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findUnique(java.lang.String, java.lang.Object)
	 */
	@Override
	public T findUnique(String name, Object value) {
		Map<String, Object> resultMap = sqlSessionTemplate.selectOne(
				getSqlName(SQL_FINDBYWHERE), sqlBuilder.findByProperties(name, value));
		return MapToEntity(resultMap, this.entityClass);
		
	}

	
	
	
	public Query createQuery(String sql, Object[] values) {
		Query query = new MybatisQuery(this,getSqlName(SQL_FINDBYPAGE),sql,values);
		return query;
	}
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(com.meizu.util.Page, java.lang.Object[])
	 */
	@Override
	public List<T> find(Page<T> page,Object... values) {
		
		List<WhereDTO> listParam = new ArrayList<WhereDTO>();
		Object params = page.getParams().get("where");
		if(params != null) {
			listParam = (List<WhereDTO>) params;
		}
		BaseDTO dto = sqlBuilder.findPage(listParam);
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
		List<Map<String, Object>> listMap = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDBYPAGE), dto);
		List<T> list = MapToList(listMap);
		//end		
		
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(com.meizu.util.Page, java.lang.Object[])
	 */
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
		BaseDTO dto = sqlBuilder.findPage(listParam);
		dto.setLinkType(LinkType.AND);
		dto.setPage(page);
		dto.setLimit("true");
//		SelectDTO selectDto = new SelectDTO(dto,page);
		List<Map<String, Object>> listMap = sqlSessionTemplate.selectList(
				getSqlName(SQL_FINDBYPAGE), dto);
		List<T> list = MapToList(listMap);
		//end		
		
		page.setResults(list);
		return page;
	}
	
	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(java.lang.String, java.lang.String, int, int, com.meizu.entity.baseEntity.IdEntity)
	 */
	@Override
	public Page<T> findPage(String sort, String orderBy, int pageNo,
			int pageSize, T param) {
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

			List<T> lst = sqlSessionTemplate.selectList(
					getSqlName(SQL_FINDBYPAGE),
					paramMap, rowBound);

			return new Page<T>(pageNo, pageSize, lst, count);
	}
	

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(int, int, com.meizu.data.mybatis.Criteria)
	 */
	@Override
	public Page<T> findPage(int pageNo, int pageSize, Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(java.lang.String, java.lang.String, int, int, com.meizu.data.mybatis.Criteria)
	 */
	@Override
	public Page<T> findPage(String sort, String orderBy, int pageNo,
			int pageSize, Criteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	

	/* (non-Javadoc)
	 * @see com.meizu.data.mybatis.IBaseDao#findPage(java.lang.String, java.lang.String, com.meizu.util.Page, java.lang.Object[])
	 */
	@Override
	public Page<T> findPage(String sort, String orderBy, Page<T> page,
			Object... values) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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

}
