package com.meizu.simplify.dao.orm;

import java.util.List;
import java.util.Map;

import com.meizu.simplify.dao.ResultHandler;
import com.meizu.simplify.dao.RowBounds;
import com.meizu.simplify.dao.dto.BaseDTO;
import com.meizu.simplify.dao.dto.SaveDTO;

/**
  * <p><b>Title:</b><i>未测试，待优化，替换sqlsession和sqlsessiontemplate</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c);2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午9:06:17</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午9:06:17</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public interface SqlSession<T> {
	
	public SqlSession setMaxResults(int maxResult);
	
	public int getMaxResults();

	public SqlSession setFirstResult(int startPosition);

	public int getFirstResult();

	public SqlSession setHint(String hintName, Object value);

//	public Map<String, Object> getHints();

//	public <T> Query setParameter(Parameter<T> param, T value);
	
//	public Query setParameter(Parameter<Calendar> param, Calendar value,TemporalType temporalType);

//	public Query setParameter(Parameter<Date> param, Date value,TemporalType temporalType);
	
	public SqlSession setParameter(String name, Object value); 
	
//	public Query setParameter(String name, Calendar value,TemporalType temporalType); 
	
//	public Query setParameter(String name, Date value, TemporalType temporalType);
	
	public SqlSession setParameter(int position, Object value);
	
//	public Query setParameter(int position, Calendar value,	TemporalType temporalType);
	
//	public Query setParameter(int position, Date value,	TemporalType temporalType);

//	public Set<Parameter<?>> getParameters();

//	public Parameter<?> getParameter(String name);

//	public <T> Parameter<T> getParameter(String name, Class<T> type);
	
//	public Parameter<?> getParameter(int position);
	
//	public <T> Parameter<T> getParameter(int position, Class<T> type);
	
//	public boolean isBound(Parameter<?> param);
	
//	public <T> T getParameterValue(Parameter<T> param);
	
	public Object getParameterValue(String name);
	
	public Object getParameterValue(int position);
	
//	public Query setFlushMode(FlushModeType flushMode);
	
//	public FlushModeType getFlushMode();

//	public Query setLockMode(LockModeType lockMode);
	
//	public LockModeType getLockMode();
	
	public <T> T unwrap(Class<T> cls);
	
	
	
	public void select(Object parameter, RowBounds rowBounds, ResultHandler handler);

	public Integer insert(SaveDTO dto);

	public void insert(String createOfBatch);

	public void flushStatements();

	public List<T> selectList(Map<String, Object> paramMap, RowBounds rowBound);

	public Map<Object, Object> selectMap(Object parameter, String mapKey, RowBounds rowBounds);

	public List<Map<String, Object>> selectList(String findBy);

	public Integer selectOne(String findAllCount);

	public Map<String, Object> selectOne(BaseDTO dto);

	public Integer selectOne(Map<String, Object> paramMap);
	
	public List<Map<String, Object>> selectList(BaseDTO dto);

	public Integer delete(BaseDTO removeById);

	public Integer delete(String removeAll);

	public Integer update(String update);

	public List<T> selectList(Map<String, Object> paramMap);
}
