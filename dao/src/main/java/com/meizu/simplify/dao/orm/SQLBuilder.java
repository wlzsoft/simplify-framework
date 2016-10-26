package com.meizu.simplify.dao.orm;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.BatchOperator;
import com.meizu.simplify.dao.dto.BaseDTO.LinkType;
import com.meizu.simplify.dao.dto.SqlDTO;
import com.meizu.simplify.dao.dto.WhereDTO;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;
 
/**
 * 
 * <p><b>Title:</b><i> sql构造器</i></p>
 * <p>Desc: TODO 反射待优化</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午4:29:25</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午4:29:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class SQLBuilder<T> {
     
    protected static final Logger logger = LoggerFactory.getLogger(SQLBuilder.class);
    private List<String> columns;
    private Map<String,String> columnsMeta;//用于create语句使用
    private List<String> otherIdColumns;
	private String tableName;
    private String columnsStr;
    private String otherIdColumnsStr;
    private String pkName;
    private ThreadLocal<Integer> tableIndexLocal = new ThreadLocal<>();
    public SQLBuilder(List<String> otherIdColumns,List<String> columns, String tableName, String pkName,Map<String,String> columnsMeta) {
        super();
        this.columnsMeta = columnsMeta;
        this.columns = columns;
        this.otherIdColumns = otherIdColumns;
        this.tableName = tableName;
        this.pkName = pkName;
        this.columnsStr = StringUtil.join(columns, ",");
        this.otherIdColumnsStr = StringUtil.join(otherIdColumns, ",");
    }
    
    public List<String> getColumns() {
		return columns;
	}
    
    public List<String> getOtherIdColumns() {
		return otherIdColumns;
	}
    
    public String getOtherIdColumnsStr() {
		return otherIdColumnsStr;
	}
    
    public String getTableName() {
    	if(tableIndexLocal.get() == null) {
    		return this.tableName;
    	} else {
    		return this.tableName+"_"+tableIndexLocal.get();
    	}
	}
     
    /**
     * 提供给生成count SQL 使用
     * 
     * @param t
     * @param currentColumnFieldNames
     * @return
     */
    public SqlDTO whereValue(T t,Map<String, String> currentColumnFieldNames) {
        
    	List<Object> values = new LinkedList<Object>();
        String whereName = "";
        for (String column : otherIdColumns) {
            Field field = ReflectionUtil.getField(t,currentColumnFieldNames.get(column));
            
            try {
            	Object value = field.get(t);
            	if(value != null) {
            		values.add(value);
            		whereName += " "+LinkType.AND+" " + column + "=?";
        		}
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
           
        }
        SqlDTO dto = new SqlDTO();
        // FIXED geny 2016-05-27 增加空字符串判断 风险提醒：这个分支会导致拖死数据库 ，考虑整合优化  TODO 整合块1
        if(!StringUtil.isEmpty(whereName)){
        	 whereName = whereName.substring(4);
             dto.setWhereName(whereName);
             dto.setWhereValues(values.toArray());
        }
        return dto;
    }
     
    /**
     * 处理value
     * 
     * @param value
     * @param field 
     * @return
     */
   /* private Object handleValue(Object value) {
        if (value instanceof String) {
            value = "\'" + value + "\'";
        } else if (value instanceof Date||value instanceof java.sql.Date) {//对应实体中java.sql.Date类型 的属性处理
            Date date = (Date) value;
            String dateStr = DateUtil.format(date);
            value = "'"+dateStr+"'";
        } else if (value instanceof Boolean) {
            Boolean v = (Boolean) value;
            value = v ? 1 : 0;
        }else if(null == value || StringUtil.isBlank(value.toString())){
            value = "''";
        }
        return value;
    }*/
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param type 枚举  delete  select
     * @param whereList
     * @return
     */
    public String commonSqlByType(String type , WhereDTO... whereList) {
    	StringBuilder sqlBuild = new StringBuilder();
    	if(type.equals("select")) {
    		type += " "+columnsStr;
    	}
        sqlBuild.append(type + " from ").append(getTableName());
        String sql = sqlBuild.toString();
        String whereSql = "";
        for (WhereDTO whereDTO : whereList) {
        	whereSql += LinkType.AND + " " + whereDTO.getKey() + whereDTO.getOperator()+ whereDTO.getValue();
		}
        whereSql = whereSql.substring(3);
        return sql+" where "+whereSql;
        
	}
    
    /**
     * 生成新增的SQL--预处理方式，prestatement方式
     * 
     * @param isAutoPk 是否提供主键自增字段 
     * @return
     */
    public String preCreate(boolean isAutoPk) {
    	
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("INSERT INTO ").append(tableName).append("(");
        if(isAutoPk) {
        	sqlBuild.append(otherIdColumnsStr);
        } else {
        	sqlBuild.append(columnsStr);
        }
        sqlBuild.append(") values(");
        String sql = sqlBuild.toString();
        int size = otherIdColumns.size();
        String charValue="";
        for(int i=0; i < size;i++) {
        	charValue+=",?";
        }
        charValue = charValue.substring(1);
        logger.info("生成的SQL为: " + sql+""+charValue+")");
        return sql+""+charValue+")";
    }
    
    /**
     * 生成批量新增的SQL
     * 
     * @param size
     * @param currentColumnFieldNames
     * @param isMycat
     * @return
     */
    public String createOfBatch(int size,Map<String, String> currentColumnFieldNames,boolean isMycat) {
        StringBuilder sqlBuild = new StringBuilder();
         	if(isMycat) {
         		sqlBuild.append("/*!mycat:catlet=demo.catlets.BatchInsertSequence*/");
         	}
            sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                    .append(otherIdColumnsStr).append(") values ");
            String values = "";
            for(int i=0; i<otherIdColumns.size();i++) {
            	values += ",?";
            }
            values = values.substring(1);
            for (int i=0; i < size; i++) {
                if (i == 0) {
                    sqlBuild.append(" ( ");
                } else {
                    sqlBuild.append(" ),( ");
                }
                sqlBuild.append(values);
                if(i == size-1) {
                	sqlBuild.append(")");
                }
            }
         
        String sql = sqlBuild.toString();
         
        logger.info("生成的SQL为: " + sql);
         
        return sql;
    }
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param name
     * @return
     */
    public String remove(String name) {
        WhereDTO where = new WhereDTO();
        where.setKey(name);
        where.setOperator(" = ");
        where.setValue("?");
        return commonSqlByType("delete",where);
	}
    
    /**
     * 生成根据ID删除的SQL
     * 
     * @param id
     * @return
     */
    public <PK> String removeById() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
         
        String sql = sqlBuild.toString();
         
        //logger.info("生成的SQL为: " + sql);
        
        return sql+" where "+pkName+" = ?";
    }
     
    /**
     * 生成根据IDs批量删除的SQL
     * 
     * @param idsLength
     * @return
     */
    public <PK> String removeOfBatch(int idsLength) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
        String sql = sqlBuild.toString();
        
        String value = pkName+" IN (";
        for (int i=0; i < idsLength; i++) {
            if (i > 0 && i % (BatchOperator.DELETE_CRITICAL_VAL.getSize() - 1) == 0) {
            	if(i == idsLength-1) {
            		value += "?" + ")";
            	} else {
            		value += "?" + ") OR "+ pkName+" IN (";
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
        logger.info("生成的SQL为: " + sql);
        return sql;
    }
     
    public String removeAll() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
        String sql = sqlBuild.toString();
        logger.info("生成的SQL为: " + sql);
        return sql;
    }
     
    /**
     * 生成更新的SQL
     * 
     * @param t
     * @param currentColumnFieldNames
     * @param whereColumn
     * @param isAllField
     * @return
     */
    public String update(T t, Map<String, String> currentColumnFieldNames,String whereColumn,Boolean... isAllField) {
    	
    	String values = "";
        for (int i=0; i < otherIdColumns.size();i++) {
        	String column = otherIdColumns.get(i);
            Object value = ReflectionUtil.obtainFieldValue(t,currentColumnFieldNames.get(column));
            if((isAllField == null||isAllField.length == 0 || isAllField[0] || value != null)&&column!=whereColumn) {
//              values += ","+column + "=" + handleValue(value);
            	values += ","+column + "=?";
        	}
        }
        values = values.substring(1);
    	
//        Object id = ReflectionUtil.obtainFieldValue(t,currentColumnFieldNames.get(pkName));
//        if(id == null) {
//        	throw new UncheckedException("主键为空，非法操作");
//        }
//        id = handleValue(id);
//        if(id == null) {
//        	throw new UncheckedException("主键为空，非法操作");
//        }
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("UPDATE ").append(tableName).append(" SET ")
                .append(values).append(" WHERE ")
                .append(whereColumn).append(" = ").append("?");
         
        String sql = sqlBuild.toString();
         
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
    }
     
    /**
     * 生成根据ID查询的SQL 已测试
     * 
     * @return
     */
    public <PK>  String findById() {
        WhereDTO where = new WhereDTO();
        where.setKey(pkName);
        where.setOperator(" = ");
        where.setValue("?");
        return commonSqlByType("select",where);
         
    }
    
    /**
     * 生成根据属性查询的SQL 已测试
     * 
     * @param key
     * @param value
     * @return
     */
    public <V>  String findByProperties(String key,V value) {
        WhereDTO where = new WhereDTO();
        where.setKey(key);
        where.setOperator(" = ");
        where.setValue(value);
        return commonSqlByType("select",where);
         
    }
    
    /**
     * 
     * 方法用途: 生成根据ID查询的SQL<br>
     * 操作步骤: TODO<br>
     * @param size
     * @return
     */
    public <PK> String findByIds(int size) {
    	String idArr = "";
    	for (int i=0; i<size; i++) {
			idArr += ",?";
		}
    	idArr = idArr.substring(1);
    			
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName())
                .append(" WHERE " + pkName + " in (" + idArr+")");
        String sql = sqlBuild.toString();
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
         
    }
    
    /**
     * 
     * 方法用途: 生成多属性值查询的sql<br>
     * 操作步骤: TODO<br>
     * @param name
     * @param size
     * @return
     */
    public <PK> String findByMutil(String name, int size) {
    	
    	String idArr = "";
    	for (int i=0; i<size; i++) {
			idArr += ",?";
		}
    	idArr = idArr.substring(1);
    	
    	return findByMutil(name,idArr);
    }
    
    /**
     * 
     * 方法用途: 生成多属性值查询的sql<br>
     * 操作步骤: TODO<br>
     * @param name
     * @param values
     * @return
     */
    public String findByMutil(String name, String values) {
    	StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName())
                .append(" WHERE " + name + " in (" + values +")");
        String sql = sqlBuild.toString();
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
	}
    
    /**
     * 生成查询所有的SQL
     * 
     * @return
     */
    public String findAll() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName());
        String sql = sqlBuild.toString();
         
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
    }

	public String findBy(String where) {
		
		StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName());
        // FIXED geny 2016-05-27 增加空字符串判断 风险提醒：这个分支会导致拖死数据库  TODO 整合块2
        if(!StringUtil.isEmpty(where)){
        	sqlBuild.append(" WHERE " + where);
        }
        String sql = sqlBuild.toString();
         
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
	}
	
    public  String count(String where) {
    	StringBuilder sqlBuild = new StringBuilder();
    	sqlBuild.append("select count(1) from ").append(getTableName());
    	if(StringUtil.isNotBlank(where)) {
    		sqlBuild.append(" where ").append(where);
    	}
        return sqlBuild.toString();
    }
    
    /**
     * 方法用途: 查询整表数据总量-无过滤条件-慎重使用<br>
     * 操作步骤: TODO<br>
     * @return
     */
    public String count() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT COUNT(1) ").append(" FROM ")
                .append(getTableName());
        String sql = sqlBuild.toString();
         
        //logger.info("生成的SQL为: " + sql);
         
        return sql;
    }

	public void setTableIndexLocal(Integer index) {
		tableIndexLocal.set(index);
	}

	public ThreadLocal<Integer> getTableIndexLocal() {
		return tableIndexLocal;
	}

	public void setTableIndexLocal(ThreadLocal<Integer> tableIndexLocal) {
		this.tableIndexLocal = tableIndexLocal;
	}
	
	public String createTable() {
		StringBuilder sqlBuild = new StringBuilder();
		createColum(sqlBuild, pkName);//设置主键
		sqlBuild.append(" NOT NULL AUTO_INCREMENT");//自动递增
		sqlBuild.append(",");
		for(String colName : otherIdColumns) {
			createColum(sqlBuild, colName);
			sqlBuild.append(",");
		}
		sqlBuild.append("PRIMARY KEY ("+pkName+")");//设置主键
        return sqlBuild.toString();
	}
	
	private void createColum(StringBuilder sqlBuild, String colName) {
		sqlBuild.append(colName);
		sqlBuild.append(" ");
		String type = columnsMeta.get(colName);
		if(colName.equals("updateTime")) {
			type = "timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP";//更新时间有数据库来产生，无需手动设置
		} else if("java.lang.String".equals(type)) {
			type = "varchar(100)";
		} else if("java.lang.Integer".equals(type)||"int".equals(type)){
			type = "int";
		} else if("java.lang.Boolean".equals(type)||"boolean".equals(type)){
			type = "bit";
		} else if("java.util.Date".equals(type)){
			type = "datetime";
		} else {
			type = "varchar(100)";
		}
		sqlBuild.append(type);
	}
	
}