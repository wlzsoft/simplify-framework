package com.meizu.data.mybatis;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meizu.data.Constant;
import com.meizu.data.annotations.Key;
import com.meizu.data.mybatis.base.BaseDTO;
import com.meizu.data.mybatis.base.SaveDTO;
import com.meizu.data.mybatis.base.WhereDTO;
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
     
    protected Logger    logger    = LoggerFactory.getLogger(this.getClass());
    private Set<String>    columns;
    private String        tableName;
    private String        columnsStr;
    private String        pkName;
    private String        seq;
    private ThreadLocal<Integer> tableIndexLocal = new ThreadLocal<>();
    public SQLBuilder(Set<String> columns, String tableName, String pkName,
            String seq) {
        super();
        this.columns = columns;
        this.tableName = tableName;
        this.pkName = pkName;
        this.seq = seq;
        this.columnsStr = StringUtil.join(this.columns, ",");
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
    private List<Object> obtainFieldValues(T t,
            Map<String, String> currentColumnFieldNames, List<String> columnsNames) {
        List<Object> values = new LinkedList<Object>();
        for (String column : columns) {
//            Object value = ReflectionUtils.obtainFieldValue(t,
//                    currentColumnFieldNames.get(column));
            Field field = ReflectionUtil.getField(t,
                  currentColumnFieldNames.get(column));
            
            Key primaryKey = field.getAnnotation(Key.class);
    		if(primaryKey != null&&primaryKey.auto()) {
    			continue;
    		}
    		if(columnsNames !=null) {
    			columnsNames.add(column);
    		}
            Object value = null;
            try {
            	value = field.get(t);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
            if(column.equalsIgnoreCase(pkName) &&  null == value){
                value = seq;
            }else{
            	if(value != null) {
        			value = handleValue(value);
        		} else {
        			if(field.getType().equals(Date.class)) {
        				value = "null";
        			} else if(field.getType().equals(Integer.class)){
        				value = 0;
        			} else {
        	            value = "''";
        	        }
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
        } else if (value instanceof Date||value instanceof java.sql.Date) {//对应实体中sql。Date的属性处理
            Date date = (Date) value;
            
//          String dateStr = DateUtils.getDate(date,"YYYY-MM-DD HH24:MI:SS.FF3");
            String dateStr = DateUtil.formatDate(date);
            value = "'"+dateStr+"'";//"TO_TIMESTAMP('" + dateStr+ "','YYYY-MM-DD HH24:MI:SS.FF3')";
        } else if (value instanceof Boolean) {
            Boolean v = (Boolean) value;
            value = v ? 1 : 0;
        }else if(null == value || StringUtil.isBlank(value.toString())){
            value = "''";
        }
        return value;
    }
    
    /**
     * 提供给生成更新SQL使用
     * 
     * @param t
     * @param currentColumnFieldNames
     * @return
     */
    private List<String> obtainColumnVals(T t,
            Map<String, String> currentColumnFieldNames) {
        List<String> colVals = new LinkedList<String>();
        for (String column : columns) {
            Object value = ReflectionUtil.obtainFieldValue(t,
                    currentColumnFieldNames.get(column));
            if (value != null && !column.equalsIgnoreCase(pkName)) {
                colVals.add(column + "=" + handleValue(value));
            }
        }
        return colVals;
    }
    
    
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param where
     * @return
     */
    public BaseDTO commonSqlByType(String type,WhereDTO where) {
        List<WhereDTO> list = new ArrayList<WhereDTO>();
        list.add(where);
        return commonSqlByType(type,list);
	}
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param whereList
     * @param type 枚举  delete  select
     * @return
     */
    public BaseDTO commonSqlByType(String type , List<WhereDTO> whereList) {
    	StringBuilder sqlBuild = new StringBuilder();
    	if(type.equals("select")) {
    		type += " "+columnsStr;
    	} else if(type.equals("count")) {
    		type = "select count(1) ";
    	} else if(type.equals("update")) {/*不在这实现*/} 
    	  else if(type.equals("delete")) {/*默认不需要处理*/}
        sqlBuild.append(type + " FROM ").append(getTableName());
        String sql = sqlBuild.toString();
        return commonSql(sql,whereList);
	}
    



	/**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param whereList
     * @return
     */
    public BaseDTO commonSql(String sql , List<WhereDTO> whereList) {
    	BaseDTO dto = new BaseDTO();
        dto.setSql(sql);
        dto.setWhereList(whereList);
        return dto;
	}
    
    /**
     * 生成新增的SQL
     * 
     * @param t
     * @param currentColumnFieldNames
     * @return
     */
    public SaveDTO create(T t, Map<String, String> currentColumnFieldNames) {
    	List<String> columns = new ArrayList<String>();
        List<Object> values = obtainFieldValues(t, currentColumnFieldNames,columns);
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                .append(StringUtil.join(columns, ",")).append(")values(")
                .append(StringUtil.join(values, ",")).append(")");
        String sql = sqlBuild.toString();
         
        logger.debug("生成的SQL为: " + sql);
        SaveDTO dto = new SaveDTO();
        dto.setSql(sql);
        return dto;
    }
     
    /**
     * 生成批量新增的SQL
     * 
     * @param list
     * @param currentColumnFieldNames
     * @return
     */
    public String createOfBatch(List<T> list,
            Map<String, String> currentColumnFieldNames,Object pkVal) {
        StringBuilder sqlBuild = new StringBuilder();
         
        if(StringUtil.isNotBlank(seq) && null == pkVal){
            // ID使用序列
            sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                    .append(columnsStr).append(")");
            sqlBuild.append(" SELECT ").append(seq).append(" , a.* FROM ( ");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                List<Object> values = obtainFieldValues(t,
                        currentColumnFieldNames,null);
                values.remove(seq);
                 
                if (i == 0) {
                    sqlBuild.append(" SELECT ");
                } else {
                    sqlBuild.append(" UNION ALL SELECT ");
                }
                int j = 0, vlen = values.size();
                for (; j < vlen; j++) {
                    sqlBuild.append(values.get(j)).append(" AS T").append(j);
                    if (j != (vlen - 1)) {
                        sqlBuild.append(",");
                    }
                }
                sqlBuild.append(" FROM DUAL ");
            }
            sqlBuild.append(" ) a ");
        } else {
        	List<String> columns = new ArrayList<String>();
            List<Object> values = obtainFieldValues(list.get(0), currentColumnFieldNames,columns);
            //ID没有使用序列
            sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                    .append(StringUtil.join(columns, ",")).append(") values ");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                //List<String> columns = new ArrayList<String>();
//                List<Object> values = obtainFieldValues(t, currentColumnFieldNames,columns);
                values = obtainFieldValues(t, currentColumnFieldNames,null);
                 
                if (i == 0) {
                    sqlBuild.append(" ( ");
                } else {
                    sqlBuild.append(" ),( ");
                }
                sqlBuild.append(StringUtil.join(values, ","));
                if(i == list.size()-1) {
                	sqlBuild.append(")");
                }
            }
        }
         
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }
    
    /**
     * 生成批量新增的SQL
     * 
     * @param list
     * @param currentColumnFieldNames
     * @return
     */
    public String createOfBatchByMycat(List<T> list,
            Map<String, String> currentColumnFieldNames,Object pkVal) {
        StringBuilder sqlBuild = new StringBuilder();
         
        if(StringUtil.isNotBlank(seq) && null == pkVal){
            // ID使用序列
            sqlBuild.append("/*!mycat:catlet=demo.catlets.BatchInsertSequence*/INSERT INTO ").append(tableName).append("(")
                    .append(columnsStr).append(")");
            sqlBuild.append(" SELECT ").append(seq).append(" , a.* FROM ( ");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                List<Object> values = obtainFieldValues(t,
                        currentColumnFieldNames,null);
                values.remove(seq);
                 
                if (i == 0) {
                    sqlBuild.append(" SELECT ");
                } else {
                    sqlBuild.append(" UNION ALL SELECT ");
                }
                int j = 0, vlen = values.size();
                for (; j < vlen; j++) {
                    sqlBuild.append(values.get(j)).append(" AS T").append(j);
                    if (j != (vlen - 1)) {
                        sqlBuild.append(",");
                    }
                }
                sqlBuild.append(" FROM DUAL ");
            }
            sqlBuild.append(" ) a ");
        } else {
        	List<String> columns = new ArrayList<String>();
            List<Object> values = obtainFieldValues(list.get(0), currentColumnFieldNames,columns);
            //ID没有使用序列
            sqlBuild.append("/*!mycat:catlet=demo.catlets.BatchInsertSequence*/INSERT INTO ").append(tableName).append("(")
                    .append(StringUtil.join(columns, ",")).append(") values ");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                //List<String> columns = new ArrayList<String>();
//                List<Object> values = obtainFieldValues(t, currentColumnFieldNames,columns);
                values = obtainFieldValues(t, currentColumnFieldNames,null);
                 
                if (i == 0) {
                    sqlBuild.append(" ( ");
                } else {
                    sqlBuild.append(" ),( ");
                }
                sqlBuild.append(StringUtil.join(values, ","));
                if(i == list.size()-1) {
                	sqlBuild.append(")");
                }
            }
        }
         
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }
    
    /**
     * 生成批量新增的SQL
     * 
     * @param list
     * @param currentColumnFieldNames
     * @return
     */
    public String createOfBatchForOracle(List<T> list,
            Map<String, String> currentColumnFieldNames,Object pkVal) {
        StringBuilder sqlBuild = new StringBuilder();
         
        if(StringUtil.isNotBlank(seq) && null == pkVal){
            // ID使用序列
            sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                    .append(columnsStr).append(")");
            sqlBuild.append(" SELECT ").append(seq).append(" , a.* FROM ( ");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                List<Object> values = obtainFieldValues(t,
                        currentColumnFieldNames,null);
                values.remove(seq);
                 
                if (i == 0) {
                    sqlBuild.append(" SELECT ");
                } else {
                    sqlBuild.append(" UNION ALL SELECT ");
                }
                int j = 0, vlen = values.size();
                for (; j < vlen; j++) {
                    sqlBuild.append(values.get(j)).append(" AS T").append(j);
                    if (j != (vlen - 1)) {
                        sqlBuild.append(",");
                    }
                }
                sqlBuild.append(" FROM DUAL ");
            }
            sqlBuild.append(" ) a ");
        } else {
            //ID没有使用序列
            sqlBuild.append("INSERT INTO ").append(tableName).append("(")
                    .append(columnsStr).append(")");
            for (int i=0; i < list.size(); i++) {
                T t = list.get(i);
                List<Object> values = obtainFieldValues(t, currentColumnFieldNames,null);
                 
                if (i == 0) {
                    sqlBuild.append(" SELECT ");
                } else {
                    sqlBuild.append(" UNION ALL SELECT ");
                }
                sqlBuild.append(StringUtil.join(values, ",")).append(
                        " FROM DUAL ");
            }
        }
         
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
         
        return sql;
    }
    
    
    /**
     * 
     * 方法用途: TODO<br>
     * 操作步骤: TODO<br>
     * @param name
     * @param value
     * @return
     */
    public BaseDTO remove(String name, Object value) {
        WhereDTO where = new WhereDTO();
        where.setKey(name);
        where.setOperator(" = ");
        where.setValue(new String[]{value.toString()});
        return commonSqlByType("delete",where);
	}
    
    
    
    /**
     * 生成根据ID删除的SQL
     * 
     * @param id
     * @return
     */
    public <PK> BaseDTO removeById(PK id) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
         
        String sql = sqlBuild.toString();
         
        //logger.debug("生成的SQL为: " + sql);
        BaseDTO dto = new BaseDTO();
        dto.setSql(sql);
        List<WhereDTO> list = new ArrayList<WhereDTO>();
        WhereDTO where = new WhereDTO();
        where.setKey(pkName);
        where.setOperator(" = ");
        where.setValue(id);
        list.add(where);
        
        //test start 测试用，后面删除
