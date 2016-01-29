package com.meizu.simplify.dao.orm;

import java.util.List;
import java.util.Map;

import com.meizu.simplify.dao.ResultHandler;
import com.meizu.simplify.dao.RowBounds;
import com.meizu.simplify.dao.dto.BaseDTO;
import com.meizu.simplify.dao.dto.SaveDTO;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月29日 下午1:44:38</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月29日 下午1:44:38</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class SqlSessionTemplate<T> implements SqlSession<T>{

	@Override
	public SqlSession setMaxResults(int maxResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMaxResults() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SqlSession setFirstResult(int startPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFirstResult() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SqlSession setHint(String hintName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlSession setParameter(String name, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlSession setParameter(int position, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParameterValue(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParameterValue(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void select(String sqlName, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer insert(String sqlName, SaveDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(String sqlName, String createOfBatch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> selectList(String sqlName, Map<String, Object> paramMap, RowBounds rowBound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Object, Object> selectMap(String sqlName, Object parameter, String mapKey, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(String sqlName, String findBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectOne(String sqlName, String findAllCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> selectOne(String sqlName, BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectOne(String sqlName, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(String sqlName, BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(String sqlName, BaseDTO removeById) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(String sqlName, String removeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer update(String sqlName, String update) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> selectList(String sqlName, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
