package com.meizu.simplify.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

/**
  * <p><b>Title:</b><i>数据库类型和java数据类型匹配工具类</i></p>
 * <p>Desc: TODO </p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2016年4月26日 上午11:35:50</p>
 * <p>Modified By:luchuangye-</p>
 * <p>Modified Date:2016年4月26日 上午11:35:50</p>
 * @author <a href="mailto:luchuangye@meizu.com">luchuangye</a>
 * @version Version 0.1
 *
 */
public class MapperTypeUtil{
	
	/**
	 * 
	 * 方法用途: 匹配数据库字段类型和实体属性类型，并转换成实体类型<br>
	 * 操作步骤: TODO<br>
	 * @param val
	 * @return
	 */
	public static Class<?> mapperOrmType(Object val,boolean useNewDate) {
		Class<?> valClazz = val.getClass();
		if(valClazz == Timestamp.class) {
			valClazz = Date.class;
		} else if(valClazz == java.sql.Date.class) {
			if(useNewDate) {
				valClazz = LocalDate.class; //目前mysql的jdbc驱动不支持
			} else {
				valClazz = Date.class; 
			}
		}
		return valClazz;
	}
	public static Object convertOrmType(Object val,Class<?> valClass,boolean useNewDate) {
		if(valClass == java.sql.Date.class) {
	    	if(useNewDate) {
				val = LocalDate.parse(val.toString());
			} else {
				val = DateUtil.parse(val.toString());
			}
	    } else if(valClass == String.class) {
	    	val = String.valueOf(val);
	    }
		return val;
	}
}
