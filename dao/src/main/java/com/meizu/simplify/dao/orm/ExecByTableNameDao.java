package com.meizu.simplify.dao.orm;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.BatchOperator;
import com.meizu.simplify.dao.Query;
import com.meizu.simplify.dao.datasource.ConnectionManager;
import com.meizu.simplify.dao.orm.base.IDataCallback;
import com.meizu.simplify.dao.orm.base.SQLExecute;
import com.meizu.simplify.ioc.annotation.Bean;
import com.meizu.simplify.ioc.annotation.Resource;
/**
 * <p><b>Title:</b><i>基于表名的基础dao实现</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年2月24日 上午11:35:31</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年2月24日 上午11:35:31</p>
 * @author <a href="mailto:luchuangye@meizu.com" >luchuangye</a>
 * @version Version 0.1
 *
 */
@Bean
public class ExecByTableNameDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecByTableNameDao.class);
	
	@Resource
	private ConnectionManager connectionManager;
	
	/**
	 * 方法用途: 忽略的字段<br>
	 * 操作步骤: TODO暂时不可用，由于是单例，后续再考量<br>
	 * @param columnNames
	 * @return
	 */
	public ExecByTableNameDao transiented(String... columnNames) {
		LOGGER.info("忽略的字段");
		return this;
	}


	/**
	 * 方法用途: 新增记录<br>
	 * 操作步骤: TODO<br>
	 * @param tableName 表名
	 * @param columns 列信息。格式：列名,列值,列名,列值,列名,列值,列名,列值
	 * @return 插入成功的id
	 */
	public Integer save(String tableName,Object... columns) {
		if(columns == null) {
			return null;
		}
		List<Object> params = new ArrayList<>();
		StringBuilder sqlBuilder = new StringBuilder();
		for (int i=0; i < columns.length/2;i++) {
			Object columnName = columns[i*2];
			Object columnValue = columns[i*2+1];
			params.add(columnValue);
			if(i > 0) {
				sqlBuilder.append(","+columnName);
			}else {
				sqlBuilder.append(columnName);
			}
		}
		String sql = preCreate(tableName,sqlBuilder.toString(),columns.length/2);
		Integer key = SQLExecute.executeInsert(connectionManager,sql, new IDataCallback<Integer>() {},params.toArray());
		return key;
	}
	
	/**
     * 生成新增的SQL--预处理方式，prestatement方式
     * 
     * @param t
     * @param currentColumnFieldNames
     * @param values 
     * @param columns 
     * @return
     */
    private String preCreate(String tableName,String otherIdColumnsStr,int otherIdColumnsSize) {
    	
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                .append(otherIdColumnsStr).append(") values(");
        String sql = sqlBuild.toString();
        String charValue="";
        for(int i=0; i < otherIdColumnsSize;i++) {
        	charValue+=",?";
        }
        charValue = charValue.substring(1);
        LOGGER.info("生成的SQL为: " + sql+""+charValue+")");
        return sql+""+charValue+")";
    }

	/**
	 * 方法用途: 删除记录<br>
	 * 操作步骤: TODO<br>
	 * @param tableName 表名
	 * @param columnName 列名
	 * @param values 待删除记录的列值数组
	 * @return
	 */
	public Integer remove(String tableName,String columnName,Object... values) {
		return SQLExecute.executeUpdate(connectionManager,removeOfBatch(tableName,columnName,values.length), values);
	}
	
	/**
     * 生成根据IDs批量删除的SQL
     * 
     * @param ids
     * @return
     */
    public String removeOfBatch(String tableName,String columnName,int idsLength) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(tableName);
        String sql = sqlBuild.toString();
        
        String value = columnName+" IN (";
        for (int i=0; i < idsLength; i++) {
            if (i > 0 && i % (BatchOperator.DELETE_CRITICAL_VAL.getSize() - 1) == 0) {
            	if(i == idsLength-1) {
            		value += "?" + ")";
            	} else {
            		value += "?" + ") OR "+ columnName+" IN (";
            	}
            } else {
            	if(i == idsLength-1) {
            		value+="?" + ")";
            	} else {
            		value+="?" + ",";
            	}
            }
        }
        sql = sql+" where "+value;
        LOGGER.info("生成的SQL为: " + sql);
        return sql;
    }

	/**
	 * 方法用途: 根据属性的值查找唯一的业务实体。<br>
	 * 操作步骤: TODO<br>
	 * @param name  属性名
	 * @param value 属性值
	 * @return 返回指定唯一的业务实体，如果没有找到则返回null。
	 */
	public Query<?> findUnique(String name, Object value) {
		return null;
	}
	
}
