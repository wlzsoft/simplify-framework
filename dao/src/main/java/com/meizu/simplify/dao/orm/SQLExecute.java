package com.meizu.simplify.dao.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.meizu.simplify.dao.datasource.DruidPoolFactory;
import com.meizu.simplify.dao.exception.BaseDaoException;
import com.meizu.simplify.exception.UncheckedException;

/**
 * <p><b>Title:</b><i>TODO</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午10:09:25</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午10:09:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
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
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			callback.paramCall(prepareStatement);
			Integer rs = prepareStatement.executeUpdate();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseDaoException("执行sql异常:"+e.getMessage());
		} finally {
//			free(prepareStatement,null);
			DruidPoolFactory.close();
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
		PreparedStatement prepareStatement = null;
		try {
			Connection conn = DruidPoolFactory.getConnection();
			prepareStatement = conn.prepareStatement(sql);
			Integer rs = prepareStatement.executeUpdate();
			return rs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseDaoException("执行sql异常:"+e.getMessage());
		} finally {
//			free(prepareStatement,null);
			DruidPoolFactory.close();
		}
	}
	
	/**
	 * 方法用途: 释放资源<br>
	 * 操作步骤: 注意：关闭PreparedStatement和ResultSet有性能消耗，没必要关闭，因为连接有连接池管理，但是另外两个确没有，并且跟着连接走，所以没必要关闭
	 *           需要更进一步验证上面的理论,默认con关闭时会自动关闭ResultSet、Statement，连接池未必会<br>
	 * @param preparedStatement
	 * @param rs
	 */
	public static void free(PreparedStatement preparedStatement,ResultSet rs){
		try{
			if(rs != null){
				if(!rs.isClosed()) {
					rs.close();
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(preparedStatement != null ) {
//					if(!preparedStatement.isCloseOnCompletion()){
//						preparedStatement.close();
//					} else 
					if(!preparedStatement.isClosed()) {
						preparedStatement.close();
//						System.out.println("preparedStatement非正常关闭");
					}
				}
			}catch(SQLException e){
				e.printStackTrace();
				throw new BaseDaoException("preparedStatement关闭异常:"+e.getMessage());
			}finally{
				DruidPoolFactory.close();
			}
		}
					
	}
	
	/**
	 * 
	 * 方法用途: 批量数据处理<br>
	 * 操作步骤: 暂未启用 TODO 
	 *  方式1:JDBC通用方式，可用于批处理inset和update等不同sql语句的集合，使用PreparedStatement.addBatch来实现。也可以专注于某个sql的批量数据处理 ，比如{语法语句：[insert into table(id,name) values(?,?);]}
	 *  注意：建立一个连接和执行一个sql语句的是很耗时间，特别是创建连接，所以才需要有连接池，那么执行sql语句的优化方法，就是批处理  
     *  另外，批处理的数据不是越大越好，因为可能会内存溢出，同时网络传输的过程中也是会进行拆包传输的，由于网络环境这个包的大小是不一定的，有时候打包的效率不一定就会高，这个和数据库的类型，版本都有关系的，所以我们在实践的过程中需要检验的。
	 *  <br>
	 */
	public static void executeBatch(IDataCallback<Integer> callback,String... sqlArr){
		Connection conn = null;
		PreparedStatement prepareStatement = null;
		ResultSet rs = null;
		try{
			conn = DruidPoolFactory.getConnection();
			if(sqlArr == null) {
				throw new UncheckedException("sql语句为空");
			}
			prepareStatement = conn.prepareStatement(sqlArr[0],PreparedStatement.RETURN_GENERATED_KEYS);
			for(int i=0;i<100;i++){
				callback.paramCall(prepareStatement);
				if(sqlArr.length>1) {
					for(String sql:sqlArr) {
						prepareStatement.addBatch(sql);
					}
				} else {
					prepareStatement.addBatch();//这里主要针对同一个表的同一个sql语句的操作，所以无需指定
				}
			}
			prepareStatement.executeBatch();
		}catch(Exception e){
			e.printStackTrace();
			throw new BaseDaoException("执行sql异常:"+e.getMessage());
		}finally{
//			free(prepareStatement,rs);
			DruidPoolFactory.close();
		}
	}
	
	/**
	 * 
	 * 方法用途: 可执行insert,支持预处理<br>
	 * 操作步骤: 可支持方式2的批量插入，mysql特有的语法，语法语句:[insert into table(id,name) values (?,?),(?,?),(?,?),...;]<br>
	 * @param sql
	 * @param callback
	 * @return
	 */
	public static Integer executeInsert(String sql,IDataCallback<Integer> callback) {
		PreparedStatement prepareStatement = null;
		try {
			prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			callback.paramCall(prepareStatement);
			prepareStatement.executeUpdate();
			ResultSet rs = prepareStatement.getGeneratedKeys();
			if(rs.next()) {
				int key=rs.getInt(1);
				return key;
			}
//			单个sql无需手动控制事务
//			DruidPoolFactory.startTransaction();
//			DruidPoolFactory.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new BaseDaoException("执行sql异常:"+e.getMessage());
//			DruidPoolFactory.rollback();
		} finally {
//			free(prepareStatement,null);
			DruidPoolFactory.close();
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
	 * @param clazz 如果类型不支持，那么值为空，那么变量b直接返回空，不初始化，避免Integer和Map等类型无法初始化而出现异常
	 * @return
	 */
	public static <B> List<B> executeQuery(String sql,IDataCallback<B> callback,Class<B> clazz) {
		List<B> bList= new ArrayList<B>();
		PreparedStatement prepareStatement = null;
		ResultSet rs = null;
		try {
			prepareStatement = DruidPoolFactory.getConnection().prepareStatement(sql);
			callback.paramCall(prepareStatement);
			rs = prepareStatement.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			while(rs.next()) {
				B b = callback.resultCall(rs,clazz);
				for(int i=1; i <= metaData.getColumnCount(); i++) {
					String columnLabel = metaData.getColumnLabel(i);
					b = callback.resultCall(columnLabel,rs.getObject(columnLabel),b);
				}
				bList.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
//			查询无需事务处理，无需事务回滚
			throw new BaseDaoException("执行sql异常:"+e.getMessage());
		} finally {
//			free(prepareStatement,rs);
			DruidPoolFactory.close();
		}
		return bList;
	}
	
	
}