//        where = new WhereDTO();
//        where.setKey(pkName);
//        where.setOperator(" = ");
//        where.setValue("0");
//        list.add(where);
        //test end
        
        dto.setWhereList(list);
        return dto;
    }
     
    /**
     * 生成根据IDs批量删除的SQL
     * 
     * @param ids
     * @return
     */
    public <PK> BaseDTO removeOfBatch(List<PK> ids) {
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("DELETE FROM ").append(getTableName());
        BaseDTO dto = new BaseDTO();
        String sql = sqlBuild.toString();
        dto.setSql(sql);
        
        List<WhereDTO> list = new ArrayList<WhereDTO>();
        WhereDTO where = new WhereDTO();
        where.setKey(pkName);
        where.setOperator(" IN ");
        String value = "";
        for (int i=0; i < ids.size(); i++) {
            PK id = ids.get(i);
            if(i > 0) {
            	value += ",";	
            }
            value+=id;
            
            if (i > 0 && i % (Constant.DELETE_CRITICAL_VAL - 1) == 0) {
            	where.setValue(value.split(","));
                list.add(where);
                value = " 0 ";
                where = new WhereDTO();
                where.setKey(pkName);
                where.setOperator(" IN ");
            }
        }
        String[] valueArr = value.split(",");  // TODO 需要优化，没必要先用字符串拼接，有转成数组,直接数据处理就可以
        where.setValue(valueArr);
        list.add(where);
        //logger.debug("生成的SQL为: " + sql);
        dto.setWhereList(list);
        return dto;
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
        List<String> values = obtainColumnVals(t, currentColumnFieldNames);
        Object id = ReflectionUtil.obtainFieldValue(t,
                currentColumnFieldNames.get(pkName));
        if(id == null) {
        	throw new UncheckedException("主键为空，非法操作");
        }
        id = handleValue(id);
        if(id == null) {
        	throw new UncheckedException("主键为空，非法操作");
        }
        StringBuilder sqlBuild = new StringBuilder();
        sqlBuild.append("UPDATE ").append(tableName).append(" SET ")
                .append(StringUtil.join(values, ",")).append(" WHERE ")
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
    public <PK>  BaseDTO findById(PK id) {
//        StringBuilder sqlBuild = new StringBuilder();
//        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ").append(getTableName());
//        SelectDTO dto = new SelectDTO();
//        dto.setSql(sqlBuild.toString());
//        dto.setWhere(pkName);
//        dto.setVal(id.toString());
        WhereDTO where = new WhereDTO();
        where.setKey(pkName);
        where.setOperator(" = ");
        where.setValue(id);
        return commonSqlByType("select",where);
         
    }
    
    /**
     * 生成根据属性查询的SQL 已测试
     * 
     * @param id
     * @return
     */
    public <V>  BaseDTO findByProperties(String key,V value) {
//        StringBuilder sqlBuild = new StringBuilder();
//        sqlBuild.append("SELECT ").append(columnsStr).append(" FROM ").append(getTableName());
//        SelectDTO dto = new SelectDTO();
//        dto.setSql(sqlBuild.toString());
//        dto.setWhere(pkName);
//        dto.setVal(id.toString());
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
    public  BaseDTO findPage(List<WhereDTO> whereList) {
        return commonSqlByType("select",whereList);
         
    }
    public  BaseDTO count(List<WhereDTO> whereList) {
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