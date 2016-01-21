package com.meizu.simplify.utils;

/**
 * 
 * <p><b>Title:</b><i>对象工具类</i></p>
 * <p>Desc: TODO</p>
 * <p>source folder:{@docRoot}</p>
 * <p>Copyright:Copyright(c)2014</p>
 * <p>Company:meizu</p>
 * <p>Create Date:2015年2月14日 上午11:42:15</p>
 * <p>Modified By:lcy-</p>
 * <p>Modified Date:2015年2月14日 上午11:42:15</p>
 * @author <a href="mailto:luchuangye@meizu.com" title="邮箱地址">lcy</a>
 * @version Version 0.1
 *
 */
public class ObjectUtil {

	/**
	 * 方法用途: 是否为空指针<br>
	 * 操作步骤: TODO<br>
	 * @param str 待判断的字符串
	 * @return
	 */
	public static boolean isNull(Object str) {
		return str == null ? true : false;
	}
	
	/**
	 * 方法用途: 是否不为空，是为true，否为false<br>
	 * 操作步骤: TODO<br>
	 * @param str 待判断的字符串
	 * @return
	 */
	public static boolean isNotNull(Object str) {
		return !isNull(str);
	}
	
	/**
	 * 方法用途: 不定长参数,其中所有参数为null，则返回true,否则返回false<br>
	 * 操作步骤: TODO<br>
	 * @param str 一个或多个待判断的字符串
	 * @return 
	 */
	public static boolean isNull(Object... str) {
		for (Object s : str) {
			if (isNotNull(s)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * 方法用途: 是否是Int类型<br>
	 * 操作步骤: TODO<br>
	 * @param s
	 * @return
	 */
	public static boolean isInt(Object s) {
		try {
			Integer.valueOf(s.toString());
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 方法用途: 是否是Float类型<br>
	 * 操作步骤: TODO<br>
	 * @param s
	 * @return
	 */
	public static boolean isFloat(Object s) {
		try {
			Float.valueOf(s.toString());
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 方法用途: 是否是Long类型<br>
	 * 操作步骤: TODO<br>
	 * @param s
	 * @return
	 */
	public static boolean isLong(Object s) {
		try {
			Long.valueOf(s.toString());
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 方法用途: 是否是Boolean类型<br>
	 * 操作步骤: TODO<br>
	 * @param s
	 * @return
	 */
	public static boolean isBoolean(Object s) {
		try {
			Boolean.valueOf(s.toString());
		} catch ( Exception e ) {
			return false;
		}
		return true;
	}
}
