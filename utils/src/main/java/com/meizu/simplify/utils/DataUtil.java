package com.meizu.simplify.utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * <p><b>Title:</b><i>参数解析工具。</i></p>
 * <p>Desc: 提供各种对数据进行处理的方法,待优化和调整  TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年3月20日 下午12:44:59</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年3月20日 下午12:44:59</p>
 * @author <a href="mailto:luchuangye@meizu.com" >lcy</a>
 * @version Version 0.1
 *
 */
public class DataUtil {

	private DataUtil() {
	}
	
	/**
	 * 
	 * 方法用途: 解析参数为布尔类型，<br>
	 * 操作步骤: TODO:如果value值非false或是true的情况下，会返回false，比如value=selsd的时候，会返回false<br>
	 * @param value
	 * @return
	 */
	public static Boolean parseBoolean(Object value) {
		return ObjectUtil.isBoolean(value);
	}
	
	/**
	 * 
	 * 方法用途: 将参数解析为int类型，为空时返回0<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @return
	 */
	public static Integer parseInt(Object value) {
		return parseInt(value, 0);
	}

	/**
	 * 
	 * 方法用途: 将参数解析为int类型，为空时返回defaultValue<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Integer parseInt(Object value, Integer defaultValue) {
		if (value == null) {
			return defaultValue;
		} 
        try {
        	return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
	}

	/**
	 *
	 * 方法用途: 将参数解析为long类型，为空时返回0<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @return
	 */
	public static Long parseLong(Object value) {
		return parseLong(value, 0L);
	}

	/**
	 *
	 * 方法用途: 将参数解析为long类型，为空时返回defaultValue<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Long parseLong(Object value, Long defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			return new Long(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 *
	 * 方法用途: 将参数解析为double类型，为空时返回0<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @return
	 */
	public static Double parseDouble(Object value) {
		return parseDouble(value, 0.0);
	}

	/**
	 *
	 * 方法用途: 将参数解析为double类型，为空时返回defaultValue<br>
	 * 操作步骤: TODO<br>
	 * @param value
	 * @param defaultValue
	 * @return
	 */
	public static Double parseDouble(Object value, Double defaultValue) {
		if (value == null) {
			return defaultValue;
		}
		try {
			return new Double(value.toString());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * 
	 * 方法用途: 未知类型转换成具体类型<br>
	 * 操作步骤: 注意：次方法一般用在框架级，插件机，通用性的功能处理上，如果在有确切类型的情况下，不要使用这个方法，而是调用具体的parseXxx方法<br>
	 * @param classType
	 * @param value
	 * @param isNull wrapper包装基本类型是否默认值以null处理
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Serializable> T convertType(Class<?> classType, Object value,boolean isNull) {
		if(classType == Integer.class ) {
			if(isNull) {
				return (T) parseInt(value,null);
			} else {
				return (T) parseInt(value);
			}
		} else 	if(classType == int.class) {
			return (T) parseInt(value);
		} else if(classType == Long.class) {
			if(isNull) {
				return (T) parseLong(value,null);
			} else {
				return (T) parseLong(value);
			}
		} else if(classType == long.class) {
			return (T) parseLong(value);
		} else if(classType == Double.class) {
			if(isNull) {
				return (T) parseDouble(value,null);
			} else {
				return (T) parseDouble(value);
			}
		} else if(classType == double.class) {
			return (T) parseDouble(value);
		} else if(classType == Boolean.class || classType == boolean.class) {//fixd whb 2016/4/3
			return (T) parseBoolean(value);
		} else if(classType == Date.class) {
			return (T) DateUtil.parse(String.valueOf(value));
		} else if(classType == LocalDate.class) {
			if(StringUtil.isNotBlank(String.valueOf(value))) {
				return (T) LocalDate.parse(String.valueOf(value));
			}
		} else if(classType == LocalTime.class) {
			if(StringUtil.isNotBlank(String.valueOf(value))) {
				return (T) LocalTime.parse(String.valueOf(value));
			}
		} else if(classType == LocalDateTime.class) {
			if(StringUtil.isNotBlank(String.valueOf(value))) {
				return (T) LocalDateTime.parse(String.valueOf(value));
			}
		}
		return (T)value;
	}
	
}
