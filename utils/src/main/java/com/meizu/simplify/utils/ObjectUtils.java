package com.meizu.simplify.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * <b>Title:</b><i>对象工具类</i> Copyright:Copyright(c)2014 Company:meizu Create
 * Date:2015年2月14日 上午11:42:15 Modified By:lcy-
 * 
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 */
public class ObjectUtils {
	/**
	 * 是否为空对象 是则返回参数2
	 * 
	 * @param obj
	 *            obj1
	 * @return Object
	 */
	public static Object notNull(Object obj, Object obj1) {
		return (obj == null || "".equals(obj)) ? obj1 : obj;
	}

	/**
	 * 是否为空指针
	 * 
	 * @param obj
	 * @return boolean
	 */
	public static boolean isNull(Object obj) {
		return obj == null ? true : false;
	}

	public static boolean isInt(Object s) {
		try {
			Integer.valueOf(s.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isFloat(Object s) {
		try {
			Float.valueOf(s.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isLong(Object s) {
		try {
			Long.valueOf(s.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean isBoolean(Object s) {
		try {
			Boolean.valueOf(s.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 对象合并
	 * 
	 * 将后者值合并到前者, 最好只用在基础数据类型属性对象
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 */
	public static void merge(Object merge_object, Object object) throws Exception {
		Class<?> classType = object.getClass();
		Field fields[] = classType.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			String fieldName = field.getName();
			String firstLetter = fieldName.substring(0, 1).toUpperCase();
			String getMethodName = "get" + firstLetter + fieldName.substring(1);
			String setMethodName = "set" + firstLetter + fieldName.substring(1);
			Method getMethod = classType.getMethod(getMethodName, new Class[] {});
			Method setMethod = classType.getMethod(setMethodName, new Class[] { field.getType() });
			Object value = getMethod.invoke(object, new Object[] {});
			if (value != null && value.toString().length() > 0)
				setMethod.invoke(merge_object, new Object[] { value });
		}
	}

	/**
	 * 反射对象
	 * 
	 * @param Path
	 * @return Class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> getObject(String Path) throws ClassNotFoundException {
		return Class.forName(Path);
	}

	public static void getObjectValue(Object object) throws Exception {
		if (object != null) {
			Class<?> clz = object.getClass();
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equals("serialVersionUID"))
					continue;
				Method m = (Method) object.getClass().getMethod("get" + firstToUpperCase(field.getName()));
				String val = (String) m.invoke(object);
				System.out.println(field.getName() + "类型：" + field.getGenericType() + "值：" + val);// 打印该类的所有属性类型
			}
		}
	}

	/**
	 * 首字母大写
	 * 
	 * @param @param str
	 * @throws
	 */
	public static String firstToUpperCase(String str) throws Exception {
		return str.substring(0, 1).toUpperCase() + str.substring(1, str.length());
	}

}
