package com.meizu.simplify.dao.orm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.dao.exception.BaseDaoException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午10:09:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午10:09:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">luchuangye</a>
 * @version Version 0.1
 *
 */
public class SQLExecute {
	
	/**
	 * 
	 * 方法用途: 执行delete，update 根据param的where条件来更新或是删除<br>
	 * 操作步骤: TODO<br>
	 * @param param
	 * @return
	 */
	public static Integer executeUpdate(String sql,Object... params) {
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
	public static Integer executeUpdate(String sql,IDataCallback<Integer> callback) {
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
	public static Integer executeUpdate(String sql) {
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
	
	/**
	 * 
	 * 方法用途: 可执行insert,支持预处理<br>
	 * 操作步骤: TODO<br>
	 * @param sql
	 * @param callback
	 * @return
	 */
	public static Integer executeInsert(String sql,IDataCallback<Integer> callback) {
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
	public static <B> List<B> executeQuery(String sql,IDataCallback<B> callback,Class<B> clazz) {
		List<B> bList= new ArrayList<B>();
		try {
			PreparedStatement prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			callback.paramCall(prepareStatement);
			ResultSet rs = prepareStatement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while(rs.next()) {
				B b = callback.resultCall(rs,clazz);
				for(int i=1; i <= metaData.getColumnCount(); i++) {
					String columnLabel = metaData.getColumnLabel(i);
					b = callback.resultCall(columnLabel,rs.getObject(columnLabel),b);
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
}