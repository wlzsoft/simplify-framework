package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.meizu.simplify.dao.ResultHandler;
import com.meizu.simplify.dao.RowBounds;
import com.meizu.simplify.dao.datasource.DruidPoolFactory;
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
	public void select(Object parameter, RowBounds rowBounds, ResultHandler handler) {
		// TODO Auto-generated method stub
		
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
	public void insert(String createOfBatch) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushStatements() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<T> selectList(Map<String, Object> paramMap, RowBounds rowBound) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Object, Object> selectMap(Object parameter, String mapKey, RowBounds rowBounds) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(String findBy) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer selectOne(String findAllCount) {
		// TODO Auto-generated method stub
		return null;
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
//					String columnClassName = metaData.getColumnClassName(i);
					map.put(columnLabel, rs.getObject(columnLabel));
					
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
	public Integer selectOne(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> selectList(BaseDTO dto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(BaseDTO removeById) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Integer delete(String removeAll) {
		// TODO Auto-generated method stub
		return null;
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

}
