package com.meizu.dao.mybatis;

import java.util.List;
import java.util.Map;

import com.meizu.dao.dto.BaseDTO;
import com.meizu.dao.dto.SaveDTO;

/**
  * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年1月27日 下午8:55:24</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年1月27日 下午8:55:24</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class SqlSessionTemplate<T> extends SqlSession{

	public void select(String sqlName, Object parameter, RowBounds rowBounds, ResultHandler handler) {
		// TODO Auto-generated method stub
		
	}

	public Integer insert(String sqlName, SaveDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public void insert(String sqlName, String createOfBatch) {
		// TODO Auto-generated method stub
		
	}

	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}

	public List<T> selectList(String sqlName, Map<String, Object> paramMap, RowBounds rowBound) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Object, Object> selectMap(String sqlName, Object parameter, String mapKey, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> selectList(String sqlName, String findBy) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer selectOne(String sqlName, String findAllCount) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> selectOne(String sqlName, BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer selectOne(String sqlName, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Map<String, Object>> selectList(String sqlName, BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer delete(String sqlName, BaseDTO removeById) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer delete(String sqlName, String removeAll) {
		// TODO Auto-generated method stub
		return null;
	}

	public Integer update(String sqlName, String update) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<T> selectList(String sqlName, Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

}
