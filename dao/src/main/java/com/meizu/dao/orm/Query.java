package com.meizu.dao.orm;
/**
  * <p><b>Title:</b><i>TODO</i></p>
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
public interface Query {
	
	public Query setMaxResults(int maxResult);

	
	public int getMaxResults();

	
	public Query setFirstResult(int startPosition);

	
	public int getFirstResult();

	
	public Query setHint(String hintName, Object value);

	
//	public Map<String, Object> getHints();

	
//	public <T> Query setParameter(Parameter<T> param, T value);

	
//	public Query setParameter(Parameter<Calendar> param, Calendar value,TemporalType temporalType);

	
//	public Query setParameter(Parameter<Date> param, Date value,TemporalType temporalType);

	
	public Query setParameter(String name, Object value); 

	
//	public Query setParameter(String name, Calendar value,TemporalType temporalType); 

	
//	public Query setParameter(String name, Date value, TemporalType temporalType);

	
	public Query setParameter(int position, Object value);

	
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
}
