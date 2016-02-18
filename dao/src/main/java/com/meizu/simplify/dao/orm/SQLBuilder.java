package com.meizu.simplify.dao.orm;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.simplify.dao.BatchOperator;
import com.meizu.simplify.dao.annotations.Value;
import com.meizu.simplify.dao.dto.BaseDTO.LinkType;
import com.meizu.simplify.dao.dto.WhereDTO;
import com.meizu.simplify.exception.UncheckedException;
import com.meizu.simplify.utils.DataUtil;
import com.meizu.simplify.utils.DateUtil;
import com.meizu.simplify.utils.ReflectionUtil;
import com.meizu.simplify.utils.StringUtil;
 
/**
 * 
 * <p><b>Title:</b><i> sql构造器</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月10日 下午4:29:25</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月10日 下午4:29:25</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 * @param <T>
 */
public class SQLBuilder<T> {
     
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<String> columns;
    private List<String> otherIdColumns;
	private String tableName;
    private String columnsStr;
    private String otherIdColumnsStr;
    private String pkName;
    private ThreadLocal<Integer> tableIndexLocal = new ThreadLocal<>();
    public SQLBuilder(List<String> otherIdColumns,List<String> columns, String tableName, String pkName) {
        super();
        this.columns = columns;
        this.otherIdColumns = otherIdColumns;
        this.tableName = tableName;
        this.pkName = pkName;
        this.columnsStr = StringUtil.join(columns, ",");
        this.otherIdColumnsStr = StringUtil.join(otherIdColumns, ",");
    }
    public List<String> getOtherIdColumns() {
		return otherIdColumns;
	}
    public String getTableName() {
    	if(tableIndexLocal.get() == null) {
    		return this.tableName;
    	} else {
    		return this.tableName+"_"+tableIndexLocal.get();
    	}
	}
   
     
    /**
     * 提供给生成新增SQL 使用
     * 
     * @param t
     * @param currentColumnFieldNames
     * @param columnsNames 
     * @return
     */
    public List<Object> obtainFieldValues(T t,Map<String, String> currentColumnFieldNames) {
        List<Object> values = new LinkedList<Object>();
        for (String column : otherIdColumns) {
            Field field = ReflectionUtil.getField(t,currentColumnFieldNames.get(column));
            
            Object value = null;
            try {
            	value = field.get(t);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
        	if(value != null) {
//        			value = handleValue(value);//TODO
    		} else {
    			if(field.getType().equals(Date.class)) {
    				value = "null";
    			} else if(field.getType().equals(Integer.class)){
    				value = 0;
    			} else {
    	            value = "''";
    	        }
    		}
            values.add(value);
        }
        return values;
    }
     
    /**
     * 处理value
     * 
     * @param value
     * @param field 
     * @return
     */
    private Object handleValue(Object value) {
        if (value instanceof String) {
            value = "\'" + value + "\'";
        } else if (value instanceof Date||value instanceof java.sql.Date) {//对应实体中java.sql.Date类型 的属性处理
            Date date = (Date) value;
            String dateStr = DateUtil.formatDate(date);
            value = "'"+dateStr+"'";
        } else if (value instanceof Boolean) {
            Boolean v = (Boolean) value;
            value = v ? 1 : 0;
        }else if(null == value || StringUtil.isBlank(value.toString())){
            value = "''";
        }
        return value;
    }
    
    
    
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param whereList
     * @param type 枚举  delete  select
     * @return
     */
    public String commonSqlByType(String type , WhereDTO... whereList) {
    	StringBuilder sqlBuild = new StringBuilder();
    	if(type.equals("select")) {
    		type += " "+columnsStr;
    	} else if(type.equals("count")) {
    		type = "select count(1) ";
    	} else if(type.equals("update")) {/*不在这实现*/} 
    	  else if(type.equals("delete")) {/*默认不需要处理*/}
        sqlBuild.append(type + " FROM ").append(getTableName());
        String sql = sqlBuild.toString();
        String whereSql = "";
        for (WhereDTO whereDTO : whereList) {
        	whereSql += LinkType.AND + " " + whereDTO.getKey() + whereDTO.getOperator()+ whereDTO.getValue();
		}
        whereSql = whereSql.substring(3);
        return sql+" where "+whereSql;
        
	}
    



    /**
     * 生成新增的SQL--非预处理方式，statement方式
     * 
     * @param t
     * @param currentColumnFieldNames
     * @return
     */
    public String create(T t, Map<String, String> currentColumnFieldNames) {
        List<Object> values = obtainFieldValues(t, currentColumnFieldNames);
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                .append(otherIdColumnsStr).append(")values(")
                .append(StringUtil.join(values, ",")).append(")");
        String sql = sqlBuild.toString();
         
        logger.debug("生成的SQL为: " + sql);
        return sql;
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
    public String preCreate() {
    	
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                .append(otherIdColumnsStr).append(") values(");
        String sql = sqlBuild.toString();
        int size = otherIdColumns.size();
        String charValue="";
        for(int i=0; i < size;i++) {
        	charValue+=",?";
        }
        charValue = charValue.substring(1);
        
        return sql+""+charValue+")";
    }
    @Value("{system.isMycat}")
    private boolean isMycat = false;
    /**
     * 生成批量新增的SQL
     * 
     * @param list
     * @param currentColumnFieldNames
     * @return
     */
    public String createOfBatch(int size,Map<String, String> currentColumnFieldNames) {
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
         
        logger.debug("生成的SQL为: " + sql);
         
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
         
        //logger.debug("生成的SQL为: " + sql);
        
        return sql+" where "+pkName+" = ?";
    }
     
    /**
     * 生成根据IDs批量删除的SQL
     * 
     * @param ids
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
        //logger.debug("生成的SQL为: " + sql);
        return sql+" where "+value;
    }
     
     
    public String removeAll() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
        String sql = sqlBuild.toString();
        logger.debug("生成的SQL为: " + sql);
        return sql;
    }
     
    /**
     * 生成更新的SQL
     * 
     * @param t
     * @param currentColumnFieldNames
     * @return
     */
    public String update(T t, Map<String, String> currentColumnFieldNames) {
    	
    	String values = "";
        for (int i=0; i < otherIdColumns.size();i++) {
        	String column = otherIdColumns.get(0);
            Object value = ReflectionUtil.obtainFieldValue(t,currentColumnFieldNames.get(column));
            if(i>0) {
            	values += ",";
            }
            values += column + "=" + handleValue(value);
        }
    	
        Object id = ReflectionUtil.obtainFieldValue(t,currentColumnFieldNames.get(pkName));
        if(id == null) {
        	throw new UncheckedException("主键为空，非法操作");
        }
        id = handleValue(id);
        if(id == null) {
        	throw new UncheckedException("主键为空，非法操作");
        }
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("UPDATE ").append(tableName).append(" SET ")
                .append(values).append(" WHERE ")
                .append(pkName).append(" = ").append(id);
         
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }
     
   
     
    /**
     * 生成根据ID查询的SQL 已测试
     * 
     * @param id
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
     * @param id
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
     * @param idArr
     * @return
     */
    public <PK> String findByIds(PK[] idArr) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName())
                .append(" WHERE " + pkName + " in (" + StringUtil.join(idArr, ",")+")");
        String sql = sqlBuild.toString();
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
         
    }
    /**
     * 
     * 方法用途: 生成多属性值查询的sql<br>
     * 操作步骤: TODO<br>
     * @param name
     * @param values
     * @return
     */
    public <PK> String findByMutil(String name, PK[] values) {
    	return findByMutil(name,StringUtil.join(values, ","));
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
        //logger.debug("生成的SQL为: " + sql);
         
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
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }
     
    /**
     * 生成查询数量的SQL
     * 
     * @return
     */
    public String findAllCount() {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT COUNT(1) ").append(" FROM ")
                .append(getTableName());
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }

	public String findBy(String name, Object value) {
		
		if(value instanceof String) {
			value = "'"+value +"'";
		} else if(value instanceof Boolean) {
			if(DataUtil.parseBoolean(value)) {
				value = 1;
			} else {
				value = 0;
			}
		}
		
		StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ")
                .append(getTableName())
                .append(" WHERE " + name + " = " + value);
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
	}
    public  String findPage(WhereDTO... whereList) {
        return commonSqlByType("select",whereList);
         
    }
    public  String count(WhereDTO... whereList) {
        return commonSqlByType("count",whereList);
         
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

	
}